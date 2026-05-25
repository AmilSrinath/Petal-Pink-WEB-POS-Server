package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientDTO {
    private Integer subItemId;
    private String subItemName;
    private Double quantity;
    private String unitType;
}