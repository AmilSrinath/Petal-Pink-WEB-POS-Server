package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public String createItem(@RequestBody ItemDTO dto) {
        return itemService.createItem(dto);
    }

    @GetMapping
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @PutMapping
    public String updateItem(@RequestBody ItemDTO dto) {
        return itemService.updateItem(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable int id) {
        return itemService.deleteItem(id);
    }
}