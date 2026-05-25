package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ItemTemplateDTO;
import lk.petalpink.petalpink.repository.ItemTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemTemplateService {

    @Autowired
    private ItemTemplateRepository itemTemplateRepository;

    // ─── SAVE ────────────────────────────────────────────────────────────────

    public String saveTemplate(ItemTemplateDTO dto) {
        if (dto.getIngredients() == null || dto.getIngredients().isEmpty()) {
            return "No ingredients provided";
        }
        int rows = itemTemplateRepository.saveTemplate(dto);
        return rows > 0
                ? "Template saved successfully (" + rows + " ingredient(s))"
                : "Failed to save template";
    }

    // ─── GET ─────────────────────────────────────────────────────────────────

    public ItemTemplateDTO getTemplateByItemId(Integer itemId) {
        return itemTemplateRepository.getTemplateByItemId(itemId);
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    public String updateTemplate(Integer itemId, ItemTemplateDTO dto) {
        if (dto.getIngredients() == null || dto.getIngredients().isEmpty()) {
            return "No ingredients provided";
        }
        dto.setItemId(itemId); // ensure path variable takes precedence
        int rows = itemTemplateRepository.updateTemplate(dto);
        return rows > 0
                ? "Template updated successfully (" + rows + " ingredient(s))"
                : "Failed to update template";
    }

    public String disableTemplate(Integer itemId) {
        int rows = itemTemplateRepository.disableTemplate(itemId);
        return rows > 0
                ? "Template disabled successfully"
                : "No active template found for item ID: " + itemId;
    }
}