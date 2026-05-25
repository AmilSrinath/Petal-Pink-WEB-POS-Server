package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemTemplateDTO {
    private Integer itemId;
    private String itemName;        // from pos_main_item_tb
    private String templateName;
    private List<IngredientDTO> ingredients;
}