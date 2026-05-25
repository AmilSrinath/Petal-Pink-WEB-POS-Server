package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ItemTemplateDTO;
import lk.petalpink.petalpink.service.ItemTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/item-templates")
@CrossOrigin(origins = "*")
public class ItemTemplateController {

    @Autowired
    private ItemTemplateService itemTemplateService;

    // ─── POST /api/item-templates ─────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> saveTemplate(@RequestBody ItemTemplateDTO dto) {
        String result = itemTemplateService.saveTemplate(dto);
        if (result.startsWith("Template saved")) {
            return ResponseEntity.ok(Map.of("message", result));
        }
        return ResponseEntity.status(500).body(Map.of("error", result));
    }

    // ─── GET /api/item-templates/{itemId} ─────────────────────────────────────
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getTemplate(@PathVariable Integer itemId) {
        ItemTemplateDTO dto = itemTemplateService.getTemplateByItemId(itemId);
        if (dto == null) {
            return ResponseEntity.status(404).body(Map.of("error", "No template found for item ID: " + itemId));
        }
        return ResponseEntity.ok(dto);
    }

    // ─── PUT /api/item-templates/{itemId} ─────────────────────────────────────
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateTemplate(@PathVariable Integer itemId,
                                            @RequestBody ItemTemplateDTO dto) {
        String result = itemTemplateService.updateTemplate(itemId, dto);
        if (result.startsWith("Template updated")) {
            return ResponseEntity.ok(Map.of("message", result));
        }
        return ResponseEntity.status(500).body(Map.of("error", result));
    }

    // PATCH /api/item-templates/{itemId}/disable
    @PatchMapping("/{itemId}/disable")
    public ResponseEntity<?> disableTemplate(@PathVariable Integer itemId) {
        String result = itemTemplateService.disableTemplate(itemId);
        if (result.startsWith("Template disabled")) {
            return ResponseEntity.ok(Map.of("message", result));
        }
        return ResponseEntity.status(404).body(Map.of("error", result));
    }
}