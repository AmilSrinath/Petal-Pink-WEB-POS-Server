package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockCheckResultDTO {
    private boolean available;
    private String message;
    private List<IngredientStockStatusDTO> ingredientStatuses; // per-ingredient detail
}