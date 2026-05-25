// StockTransactionDTO.java - what the controller receives
package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockTransactionDTO {
    // Master stock fields
    private Integer itemId;
    private String itemName;

    // Detail fields
    private Integer grnId;
    private Integer mainItemCategoryId;
    private Integer subItemCategoryId;
    private Integer itemBarCode;
    private Integer stockCategoryId;
    private String stockName;
    private Integer unitTypeId;
    private Double costPrice;
    private Double lastGrnPrice;
    private Integer quantityChange;
    private Integer userId;
    private Integer visible;
    private String reason;
}