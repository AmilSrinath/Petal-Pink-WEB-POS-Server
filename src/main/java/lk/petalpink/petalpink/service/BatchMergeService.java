package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.*;
import lk.petalpink.petalpink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchMergeService {

    @Autowired
    private BatchProfileRepository batchProfileRepository;

    @Autowired
    private BatchRegRepository batchRegRepository;

    @Autowired
    private StockRepository stockRepository;

    // ── Query ────────────────────────────────────────────────────────────────

    public List<BatchProfileDTO> getActiveBatchesByItem(int itemId) {
        return batchProfileRepository.findActiveByItemId(itemId);
    }

    // ── Merge ────────────────────────────────────────────────────────────────

    @Transactional
    public String mergeBatches(BatchMergeRequestDTO request) {

        List<Integer> sourceIds = request.getSourceProfileIds();

        if (sourceIds == null || sourceIds.size() < 2) {
            return "At least 2 batch profiles are required to merge";
        }

        // Step 1 — load all source profiles
        List<BatchProfileDTO> sources = sourceIds.stream()
                .map(batchProfileRepository::findById)
                .collect(Collectors.toList());

        // Guard: none missing
        for (int i = 0; i < sources.size(); i++) {
            if (sources.get(i) == null) {
                return "Batch profile not found: id=" + sourceIds.get(i);
            }
        }

        // Step 2 — all must belong to the same item
        long distinctItems = sources.stream()
                .map(BatchProfileDTO::getItemId)
                .distinct()
                .count();

        if (distinctItems > 1) {
            return "Cannot merge batches from different items. All selected profiles must share the same item_id.";
        }

        // Guard: all must still be active
        boolean anyInactive = sources.stream()
                .anyMatch(p -> p.getIsActive() == null || p.getIsActive() == 0);
        if (anyInactive) {
            return "One or more selected batch profiles are already inactive";
        }

        int itemId = sources.get(0).getItemId();

        // Step 3 — sum qty, use first batch as price reference
        double mergedQty = sources.stream()
                .mapToDouble(p -> p.getPlusQty() != null ? p.getPlusQty() : 0.0)
                .sum();

        BatchProfileDTO ref = sources.get(0);

        // Step 4 — create new BatchReg
        BatchRegDTO newReg = new BatchRegDTO();
        newReg.setBatchRegPrefix("BA");
        newReg.setIsActive(1);
        newReg.setUserId(request.getUserId());
        newReg.setRemark(request.getRemark() != null
                ? request.getRemark()
                : "Merged from profiles: " + sourceIds);

        int newRegId = batchRegRepository.saveAndGetId(newReg);

        // Step 5 — create merged BatchProfile
        BatchProfileDTO merged = new BatchProfileDTO();
        merged.setRegId(newRegId);
        merged.setItemId(itemId);
        merged.setGrnId(request.getTargetGrnId());
        merged.setCostPrice(ref.getCostPrice());
        merged.setRetailPrice(ref.getRetailPrice());
        merged.setWholeSalePrice(ref.getWholeSalePrice());
        merged.setExpDate(ref.getExpDate());
        merged.setIsReleaseForSell(ref.getIsReleaseForSell());
        merged.setPoNo(ref.getPoNo());
        merged.setUnitType(ref.getUnitType());
        merged.setIsActive(1);
        merged.setUserId(request.getUserId());
        merged.setPlusQty(mergedQty);
        merged.setRemark(newReg.getRemark());

        batchProfileRepository.save(merged);

        // Step 6 — deactivate all source profiles
        for (Integer profileId : sourceIds) {
            batchProfileRepository.deactivate(profileId);
        }

        // Step 7 — recalculate master stock qty
//        stockRepository.recalculateQtyByItemId(itemId);

        return "Merged " + sourceIds.size() + " batches into new reg_id=" + newRegId
                + " with total qty=" + mergedQty;
    }
}