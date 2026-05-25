package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.dto.StockInitDTO;
import lk.petalpink.petalpink.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StockService stockService;

    @Transactional
    public Long createItem(ItemDTO dto) {
        // 1. Save item and get the new item_id
        Long newItemId = itemRepository.save(dto);

        // 2. Auto-initialize stock record
        if (newItemId != null && newItemId > 0) {
            StockInitDTO stockInit = new StockInitDTO();
            stockInit.setItemId(newItemId.intValue());
            stockInit.setItemName(dto.getItemName());
            stockInit.setUnitType(dto.getUnitTypeId());          // FK to unit type table
            stockInit.setIsLowStockAlert(dto.getIsLowStockAlert());
            stockInit.setLowStockAlert(dto.getLowStockAlert());

            stockService.initializeStock(stockInit);
        }

        return newItemId;
    }

    @Transactional
    public String updateItem(ItemDTO dto) {
        int rows = itemRepository.update(dto);
        if (rows <= 0) return "Item not found or update failed";

        // Also sync stock alert fields and unit_type on edit
        StockInitDTO stockInit = new StockInitDTO();
        stockInit.setItemId(dto.getItemId().intValue());
        stockInit.setItemName(dto.getItemName());
        stockInit.setUnitType(dto.getUnitTypeId());
        stockInit.setIsLowStockAlert(dto.getIsLowStockAlert());
        stockInit.setLowStockAlert(dto.getLowStockAlert());

        stockService.initializeStock(stockInit);

        return "Item updated successfully";
    }

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll();
    }

    public String deleteItem(int itemId) {
        int rows = itemRepository.softDelete(itemId);
        return rows > 0 ? "Item deleted successfully" : "Item not found";
    }

    public List<ItemDTO> getAllGRNItems() {
        return itemRepository.findAllGRN();
    }

    @Autowired
    private ConfigService configService;

    public List<ItemDTO> getCourierBagItems() {
        int subCatId = configService.getCourierBagsSubcategory();
        return itemRepository.findBySubcategoryId(subCatId);
    }
}