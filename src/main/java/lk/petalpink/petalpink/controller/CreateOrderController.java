package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.UpdateOrderRequestDTO;
import lk.petalpink.petalpink.service.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class CreateOrderController {

    @Autowired
    private CreateOrderService createOrderService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createOrder(@RequestBody CreateOrderRequestDTO req) {
        Integer deliveryId = createOrderService.createOrder(req);
        return ResponseEntity.ok(deliveryId);
    }

    @PatchMapping("/{deliveryId}/order-code")
    public ResponseEntity<Void> updateOrderCode(
            @PathVariable Integer deliveryId,
            @RequestParam String orderCode) {
        createOrderService.updateOrderCode(deliveryId, orderCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateOrder(@RequestBody UpdateOrderRequestDTO req) {
        createOrderService.updateOrder(req);
        return ResponseEntity.ok("Order updated successfully");
    }
}