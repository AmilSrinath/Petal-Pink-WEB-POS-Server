package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.StockAdjTypeDTO;
import lk.petalpink.petalpink.service.StockAdjTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-adj-types")
@CrossOrigin(origins = "http://localhost:5173")
public class StockAdjTypeController {

    @Autowired
    private StockAdjTypeService stockAdjTypeService;

    @PostMapping
    public String createStockAdjType(@RequestBody StockAdjTypeDTO dto) {
        return stockAdjTypeService.createStockAdjType(dto);
    }

    @GetMapping
    public List<StockAdjTypeDTO> getAllStockAdjTypes() {
        return stockAdjTypeService.getAllStockAdjTypes();
    }

    @PutMapping
    public String updateStockAdjType(@RequestBody StockAdjTypeDTO dto) {
        return stockAdjTypeService.updateStockAdjType(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteStockAdjType(@PathVariable int id) {
        return stockAdjTypeService.deleteStockAdjType(id);
    }
}