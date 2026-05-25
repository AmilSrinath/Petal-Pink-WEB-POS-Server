package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockCategoryDTO {
    private Integer stockCategoryId;
    private String stockName;
    private String location;
    private Integer status;
    private Integer userId;
    private Integer visible;
}