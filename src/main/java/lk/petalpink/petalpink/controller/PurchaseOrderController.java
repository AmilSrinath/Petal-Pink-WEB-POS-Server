package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.PurchaseOrderDTO;
import lk.petalpink.petalpink.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:5173")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping
    public String createPurchaseOrder(@RequestBody PurchaseOrderDTO dto) {
        return purchaseOrderService.createPurchaseOrder(dto);
    }

    @GetMapping
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @PutMapping
    public String updatePurchaseOrder(@RequestBody PurchaseOrderDTO dto) {
        return purchaseOrderService.updatePurchaseOrder(dto);
    }

    @DeleteMapping("/{id}")
    public String deletePurchaseOrder(@PathVariable int id) {
        return purchaseOrderService.deletePurchaseOrder(id);
    }
}