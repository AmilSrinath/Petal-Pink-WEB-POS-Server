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

    public List<GrnItemDetailDTO> getGrnItems(int grnId) {
        return grnRepository.findGrnItems(grnId);
    }

    public String getNextInvoiceNo() {
        return grnRepository.getNextInvoiceNo();
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

    @Transactional
    public String updateGrnTransaction(GrnUpdateRequestDTO request) {
        if (request.getGrnId() == null) {
            throw new IllegalArgumentException("GRN ID is required for update");
        }

        // 1. Update GRN Header
        int updatedHeaderRows = grnRepository.updateGrnHeader(request);
        if (updatedHeaderRows == 0) {
            throw new RuntimeException("GRN not found with id: " + request.getGrnId());
        }

        Integer regId = grnRepository.findBatchRegIdByGrnId(request.getGrnId());
        if (regId == null && request.getItems() != null) {
            for (GrnItemUpdateDTO it : request.getItems()) {
                if (it.getRegId() != null && it.getRegId() > 0) {
                    regId = it.getRegId();
                    break;
                }
            }
        }

        if (regId == null) {
            BatchRegDTO batchReg = new BatchRegDTO();
            batchReg.setBatchRegPrefix("BA");
            batchReg.setIsActive(1);
            batchReg.setUserId(request.getUserId());
            batchReg.setRemark("GRN Update Reg");
            regId = batchRegRepository.saveAndGetId(batchReg);
        }

        // 2. Fetch existing items to track modifications and deletions
        List<GrnItemDetailDTO> existingItems = grnRepository.findGrnItems(request.getGrnId());
        List<Integer> updatedProfileIds = new ArrayList<>();

        if (request.getItems() != null) {
            for (GrnItemUpdateDTO item : request.getItems()) {
                String stockName = item.getItemName();
                if (stockName == null || stockName.isEmpty()) {
                    stockName = itemRepository.findItemNameById(item.getItemId());
                }

                StockDTO stockMaster = stockRepository.findByItemId(item.getItemId());
                int stockId;
                if (stockMaster != null) {
                    stockId = stockMaster.getStockId();
                } else {
                    stockId = stockRepository.insertAndGetId(
                            item.getItemId(),
                            stockName,
                            item.getQuantity() != null ? item.getQuantity().intValue() : 0
                    );
                }

                if (item.getProfileId() != null && item.getProfileId() > 0) {
                    // ── EXISTING ITEM UPDATE ──
                    updatedProfileIds.add(item.getProfileId());
                    grnRepository.updateBatchProfile(item);
                    if (item.getUnitType() != null && item.getUnitType() > 0) {
                        stockRepository.updateUnitType(item.getItemId(), item.getUnitType());
                    }

                    int itemRegId = (item.getRegId() != null && item.getRegId() > 0) ? item.getRegId() : regId;
                    Double currentPlusQty = grnRepository.findCurrentPlusQty(itemRegId, stockId);
                    double oldQty = currentPlusQty != null ? currentPlusQty : 0.0;
                    double newQty = item.getQuantity() != null ? item.getQuantity() : 0.0;
                    double diff = newQty - oldQty;

                    if (currentPlusQty != null) {
                        grnRepository.updateStockDetail(
                                itemRegId,
                                stockId,
                                newQty,
                                item.getCostPrice() != null ? item.getCostPrice() : 0.0,
                                request.getStockLocationId(),
                                request.getCreatedDate()
                        );
                    } else {
                        StockDetailsDTO sd = new StockDetailsDTO();
                        sd.setStockLocationId(request.getStockLocationId());
                        sd.setBatchRegId(itemRegId);
                        sd.setStockAdjTypeId(1);
                        sd.setStockId(stockId);
                        sd.setStockName(stockName);
                        sd.setCostPrice(item.getCostPrice() != null ? item.getCostPrice() : 0.0);
                        sd.setLastGrnPrice(item.getCostPrice() != null ? item.getCostPrice() : 0.0);
                        sd.setPlusQty(newQty);
                        sd.setMinusQty(0.0);
                        sd.setIsInitQty(0);
                        sd.setStatus(1);
                        sd.setVisible(1);
                        sd.setCreatedDate(request.getCreatedDate());
                        sd.setUserId(request.getUserId());
                        stockDetailsRepository.saveAll(List.of(sd));
                    }

                    if (diff != 0.0) {
                        stockRepository.updateMasterQty(item.getItemId(), diff);
                    }
                } else {
                    // ── NEW ITEM ADDED TO GRN ──
                    BatchProfileDTO bp = new BatchProfileDTO();
                    bp.setRegId(regId);
                    bp.setItemId(item.getItemId());
                    bp.setGrnId(request.getGrnId());
                    bp.setCostPrice(item.getCostPrice() != null ? item.getCostPrice() : 0.0);
                    bp.setRetailPrice(item.getRetailPrice() != null ? item.getRetailPrice() : 0.0);
                    bp.setWholeSalePrice(item.getWholeSalePrice() != null ? item.getWholeSalePrice() : 0.0);
                    bp.setExpDate(item.getExpDate());
                    bp.setIsReleaseForSell(item.getIsReleaseForSell() != null ? item.getIsReleaseForSell() : 1);
                    bp.setPoNo(item.getPoNo());
                    int uType = (item.getUnitType() != null && item.getUnitType() > 0) ? item.getUnitType() : 1;
                    bp.setUnitType(uType);
                    bp.setIsActive(1);
                    bp.setUserId(request.getUserId());
                    bp.setRemark(item.getRemark());
                    batchProfileRepository.save(bp);

                    stockRepository.updateUnitType(item.getItemId(), uType);

                    double addedQty = item.getQuantity() != null ? item.getQuantity() : 0.0;
                    stockRepository.incrementQty(item.getItemId(), addedQty);

                    StockDetailsDTO sd = new StockDetailsDTO();
                    sd.setStockLocationId(request.getStockLocationId());
                    sd.setBatchRegId(regId);
                    sd.setStockAdjTypeId(1);
                    sd.setStockId(stockId);
                    sd.setStockName(stockName);
                    sd.setCostPrice(item.getCostPrice() != null ? item.getCostPrice() : 0.0);
                    sd.setLastGrnPrice(item.getCostPrice() != null ? item.getCostPrice() : 0.0);
                    sd.setPlusQty(addedQty);
                    sd.setMinusQty(0.0);
                    sd.setIsInitQty(0);
                    sd.setStatus(1);
                    sd.setVisible(1);
                    sd.setCreatedDate(request.getCreatedDate());
                    sd.setUserId(request.getUserId());
                    stockDetailsRepository.saveAll(List.of(sd));
                }
            }
        }

        // 3. Handle removed items
        for (GrnItemDetailDTO existing : existingItems) {
            if (existing.getProfileId() != null && !updatedProfileIds.contains(existing.getProfileId())) {
                grnRepository.deactivateBatchProfile(existing.getProfileId());

                StockDTO sm = stockRepository.findByItemId(existing.getItemId());
                if (sm != null && existing.getRegId() != null) {
                    Double currentPlusQty = grnRepository.findCurrentPlusQty(existing.getRegId(), sm.getStockId());
                    double removedQty = currentPlusQty != null ? currentPlusQty : (existing.getQuantity() != null ? existing.getQuantity() : 0.0);
                    if (removedQty > 0) {
                        stockRepository.updateMasterQty(existing.getItemId(), -removedQty);
                        grnRepository.removeStockDetail(existing.getRegId(), sm.getStockId());
                    }
                }
            }
        }

        return "GRN #" + request.getGrnId() + " updated successfully";
    }
}