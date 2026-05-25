package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemDTO dto) {
        Long newId = itemService.createItem(dto);
        if (newId == null || newId <= 0) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create item"));
        }
        return ResponseEntity.ok(Map.of("itemId", newId));
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

    @GetMapping("/grn")
    public List<ItemDTO> getAllGRNItems() {
        return itemService.getAllGRNItems();
    }

    @GetMapping("/courier-bags")
    public ResponseEntity<?> getCourierBagItems() {
        List<ItemDTO> items = itemService.getCourierBagItems();
        return ResponseEntity.ok(items);
    }
}