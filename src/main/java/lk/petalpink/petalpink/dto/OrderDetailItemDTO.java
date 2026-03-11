package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailItemDTO {
    private Integer itemId;
    private Integer itemBarCode;
    private Integer unitTypeId;
    private Integer printerTypeId;
    private Integer quantity;
    private Double perItemPrice;
    private Double totalDiscountPrice;
    private Double totalItemPrice;
    private String remark;
}