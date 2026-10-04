package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.dto.OrderDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.service.DeliveryOrderService;
import lk.petalpink.petalpink.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
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
            @RequestParam Integer statusId,
            @RequestParam(required = false) Integer userId) {
        try {
            deliveryOrderService.updateDeliveryStatus(deliveryId, statusId, userId);
            return ResponseEntity.ok("Status updated successfully");
        } catch (IllegalStateException e) {
            // e.g. courier bag out of stock — status change is rejected so the
            // order doesn't move to Despatch without a bag actually deducted
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    public ResponseEntity<String> generateTracking(
            @PathVariable Integer deliveryId,
            @RequestParam(required = false) Integer courierBagId,
            @RequestParam(required = false) String courierBagName) {
        try {
            String trackingCode = deliveryOrderService.generateAndAssignTracking(deliveryId, courierBagId, courierBagName);
            return ResponseEntity.ok(trackingCode);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to generate tracking: " + e.getMessage());
        }
    }

    // Moves a pending order to Wrapping (status_id = 3) and records the
    // courier bag chosen for it. Used when the tracking/order code is
    // entered manually instead of auto-generated (see generate-tracking).
    @PatchMapping("/{deliveryId}/wrapping")
    public ResponseEntity<String> moveToWrapping(
            @PathVariable Integer deliveryId,
            @RequestParam(required = false) Integer courierBagId,
            @RequestParam(required = false) String courierBagName) {
        try {
            deliveryOrderService.moveToWrapping(deliveryId, courierBagId, courierBagName);
            return ResponseEntity.ok("Order moved to Wrapping");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update order: " + e.getMessage());
        }
    }

    @GetMapping("/delivery-orders/{deliveryId}")
    public ResponseEntity<DeliveryOrderDTO> getByDeliveryId(@PathVariable Integer deliveryId) {
        DeliveryOrderDTO order = deliveryOrderService.getByDeliveryId(deliveryId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderDetailItemDTO>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetailItemDTO> items = deliveryOrderService.getOrderDetailsByOrderId(orderId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/delivery-orders/tracking/{orderCode}")
    public ResponseEntity<DeliveryOrderDTO> getByOrderCode(@PathVariable String orderCode) {
        try {
            DeliveryOrderDTO order = deliveryOrderService.getByOrderCode(orderCode);
            return ResponseEntity.ok(order);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/orders/items/{orderDetailId}/remark")
    public ResponseEntity<String> updateOrderItemRemark(
            @PathVariable Integer orderDetailId,
            @RequestBody(required = false) String remark) {
        deliveryOrderService.updateOrderItemRemark(orderDetailId, remark != null ? remark : "");
        return ResponseEntity.ok("Item remark updated successfully");
    }
}