package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.PurchaseOrderDetailsDTO;
import lk.petalpink.petalpink.service.PurchaseOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-order-details")
@CrossOrigin(origins = "http://localhost:5173")
public class PurchaseOrderDetailsController {

    @Autowired
    private PurchaseOrderDetailsService purchaseOrderDetailsService;

    @PostMapping
    public String createPurchaseOrderDetails(@RequestBody PurchaseOrderDetailsDTO dto) {
        return purchaseOrderDetailsService.createPurchaseOrderDetails(dto);
    }

    @GetMapping
    public List<PurchaseOrderDetailsDTO> getAllPurchaseOrderDetails() {
        return purchaseOrderDetailsService.getAllPurchaseOrderDetails();
    }

    @GetMapping("/by-po/{poId}")
    public List<PurchaseOrderDetailsDTO> getDetailsByPoId(@PathVariable int poId) {
        return purchaseOrderDetailsService.getDetailsByPoId(poId);
    }

    @PutMapping
    public String updatePurchaseOrderDetails(@RequestBody PurchaseOrderDetailsDTO dto) {
        return purchaseOrderDetailsService.updatePurchaseOrderDetails(dto);
    }

    @DeleteMapping("/{id}")
    public String deletePurchaseOrderDetails(@PathVariable int id) {
        return purchaseOrderDetailsService.deletePurchaseOrderDetails(id);
    }
}