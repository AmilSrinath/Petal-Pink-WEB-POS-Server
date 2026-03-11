package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public String createItem(ItemDTO dto) {
        int rows = itemRepository.save(dto);
        return rows > 0 ? "Item created successfully" : "Failed to create item";
    }

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll();
    }

    public String updateItem(ItemDTO dto) {
        int rows = itemRepository.update(dto);
        return rows > 0 ? "Item updated successfully" : "Item not found or update failed";
    }

    public String deleteItem(int itemId) {
        int rows = itemRepository.softDelete(itemId);
        return rows > 0 ? "Item deleted successfully" : "Item not found";
    }
}