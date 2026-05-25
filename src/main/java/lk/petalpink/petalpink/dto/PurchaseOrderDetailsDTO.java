package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderDetailsDTO {
    private Integer poDetailsId;
    private Integer poId;
    private Integer itemId;
    private String itemName;
    private BigDecimal qty;
    private BigDecimal expectedPrice;
    private BigDecimal lastGrnPrice;
    private BigDecimal totalPrice;
    private Integer userId;
}