package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.*;
import lk.petalpink.petalpink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrnService {

    @Autowired
    private GrnRepository grnRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StockDetailsRepository stockDetailsRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BatchRegRepository batchRegRepository;

    @Autowired
    private BatchProfileRepository batchProfileRepository;

    public String createGrn(GrnDTO dto) {
        int rows = grnRepository.save(dto);
        return rows > 0 ? "GRN created successfully" : "Failed to create GRN";
    }

    public List<GrnDTO> getAllGrns() {
        return grnRepository.findAll();
    }

    public String updateGrn(GrnDTO dto) {
        int rows = grnRepository.update(dto);
        return rows > 0 ? "GRN updated successfully" : "GRN not found or update failed";
    }

    public String deleteGrn(int grnId) {
        int rows = grnRepository.softDelete(grnId);
        return rows > 0 ? "GRN deleted successfully" : "GRN not found";
    }

    @Transactional
    public String createGrnTransaction(GrnRequestDTO request) {
        GrnDTO grn = request.getGrn();
        List<BatchProfileDTO> batchItems = request.getBatchItems();

        // Step 1 — save GRN header
        int grnId = grnRepository.saveAndGetId(grn);

        List<StockDetailsDTO> stockDetailsList = new ArrayList<>();

        // Step 2a — register batch (once per GRN)
        BatchRegDTO batchReg = new BatchRegDTO();
        batchReg.setBatchRegPrefix("BA");
        batchReg.setIsActive(1);
        batchReg.setUserId(grn.getUserId());
        batchReg.setRemark("");

        int regId = batchRegRepository.saveAndGetId(batchReg);

        for (BatchProfileDTO item : batchItems) {
            // 2b — save batch profile
            item.setGrnId(grnId);
            item.setRegId(regId);
            batchProfileRepository.save(item);

            // 2c — fetch item name
            String stockName = itemRepository.findItemNameById(item.getItemId());

            // 2d — upsert stock master and capture stock_id ✅
            StockDTO existing = stockRepository.findByItemId(item.getItemId());

            int stockId;
            if (existing != null) {
                stockRepository.incrementQty(item.getItemId(), item.getPlusQty().intValue());
                stockId = existing.getStockId();   // ✅ get from existing row
            } else {
                stockId = stockRepository.insertAndGetId(   // ✅ return new stock_id
                        item.getItemId(),
                        stockName,
                        item.getPlusQty().intValue()
                );
            }

            // 2e — build stock detail row
            StockDetailsDTO stockDetail = new StockDetailsDTO();
            stockDetail.setStockLocationId(grn.getStockLocationId());
            stockDetail.setBatchRegId(regId);
            stockDetail.setStockAdjTypeId(1);
            stockDetail.setStockId(stockId);              // ✅ NEW
            stockDetail.setStockName(stockName);
            stockDetail.setCostPrice(item.getCostPrice());
            stockDetail.setLastGrnPrice(item.getCostPrice());
            stockDetail.setPlusQty(item.getPlusQty());
            stockDetail.setMinusQty(0.0);
            stockDetail.setIsInitQty(0);
            stockDetail.setStatus(1);
            stockDetail.setVisible(1);
            stockDetail.setCreatedDate(grn.getCreatedDate());
            stockDetail.setUserId(grn.getUserId());

            stockDetailsList.add(stockDetail);
        }

        // Step 3 — batch insert all stock details
        stockDetailsRepository.saveAll(stockDetailsList);

        return "GRN #" + grnId + " created with " + batchItems.size() + " batch(es) successfully";
    }
}