package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.dto.OrderDTO;
import lk.petalpink.petalpink.service.DeliveryOrderService;
import lk.petalpink.petalpink.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class DeliveryOrderController {

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/delivery-orders")
    public List<DeliveryOrderDTO> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return deliveryOrderService.getByDateRange(startDate, endDate);
    }

    @GetMapping("/delivery-orders/{deliveryId}/items")
    public List<ItemDTO> getOrderItemsByDeliveryId(@PathVariable Integer deliveryId) {
        return deliveryOrderService.getOrderItemsByDeliveryId(deliveryId);
    }

    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<String> updateDeliveryStatus(
            @PathVariable Integer deliveryId,
            @RequestParam Integer statusId) {
        deliveryOrderService.updateDeliveryStatus(deliveryId, statusId);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/{deliveryId}/remark")
    public ResponseEntity<String> getRemarkByDeliveryId(@PathVariable Integer deliveryId) {
        String remark = deliveryOrderService.getRemarkByDeliveryId(deliveryId);
        return ResponseEntity.ok(remark);
    }

    @PutMapping("/{deliveryId}/remark")
    public ResponseEntity<String> updateRemark(
            @PathVariable Integer deliveryId,
            @RequestBody String remark) {
        deliveryOrderService.updateRemark(deliveryId, remark);
        return ResponseEntity.ok("Remark updated successfully");
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderDTO>> getOrdersWithCustomers() {
        return ResponseEntity.ok(orderService.getOrdersWithCustomers());
    }

    @PostMapping("/{deliveryId}/generate-tracking")
    public ResponseEntity<String> generateTracking(@PathVariable Integer deliveryId) {
        try {
            String trackingCode = deliveryOrderService.generateAndAssignTracking(deliveryId);
            return ResponseEntity.ok(trackingCode);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to generate tracking: " + e.getMessage());
        }
    }
}