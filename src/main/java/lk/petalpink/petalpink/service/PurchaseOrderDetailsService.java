package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.PurchaseOrderDetailsDTO;
import lk.petalpink.petalpink.repository.PurchaseOrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderDetailsService {

    @Autowired
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    public String createPurchaseOrderDetails(PurchaseOrderDetailsDTO dto) {
        int rows = purchaseOrderDetailsRepository.save(dto);
        return rows > 0 ? "Purchase order details created successfully" : "Failed to create purchase order details";
    }

    public List<PurchaseOrderDetailsDTO> getAllPurchaseOrderDetails() {
        return purchaseOrderDetailsRepository.findAll();
    }

    public List<PurchaseOrderDetailsDTO> getDetailsByPoId(int poId) {
        return purchaseOrderDetailsRepository.findByPoId(poId);
    }

    public String updatePurchaseOrderDetails(PurchaseOrderDetailsDTO dto) {
        int rows = purchaseOrderDetailsRepository.update(dto);
        return rows > 0 ? "Purchase order details updated successfully" : "Purchase order details not found or update failed";
    }

    public String deletePurchaseOrderDetails(int poDetailsId) {
        int rows = purchaseOrderDetailsRepository.delete(poDetailsId);
        return rows > 0 ? "Purchase order details deleted successfully" : "Purchase order details not found";
    }
}