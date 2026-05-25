package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.StockCheckRequestDTO;
import lk.petalpink.petalpink.dto.StockCheckResultDTO;
import lk.petalpink.petalpink.service.OrderStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-check")
@CrossOrigin(origins = "http://localhost:5173")
public class StockCheckController {

    @Autowired
    private OrderStockService orderStockService;

    /**
     * POST /api/stock-check/item
     * Check a single item before adding to cart.
     * Body: { "itemId": 5, "quantity": 2.0 }
     */
    @PostMapping("/item")
    public ResponseEntity<StockCheckResultDTO> checkSingleItem(
            @RequestBody StockCheckRequestDTO req) {

        StockCheckResultDTO result = orderStockService.checkStockForItem(
                req.getItemId(), req.getQuantity());
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/stock-check/cart
     * Batch check for all items in the cart before placing order.
     * Body: [ { "itemId": 5, "quantity": 2.0 }, { "itemId": 8, "quantity": 1.0 } ]
     */
    @PostMapping("/cart")
    public ResponseEntity<List<StockCheckResultDTO>> checkCart(
            @RequestBody List<StockCheckRequestDTO> cartItems) {

        List<StockCheckResultDTO> results = orderStockService.checkStockForCart(cartItems);
        return ResponseEntity.ok(results);
    }
}