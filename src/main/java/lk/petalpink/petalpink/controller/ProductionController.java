package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ProductionDTO;
import lk.petalpink.petalpink.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
@CrossOrigin(origins = "*")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    /**
     * POST /api/production
     *
     * Create a new production.  The service will:
     *   - auto-generate the ref_no
     *   - reduce ingredient stock (from item template)
     *   - add produced item stock
     *   - write stock_details rows for both directions
     *
     * Request body (JSON):
     * {
     *   "itemId"   : 12,
     *   "manDate"  : "2026-06-03",
     *   "expDate"  : "2026-09-03",   // optional
     *   "unitType" : 2,
     *   "qty"      : 50,
     *   "wastage"  : 2,              // optional, default 0
     *   "userId"   : 1
     * }
     */
    @PostMapping
    public ResponseEntity<String> createProduction(@RequestBody ProductionDTO dto) {
        try {
            String result = productionService.createProduction(dto);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/production
     * Return all active production records.
     */
    @GetMapping
    public List<ProductionDTO> getAllProductions() {
        return productionService.getAllProductions();
    }

    /**
     * GET /api/production/{id}
     * Return a single production record.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductionDTO> getById(@PathVariable int id) {
        ProductionDTO result = productionService.getProductionById(id);
        return result != null
                ? ResponseEntity.ok(result)
                : ResponseEntity.notFound().build();
    }
}