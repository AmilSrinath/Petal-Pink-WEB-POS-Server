package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockInitDTO {
    private Integer itemId;
    private String itemName;
    private Integer unitType;
    private Integer isLowStockAlert;
    private Double lowStockAlert;
}
