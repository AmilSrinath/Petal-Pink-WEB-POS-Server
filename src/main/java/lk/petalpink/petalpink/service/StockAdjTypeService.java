package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.StockAdjTypeDTO;
import lk.petalpink.petalpink.repository.StockAdjTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAdjTypeService {

    @Autowired
    private StockAdjTypeRepository stockAdjTypeRepository;

    public String createStockAdjType(StockAdjTypeDTO dto) {
        int rows = stockAdjTypeRepository.save(dto);
        return rows > 0 ? "Stock adjustment type created successfully" : "Failed to create stock adjustment type";
    }

    public List<StockAdjTypeDTO> getAllStockAdjTypes() {
        return stockAdjTypeRepository.findAll();
    }

    public String updateStockAdjType(StockAdjTypeDTO dto) {
        int rows = stockAdjTypeRepository.update(dto);
        return rows > 0 ? "Stock adjustment type updated successfully" : "Stock adjustment type not found or update failed";
    }

    public String deleteStockAdjType(int stockAdjTypeId) {
        int rows = stockAdjTypeRepository.softDelete(stockAdjTypeId);
        return rows > 0 ? "Stock adjustment type deleted successfully" : "Stock adjustment type not found";
    }
}