package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.StockCategoryDTO;
import lk.petalpink.petalpink.service.StockCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-location")
@CrossOrigin(origins = "http://localhost:5173")
public class StockCategoryController {

    @Autowired
    private StockCategoryService stockCategoryService;

    @PostMapping
    public String createStockCategory(@RequestBody StockCategoryDTO dto) {
        return stockCategoryService.createStockCategory(dto);
    }

    @GetMapping
    public List<StockCategoryDTO> getAllStockCategories() {
        return stockCategoryService.getAllStockCategories();
    }

    @PutMapping
    public String updateStockCategory(@RequestBody StockCategoryDTO dto) {
        return stockCategoryService.updateStockCategory(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteStockCategory(@PathVariable int id) {
        return stockCategoryService.deleteStockCategory(id);
    }
}