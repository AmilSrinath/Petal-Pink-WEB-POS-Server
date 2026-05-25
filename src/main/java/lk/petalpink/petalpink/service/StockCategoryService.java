package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.StockCategoryDTO;
import lk.petalpink.petalpink.repository.StockCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCategoryService {

    @Autowired
    private StockCategoryRepository stockCategoryRepository;

    public String createStockCategory(StockCategoryDTO dto) {
        int rows = stockCategoryRepository.save(dto);
        return rows > 0 ? "Stock category created successfully" : "Failed to create stock category";
    }

    public List<StockCategoryDTO> getAllStockCategories() {
        return stockCategoryRepository.findAll();
    }

    public String updateStockCategory(StockCategoryDTO dto) {
        int rows = stockCategoryRepository.update(dto);
        return rows > 0 ? "Stock category updated successfully" : "Stock category not found or update failed";
    }

    public String deleteStockCategory(int stockCategoryId) {
        int rows = stockCategoryRepository.softDelete(stockCategoryId);
        return rows > 0 ? "Stock category deleted successfully" : "Stock category not found";
    }
}