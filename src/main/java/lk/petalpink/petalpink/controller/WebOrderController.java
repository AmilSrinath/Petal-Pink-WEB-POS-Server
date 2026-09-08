package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/order")
@CrossOrigin(origins = "*")
public class WebOrderController {

    @Autowired
    private WebOrderService webOrderService;

    /**
     * POST /api/website/order/place
     * Frontend Checkout page calls this to save the order.
     * Returns { "orderRef": "PP-843021" }
     */
    @PostMapping("/place")
    public ResponseEntity<Map<String, String>> placeOrder(
            @RequestBody WebsiteSaveOrderRequestDTO req) {
        String orderRef = webOrderService.placeOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("orderRef", orderRef));
    }

    /**
     * GET /api/website/order/all
     * Admin panel: list all web orders (header only).
     */
    @GetMapping("/all")
    public ResponseEntity<List<WebsiteOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(webOrderService.getAllOrders());
    }

    /**
     * GET /api/website/order/{orderRef}
     * Admin panel: full detail for one order.
     */
    @GetMapping("/details/{orderRef}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderRef) {
        WebsiteOrderDetailsResponseDTO detail = webOrderService.getOrderDetails(orderRef);
        if (detail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found: " + orderRef));
        }
        return ResponseEntity.ok(detail);
    }

    /**
     * GET /api/website/order/today-sales
     * Website Dashboard KPI card.
     */
    @GetMapping("/today-sales")
    public ResponseEntity<Map<String, Object>> getTodaySales() {
        return ResponseEntity.ok(Map.of("todaySales", webOrderService.getTodaySales()));
    }

    /**
     * GET /api/website/order/year-sales
     * Website Dashboard bar chart — 12 monthly totals for the current year.
     */
    @GetMapping("/year-sales")
    public ResponseEntity<Map<String, Object>> getYearSales() {
        return ResponseEntity.ok(Map.of("monthlySales", webOrderService.getYearSales()));
    }

    /**
     * PUT /api/website/order/status/{orderId}
     * Manage Web Orders page: update order status.
     */
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Map<String, String>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, String> body) {
        webOrderService.updateOrderStatus(orderId, body.get("order_status"));
        return ResponseEntity.ok(Map.of("message", "Order status updated successfully."));
    }

    /**
     * PUT /api/website/order/tracking/{orderId}
     * Manage Web Orders page: update courier tracking number.
     */
    @PutMapping("/tracking/{orderId}")
    public ResponseEntity<Map<String, String>> updateTrackingNumber(
            @PathVariable String orderId,
            @RequestBody Map<String, String> body) {
        webOrderService.updateTrackingNumber(orderId, body.get("tracking_number"));
        return ResponseEntity.ok(Map.of("message", "Tracking number updated successfully."));
    }
}
