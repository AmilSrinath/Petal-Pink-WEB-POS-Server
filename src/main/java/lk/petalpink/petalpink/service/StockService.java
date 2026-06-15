package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.StockAdjustmentDTO;
import lk.petalpink.petalpink.dto.StockDTO;
import lk.petalpink.petalpink.dto.StockDetailsDTO;
import lk.petalpink.petalpink.dto.StockInitDTO;
import lk.petalpink.petalpink.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // ─────────────────────────────────────────────────────────────────────────
    //  ADJUSTMENT — ADD
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public String addStock(StockAdjustmentDTO dto) {
        if (dto.getItemId() == null || dto.getQty() == null || dto.getQty() <= 0) {
            return "Invalid request: itemId and a positive qty are required";
        }

        StockDTO master = stockRepository.findByItemId(dto.getItemId());
        if (master == null) {
            return "Stock record not found for itemId: " + dto.getItemId();
        }

        StockDetailsDTO detail = buildDetail(dto, master.getStockId());
        detail.setPlusQty(dto.getQty());
        detail.setMinusQty(0.0);

        stockRepository.insertAdjustmentDetail(detail);
        stockRepository.updateMasterQty(dto.getItemId(), dto.getQty());

        return "Stock added successfully";
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ADJUSTMENT — REDUCE
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public String reduceStock(StockAdjustmentDTO dto) {
        if (dto.getItemId() == null || dto.getQty() == null || dto.getQty() <= 0) {
            return "Invalid request: itemId and a positive qty are required";
        }

        StockDTO master = stockRepository.findByItemId(dto.getItemId());
        if (master == null) {
            return "Stock record not found for itemId: " + dto.getItemId();
        }

        if (master.getQty() < dto.getQty()) {
            return "Insufficient stock: available " + master.getQty()
                    + ", requested " + dto.getQty();
        }

        StockDetailsDTO detail = buildDetail(dto, master.getStockId());
        detail.setPlusQty(0.0);
        detail.setMinusQty(dto.getQty());

        stockRepository.insertAdjustmentDetail(detail);
        stockRepository.updateMasterQty(dto.getItemId(), -dto.getQty());  // negative to subtract

        return "Stock reduced successfully";
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  HELPER
    // ─────────────────────────────────────────────────────────────────────────

    private StockDetailsDTO buildDetail(StockAdjustmentDTO dto, Integer stockId) {
        StockDetailsDTO detail = new StockDetailsDTO();
        detail.setStockId(stockId);                 // resolved stock_id (PK) from master lookup
        detail.setStockName(dto.getStockName());
        detail.setStockAdjTypeId(dto.getStockAdjTypeId());
        detail.setCostPrice(dto.getCostPrice() != null ? dto.getCostPrice() : 0.0);
        detail.setLastGrnPrice(dto.getLastGrnPrice() != null ? dto.getLastGrnPrice() : 0.0);
        detail.setIsInitQty(0);
        detail.setStatus(1);
        detail.setVisible(1);
        detail.setUserId(dto.getUserId());
        detail.setCreatedDate(LocalDate.now());
        detail.setStockLocationId(1);
        detail.setBatchRegId(null);
        return detail;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  EXISTING METHODS (unchanged)
    // ─────────────────────────────────────────────────────────────────────────

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
            stockRepository.insertMaster(
                    dto.getItemId(), dto.getItemName(), 0.0,
                    dto.getUnitType(), dto.getIsLowStockAlert(),
                    dto.getLowStockAlert(), dto.getIsSellingItem()
            );
        } else {
            stockRepository.updateAlertAndUnitType(
                    dto.getItemId(), dto.getUnitType(),
                    dto.getIsLowStockAlert(), dto.getLowStockAlert(),
                    dto.getIsSellingItem()
            );
        }
    }
}