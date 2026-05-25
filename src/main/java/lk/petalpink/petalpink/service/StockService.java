package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.StockDTO;
import lk.petalpink.petalpink.dto.StockDetailsDTO;
import lk.petalpink.petalpink.dto.StockInitDTO;
import lk.petalpink.petalpink.dto.StockTransactionDTO;
import lk.petalpink.petalpink.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    /**
     * Add stock (e.g. GRN received).
     * quantityChange must be positive.
     */
//    @Transactional
//    public String addStock(StockTransactionDTO dto) {
//        if (dto.getQuantityChange() == null || dto.getQuantityChange() <= 0) {
//            return "Quantity must be a positive value for adding stock";
//        }
//        return processTransaction(dto, dto.getQuantityChange());
//    }

    /**
     * Reduce stock (e.g. sale, damage write-off).
     * quantityChange must be positive — the service makes it negative internally.
     */
//    @Transactional
//    public String reduceStock(StockTransactionDTO dto) {
//        if (dto.getQuantityChange() == null || dto.getQuantityChange() <= 0) {
//            return "Quantity must be a positive value for reducing stock";
//        }
//
//        // Guard: prevent negative stock
//        StockDTO master = stockRepository.findByItemId(dto.getItemId());
//        if (master == null) {
//            return "Item not found in stock";
//        }
//        if (master.getQty() < dto.getQuantityChange()) {
//            return "Insufficient stock. Available: " + master.getQty();
//        }
//
//        return processTransaction(dto, -dto.getQuantityChange());
//    }
//
//    /**
//     * Core logic: update master qty + insert detail record.
//     */
//    private String processTransaction(StockTransactionDTO dto, int signedQty) {
//        StockDTO master = stockRepository.findByItemId(dto.getItemId());
//        int stockId;
//
//        if (master == null) {
//            // First time this item enters stock
//            stockId = stockRepository.insertMaster(dto.getItemId(), dto.getItemName(), signedQty);
//        } else {
//            stockId = master.getStockId();
//            stockRepository.updateMasterQty(dto.getItemId(), signedQty);
//        }
//
//        // Build and insert the detail/audit record
//        StockDetailsDTO detail = new StockDetailsDTO();
//        detail.setStockId(stockId);
//        detail.setGrnId(dto.getGrnId());
//        detail.setMainItemCategoryId(dto.getMainItemCategoryId());
//        detail.setSubItemCategoryId(dto.getSubItemCategoryId());
//        detail.setItemId(dto.getItemId());
//        detail.setItemBarCode(dto.getItemBarCode());
//        detail.setStockCategoryId(dto.getStockCategoryId());
//        detail.setStockName(dto.getStockName());
//        detail.setUnitTypeId(dto.getUnitTypeId());
//        detail.setCostPrice(dto.getCostPrice());
//        detail.setLastGrnPrice(dto.getLastGrnPrice());
//        detail.setQuantity(signedQty);   // signed: + for in, - for out
//        detail.setStatus(1);
//        detail.setUserId(dto.getUserId());
//        detail.setVisible(dto.getVisible());
//
//        int detailRows = stockRepository.insertDetail(detail);
//        return detailRows > 0 ? "Stock transaction recorded successfully" : "Failed to record detail";
//    }

    public List<StockDTO> getAllMasterStocks() {
        return stockRepository.findAllMaster();
    }

    public List<StockDetailsDTO> getDetailsByStockId(int stockId) {
        return stockRepository.findDetailsByStockId(stockId);
    }

    public List<StockDetailsDTO> getAllDetails() {
        return stockRepository.findAllDetails();
    }

    @Transactional
    public void initializeStock(StockInitDTO dto) {
        StockDTO existing = stockRepository.findByItemId(dto.getItemId());
        if (existing == null) {
            // First time — insert with qty = 0
            stockRepository.insertMaster(
                    dto.getItemId(),
                    dto.getItemName(),
                    0.0,
                    dto.getUnitType(),
                    dto.getIsLowStockAlert(),
                    dto.getLowStockAlert()
            );
        } else {
            // Already exists — update unit_type + alert fields only, leave qty untouched
            stockRepository.updateAlertAndUnitType(
                    dto.getItemId(),
                    dto.getUnitType(),
                    dto.getIsLowStockAlert(),
                    dto.getLowStockAlert()
            );
        }
    }
}