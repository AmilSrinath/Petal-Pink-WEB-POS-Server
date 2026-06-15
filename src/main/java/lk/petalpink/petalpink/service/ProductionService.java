package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.*;
import lk.petalpink.petalpink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionService {

    @Autowired private ProductionRepository    productionRepository;
    @Autowired private ItemRepository          itemRepository;
    @Autowired private ItemTemplateRepository  itemTemplateRepository;
    @Autowired private StockRepository         stockRepository;
    @Autowired private StockDetailsRepository  stockDetailsRepository;

    // ── stock_adj_type_id values (mirror your stock_adj_type_tb rows) ─────────
    private static final int ADJ_TYPE_PRODUCTION_IN  = 8; // e.g. "Production - Add"
    private static final int ADJ_TYPE_PRODUCTION_OUT = 9; // e.g. "Production - Reduce"
    // Adjust the IDs above to match what is actually in your stock_adj_type_tb.

    // ─────────────────────────────────────────────────────────────────────────
    //  CREATE PRODUCTION  (FIFO ingredient consumption)
    //
    //  Steps:
    //    1. Generate ref-no, save production record
    //    2. Fetch item-template (ingredients) for the produced item
    //    3. For every ingredient → consume batches in FIFO order
    //         - query pos_inv_stock_details_tb for plus_qty rows ordered by
    //           created_date ASC (oldest batch first)
    //         - walk through batches, draining each until totalRequired is met
    //         - each consumed slice becomes one minus-qty detail row that
    //           carries the correct batch_reg_id
    //         - reduce master stock qty
    //    4. Add produced item → upsert pos_inv_stock_tb (qty + produced_qty)
    //                         → insert pos_inv_stock_details_tb (plus_qty row)
    //           The produced item's detail row uses a newly-created batch_reg
    //           so downstream sales/adjustments can also track it by FIFO.
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public String createProduction(ProductionDTO dto) {

        // ── 1. Resolve item name if not supplied ──────────────────────────────
        if (dto.getItemName() == null || dto.getItemName().isBlank()) {
            dto.setItemName(itemRepository.findItemNameById(dto.getItemId()));
        }

        // ── 2. Generate reference number & persist production record ──────────
        String refNo = productionRepository.nextRefNo();
        dto.setRefNo(refNo);
        int productionId = productionRepository.saveAndGetId(dto);

        // ── 3. Fetch ingredient template ──────────────────────────────────────
        ItemTemplateDTO template = itemTemplateRepository.getTemplateByItemId(dto.getItemId());
        if (template == null || template.getIngredients().isEmpty()) {
            throw new IllegalStateException(
                    "No item template found for item_id=" + dto.getItemId() +
                            ". Please configure ingredients first.");
        }

        double producedQty = dto.getQty();
        List<StockDetailsDTO> detailsToSave = new ArrayList<>();

        // ── 4. Reduce each ingredient using FIFO ──────────────────────────────
        for (IngredientDTO ingredient : template.getIngredients()) {
            int    subItemId       = ingredient.getSubItemId();
            double requiredPerUnit = ingredient.getQuantity();
            double totalRequired   = requiredPerUnit * producedQty;

            // 4a. Validate master stock
            StockDTO subStock = stockRepository.findByItemId(subItemId);
            if (subStock == null) {
                throw new IllegalStateException(
                        "Ingredient item_id=" + subItemId + " not found in stock.");
            }
            if (subStock.getQty() < totalRequired) {
                throw new IllegalStateException(
                        "Insufficient stock for ingredient '" + ingredient.getSubItemName() +
                                "'. Available=" + subStock.getQty() + ", Required=" + totalRequired);
            }

            // 4b. FIFO: fetch all available (un-exhausted) plus-qty batches,
            //     oldest created_date first.
            //     A batch's "available qty" = SUM(plus_qty) - SUM(minus_qty)
            //     for that batch_reg_id + stock_id combination.
            //     We fetch individual plus rows so we can drain them one by one.
            List<FifoBatchDTO> fifoBatches =
                    stockDetailsRepository.findFifoBatchesForStock(subStock.getStockId());

            double remaining = totalRequired;

            for (FifoBatchDTO batch : fifoBatches) {
                if (remaining <= 0) break;

                double available = batch.getAvailableQty();
                if (available <= 0) continue;

                double consume = Math.min(available, remaining);
                remaining -= consume;

                // One OUT detail row per batch slice – carrying the correct batch_reg_id
                StockDetailsDTO outDetail = new StockDetailsDTO();
                outDetail.setStockLocationId(1);
                outDetail.setBatchRegId(batch.getBatchRegId());   // ← FIFO batch
                outDetail.setStockAdjTypeId(ADJ_TYPE_PRODUCTION_OUT);
                outDetail.setStockId(subStock.getStockId());
                outDetail.setStockName(ingredient.getSubItemName());
                outDetail.setCostPrice(batch.getCostPrice());
                outDetail.setLastGrnPrice(batch.getLastGrnPrice());
                outDetail.setPlusQty(0.0);
                outDetail.setMinusQty(consume);
                outDetail.setIsInitQty(0);
                outDetail.setStatus(1);
                outDetail.setVisible(1);
                outDetail.setCreatedDate(dto.getManDate());
                outDetail.setUserId(dto.getUserId());
                detailsToSave.add(outDetail);
            }

            if (remaining > 0.0001) {   // floating-point guard
                throw new IllegalStateException(
                        "FIFO exhausted before fulfilling full requirement for '" +
                                ingredient.getSubItemName() + "'. Shortfall=" + remaining);
            }

            // 4c. Reduce master stock
            stockRepository.updateMasterQty(subItemId, -totalRequired);
        }

        // ── 5. Add produced item to stock ─────────────────────────────────────
        StockDTO producedStock = stockRepository.findByItemId(dto.getItemId());
        int producedStockId;

        if (producedStock != null) {
            stockRepository.updateMasterQty(dto.getItemId(), producedQty);
            producedStockId = producedStock.getStockId();
        } else {
            producedStockId = stockRepository.insertAndGetId(
                    dto.getItemId(), dto.getItemName(), (int) producedQty);
        }

        // 5a. Create a new batch_reg for the produced quantity so FIFO can
        //     track it in future sales / adjustments.
        int producedBatchRegId = stockDetailsRepository.createBatchReg(dto.getUserId());

        // 5b. Stock details row – PLUS (production output)
        StockDetailsDTO inDetail = new StockDetailsDTO();
        inDetail.setStockLocationId(1);
        inDetail.setBatchRegId(producedBatchRegId);        // ← new batch for produced item
        inDetail.setStockAdjTypeId(ADJ_TYPE_PRODUCTION_IN);
        inDetail.setStockId(producedStockId);
        inDetail.setStockName(dto.getItemName());
        inDetail.setCostPrice(0.0);
        inDetail.setLastGrnPrice(0.0);
        inDetail.setPlusQty(producedQty);
        inDetail.setMinusQty(0.0);
        inDetail.setIsInitQty(0);
        inDetail.setStatus(1);
        inDetail.setVisible(1);
        inDetail.setCreatedDate(dto.getManDate());
        inDetail.setUserId(dto.getUserId());
        detailsToSave.add(inDetail);

        // ── 6. Batch-save all stock detail rows ───────────────────────────────
        stockDetailsRepository.saveAll(detailsToSave);

        return "Production #" + productionId + " (" + refNo + ") created successfully. " +
                "Produced qty=" + producedQty + " of '" + dto.getItemName() + "'.";
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  READ
    // ─────────────────────────────────────────────────────────────────────────

    public List<ProductionDTO> getAllProductions() {
        return productionRepository.findAll();
    }

    public ProductionDTO getProductionById(int productionId) {
        return productionRepository.findById(productionId);
    }
}