package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.*;
import lk.petalpink.petalpink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderStockService {

    private static final int STOCK_ADJ_TYPE_SALE = 7;
    private static final int STOCK_LOCATION_MAIN = 1;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTemplateRepository itemTemplateRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BatchProfileRepository batchProfileRepository;

    @Autowired
    private StockDetailsRepository stockDetailsRepository;

    // ─────────────────────────────────────────────────────────────────────────
    //  1. CHECK STOCK BEFORE ADDING TO CART
    //     Checks the ordered item itself (not ingredients — production handles that)
    // ─────────────────────────────────────────────────────────────────────────

    public StockCheckResultDTO checkStockForItem(Integer itemId, Double orderQuantity) {

        if (isBudgetPack(itemId)) {
            return checkBudgetPackStock(itemId, orderQuantity);
        }

        StockCheckResultDTO result = new StockCheckResultDTO();
        List<IngredientStockStatusDTO> statuses = new ArrayList<>();

        StockDTO stock = stockRepository.findByItemId(itemId);

        double available =
                (stock != null && stock.getQty() != null)
                        ? stock.getQty()
                        : 0.0;

        boolean sufficient = available >= orderQuantity;

        IngredientStockStatusDTO status = new IngredientStockStatusDTO();
        status.setSubItemId(itemId);
        status.setSubItemName(stock != null ? stock.getItemName() : "Item " + itemId);
        status.setRequiredQty(orderQuantity);
        status.setAvailableQty(available);
        status.setSufficient(sufficient);

        statuses.add(status);

        result.setAvailable(sufficient);
        result.setIngredientStatuses(statuses);
        result.setMessage(
                sufficient
                        ? "Stock available"
                        : "Insufficient stock for item"
        );

        return result;
    }

    public List<StockCheckResultDTO> checkStockForCart(List<StockCheckRequestDTO> cartItems) {
        List<StockCheckResultDTO> results = new ArrayList<>();
        for (StockCheckRequestDTO req : cartItems) {
            results.add(checkStockForItem(req.getItemId(), req.getQuantity()));
        }
        return results;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  2. DEDUCT THE ORDERED ITEM ITSELF FROM STOCK (FIFO)
    //
    //  Production has already reduced raw ingredients when the product was made.
    //  On sale we only need to reduce the finished item's stock and record
    //  one or more stock_details rows (one per batch slice consumed).
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public void deductItemStockForOrder(Integer itemId, String itemName,
                                        double orderQuantity, Integer userId) {

        // ── Resolve master stock ──────────────────────────────────────────────
        StockDTO master = stockRepository.findByItemId(itemId);

        if (master == null) {
            throw new IllegalStateException(
                    "Item not found in stock: itemId=" + itemId);
        }

        double available = master.getQty() != null ? master.getQty() : 0.0;
        if (available < orderQuantity) {
            throw new IllegalStateException(
                    "Insufficient stock for item '" + master.getItemName() +
                            "' | Required: " + orderQuantity + " | Available: " + available);
        }

        String resolvedName = (itemName != null && !itemName.isBlank())
                ? itemName : master.getItemName();

        // ── FIFO: fetch active batches for this item, oldest first ────────────
        List<FifoBatchDTO> fifoBatches =
                stockDetailsRepository.findFifoBatchesForStock(master.getStockId());

        double remaining = orderQuantity;

        if (fifoBatches != null && !fifoBatches.isEmpty()) {
            // Walk batches oldest → newest, consume what we need
            for (FifoBatchDTO batch : fifoBatches) {
                if (remaining <= 0) break;

                double consume = Math.min(batch.getAvailableQty(), remaining);
                remaining -= consume;

                stockDetailsRepository.insertSaleDetail(
                        master.getStockId(),
                        batch.getBatchRegId(),
                        STOCK_ADJ_TYPE_SALE,
                        resolvedName,
                        batch.getCostPrice(),
                        batch.getLastGrnPrice(),
                        consume,
                        master.getUnitType(),
                        userId
                );

                stockRepository.updateMasterQty(itemId, -consume);
            }
        }

        // ── Remainder not covered by any batch (no batch_reg_id) ─────────────
        if (remaining > 0.0001) {
            stockDetailsRepository.insertSaleDetail(
                    master.getStockId(),
                    null,               // no batch available for this slice
                    STOCK_ADJ_TYPE_SALE,
                    resolvedName,
                    null,
                    null,
                    remaining,
                    master.getUnitType(),
                    userId
            );
            stockRepository.updateMasterQty(itemId, -remaining);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  3. DEDUCT COURIER BAG (unchanged)
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public void deductCourierBag(Integer courierBagId, String courierBagName, Integer userId) {
        if (courierBagId == null) return;

        StockDTO master = stockRepository.findByItemId(courierBagId);
        if (master == null) {
            throw new IllegalStateException("Courier bag not found in stock: " + courierBagName);
        }

        double available = master.getQty() != null ? master.getQty() : 0.0;
        if (available < 1.0) {
            throw new IllegalStateException(
                    "Insufficient courier bag stock: " + courierBagName +
                            " | Available: " + available);
        }

        stockDetailsRepository.insertSaleDetail(
                master.getStockId(),
                null,
                STOCK_ADJ_TYPE_SALE,
                courierBagName != null ? courierBagName : "Courier Bag",
                null,
                null,
                1.0,
                master.getUnitType(),
                userId
        );

        stockRepository.updateMasterQty(courierBagId, -1.0);
    }

    public boolean isBudgetPack(Integer itemId) {

        String item = itemRepository.findItemNameById(itemId);

        if (item == null) {
            return false;
        }

        return item
                .trim()
                .toLowerCase()
                .startsWith("budget pack");
    }

    public StockCheckResultDTO checkBudgetPackStock(
            Integer budgetPackId,
            Double orderQty) {

        ItemTemplateDTO template =
                itemTemplateRepository.getTemplateByItemId(budgetPackId);

        if (template == null) {
            throw new IllegalStateException(
                    "Budget pack template not found : " + budgetPackId);
        }

        List<IngredientStockStatusDTO> statuses = new ArrayList<>();

        boolean allAvailable = true;

        for (IngredientDTO ingredient : template.getIngredients()) {

            double requiredQty =
                    ingredient.getQuantity() * orderQty;

            StockDTO stock =
                    stockRepository.findByItemId(
                            ingredient.getSubItemId());

            double availableQty =
                    stock != null && stock.getQty() != null
                            ? stock.getQty()
                            : 0.0;

            boolean sufficient =
                    availableQty >= requiredQty;

            if (!sufficient) {
                allAvailable = false;
            }

            IngredientStockStatusDTO status =
                    new IngredientStockStatusDTO();

            status.setSubItemId(
                    ingredient.getSubItemId());

            status.setSubItemName(
                    ingredient.getSubItemName());

            status.setRequiredQty(requiredQty);

            status.setAvailableQty(availableQty);

            status.setSufficient(sufficient);

            statuses.add(status);
        }

        StockCheckResultDTO result =
                new StockCheckResultDTO();

        result.setAvailable(allAvailable);
        result.setIngredientStatuses(statuses);

        result.setMessage(
                allAvailable
                        ? "Budget pack stock available"
                        : "Insufficient stock in budget pack");

        return result;
    }

    @Transactional
    public void deductBudgetPackStock(
            Integer budgetPackId,
            Double orderQty,
            Integer userId) {

        ItemTemplateDTO template =
                itemTemplateRepository.getTemplateByItemId(
                        budgetPackId);

        if (template == null) {
            throw new IllegalStateException(
                    "Budget pack template not found : "
                            + budgetPackId);
        }

        for (IngredientDTO ingredient :
                template.getIngredients()) {

            double qtyToDeduct =
                    ingredient.getQuantity() * orderQty;

            deductItemStockForOrder(
                    ingredient.getSubItemId(),
                    ingredient.getSubItemName(),
                    qtyToDeduct,
                    userId
            );
        }
    }
}