package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.PurchaseOrderDTO;
import lk.petalpink.petalpink.dto.PurchaseOrderDetailsDTO;
import lk.petalpink.petalpink.repository.PurchaseOrderDetailsRepository;
import lk.petalpink.petalpink.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    /**
     * Saves the purchase order header first, then inserts each detail line
     * using the auto-generated po_id. Rolls back everything on failure.
     */
    @Transactional
    public String createPurchaseOrder(PurchaseOrderDTO dto) {
        // 1. Insert header — get back the new po_id
        int generatedPoId = purchaseOrderRepository.save(dto);
        if (generatedPoId == -1) {
            return "Failed to create purchase order";
        }

        // 2. Insert each detail line with the real po_id
        List<PurchaseOrderDetailsDTO> details = dto.getDetails();
        if (details != null && !details.isEmpty()) {
            for (PurchaseOrderDetailsDTO detail : details) {
                detail.setPoId(generatedPoId);          // stamp the FK
                detail.setUserId(dto.getUserId());       // inherit user if not set
                int rows = purchaseOrderDetailsRepository.save(detail);
                if (rows == 0) {
                    // Triggers rollback for the whole transaction
                    throw new RuntimeException(
                            "Failed to save detail for item: " + detail.getItemName());
                }
            }
        }

        return "Purchase order created successfully";
    }

    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public String updatePurchaseOrder(PurchaseOrderDTO dto) {
        int rows = purchaseOrderRepository.update(dto);
        return rows > 0 ? "Purchase order updated successfully"
                : "Purchase order not found or update failed";
    }

    public String deletePurchaseOrder(int poId) {
        int rows = purchaseOrderRepository.softDelete(poId);
        return rows > 0 ? "Purchase order deleted successfully"
                : "Purchase order not found";
    }
}