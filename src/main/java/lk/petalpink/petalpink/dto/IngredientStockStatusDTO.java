package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientStockStatusDTO {
    private Integer subItemId;
    private String subItemName;
    private Double requiredQty;   // qty * orderQuantity
    private Double availableQty;  // current stock
    private boolean sufficient;
}