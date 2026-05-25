package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockAdjTypeDTO {
    private Integer stockAdjTypeId;
    private String stockAdjTypeName;
    private Integer isStockAdd;       // 1 = Add stock, 0 = Reduce stock
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp editedDate;
    private Integer status;
}