package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDTO {
    private Integer stockId;
    private Integer itemId;
    private String itemName;
    private Double qty;
    private Integer unitType;
    private Integer status;
    private Integer isLowStockAlert;
    private Double lowStockAlert;
    private String itemCodePrefix;
}