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
    private ItemTemplateRepository itemTemplateRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BatchProfileRepository batchProfileRepository;

    @Autowired
    private StockDetailsRepository stockDetailsRepository;

    // ─── 1. CHECK STOCK BEFORE ADDING TO CART ────────────────────────────────

    public StockCheckResultDTO checkStockForItem(Integer itemId, Double orderQuantity) {
        StockCheckResultDTO result = new StockCheckResultDTO();
        List<IngredientStockStatusDTO> statuses = new ArrayList<>();

        ItemTemplateDTO template = itemTemplateRepository.getTemplateByItemId(itemId);

        if (template == null || template.getIngredients() == null || template.getIngredients().isEmpty()) {
            result.setAvailable(true);
            result.setMessage("No ingredient template found — stock check skipped");
            result.setIngredientStatuses(statuses);
            return result;
        }

        boolean allSufficient = true;

        for (IngredientDTO ingredient : template.getIngredients()) {
            double requiredQty = ingredient.getQuantity() * orderQuantity;

            List<BatchProfileDTO> batches = batchProfileRepository
                    .findActiveBatchesFIFO(ingredient.getSubItemId());

            double availableQty = batches.stream()
                    .mapToDouble(b -> b.getPlusQty() != null ? b.getPlusQty() : 0.0)
                    .sum();

            boolean sufficient = availableQty >= requiredQty;
            if (!sufficient) allSufficient = false;

            IngredientStockStatusDTO status = new IngredientStockStatusDTO();
            status.setSubItemId(ingredient.getSubItemId());
            status.setSubItemName(ingredient.getSubItemName());
            status.setRequiredQty(requiredQty);
            status.setAvailableQty(availableQty);
            status.setSufficient(sufficient);
            statuses.add(status);
        }

        result.setAvailable(allSufficient);
        result.setIngredientStatuses(statuses);
        result.setMessage(allSufficient
                ? "Stock available"
                : "Insufficient stock for one or more ingredients");

        return result;
    }

    public List<StockCheckResultDTO> checkStockForCart(List<StockCheckRequestDTO> cartItems) {
        List<StockCheckResultDTO> results = new ArrayList<>();
        for (StockCheckRequestDTO req : cartItems) {
            results.add(checkStockForItem(req.getItemId(), req.getQuantity()));
        }
        return results;
    }

    // ─── 2. DEDUCT STOCK ON PLACE ORDER (FIFO) ───────────────────────────────

    @Transactional
    public void deductStockForOrder(Integer itemId, Double orderQuantity,
                                    Integer orderId, Integer userId) {

        ItemTemplateDTO template = itemTemplateRepository.getTemplateByItemId(itemId);

        if (template == null || template.getIngredients() == null
                || template.getIngredients().isEmpty()) {
            return;
        }

        for (IngredientDTO ingredient : template.getIngredients()) {
            double totalRequired = ingredient.getQuantity() * orderQuantity;

            // ✅ Fetch master stock once per ingredient to reuse stockId
            StockDTO master = stockRepository.findByItemId(ingredient.getSubItemId());
            Integer stockId = (master != null) ? master.getStockId() : null;

            List<BatchProfileDTO> batches = batchProfileRepository
                    .findActiveBatchesFIFO(ingredient.getSubItemId());

            if (batches == null || batches.isEmpty()) {
                // ── FALLBACK: No batch profile — use master stock directly ──
                deductFromMasterDirectly(ingredient, totalRequired, stockId, userId);
                continue;
            }

            // ── FIFO batch deduction ──
            double remaining = totalRequired;

            for (BatchProfileDTO batch : batches) {
                if (remaining <= 0) break;

                double batchAvailable = batch.getPlusQty() != null ? batch.getPlusQty() : 0.0;
                if (batchAvailable <= 0) continue;

                double consumeFromBatch = Math.min(batchAvailable, remaining);

                // ✅ stockId added as first argument
                stockDetailsRepository.insertSaleDetail(
                        stockId,
                        batch.getRegId(),
                        STOCK_ADJ_TYPE_SALE,
                        ingredient.getSubItemName(),
                        batch.getCostPrice(),
                        batch.getCostPrice(),
                        consumeFromBatch,
                        batch.getUnitType(),
                        userId
                );

                stockRepository.updateMasterQty(ingredient.getSubItemId(), -consumeFromBatch);
                remaining -= consumeFromBatch;
            }

            // After walking all batches, check if fully covered
            if (remaining > 0.0001) {
                double masterQty = (master != null && master.getQty() != null) ? master.getQty() : 0.0;

                if (masterQty < remaining) {
                    throw new IllegalStateException(
                            "Insufficient stock for ingredient: " + ingredient.getSubItemName()
                                    + " | Still needed: " + remaining
                                    + " | Master stock available: " + masterQty
                    );
                }

                // ✅ stockId added, batch_reg_id = null (no batch covers this remainder)
                stockDetailsRepository.insertSaleDetail(
                        stockId,
                        null,
                        STOCK_ADJ_TYPE_SALE,
                        ingredient.getSubItemName(),
                        null,
                        null,
                        remaining,
                        null,
                        userId
                );

                stockRepository.updateMasterQty(ingredient.getSubItemId(), -remaining);
            }
        }
    }

    // ── Helper: no batch profile exists — deduct straight from master ─────────
    private void deductFromMasterDirectly(IngredientDTO ingredient,
                                          double totalRequired,
                                          Integer stockId,       // ✅ NEW parameter
                                          Integer userId) {
        StockDTO master = stockRepository.findByItemId(ingredient.getSubItemId());
        double available = (master != null && master.getQty() != null) ? master.getQty() : 0.0;

        if (available < totalRequired) {
            throw new IllegalStateException(
                    "Insufficient stock for ingredient: " + ingredient.getSubItemName()
                            + " | Required: " + totalRequired
                            + " | Available: " + available
            );
        }

        // ✅ stockId added as first argument
        stockDetailsRepository.insertSaleDetail(
                stockId,
                null,
                STOCK_ADJ_TYPE_SALE,
                ingredient.getSubItemName(),
                null,
                null,
                totalRequired,
                null,
                userId
        );

        stockRepository.updateMasterQty(ingredient.getSubItemId(), -totalRequired);
    }

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
                    "Insufficient courier bag stock: " + courierBagName
                            + " | Available: " + available
            );
        }

        // Insert stock detail (minus 1 bag)
        stockDetailsRepository.insertSaleDetail(
                master.getStockId(),   // stockId
                null,                  // batchRegId (no batch for courier bags)
                STOCK_ADJ_TYPE_SALE,
                courierBagName != null ? courierBagName : "Courier Bag",
                null,                  // costPrice
                null,                  // lastGrnPrice
                1.0,                   // minusQty — always 1 per order
                master.getUnitType(),  // stockUnitType
                userId
        );

        // Deduct 1 from master stock
        stockRepository.updateMasterQty(courierBagId, -1.0);
    }
}