package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {
    private Integer itemId;
    private Integer itemBarCode;
    private Integer mainItemCategoryId;
    private Integer subItemCategoryId;
    private String itemPrefix;
    private String itemCodePrefix;
    private Double discount;
    private String itemName;
    private String unitType;
    private String printerType;
    private Double costPrice;
    private Double unitPrice;
    private String imagePath;
    private Integer grnStatus;
    private Integer sellingStatus;
    private Integer status;
    private Integer userId;
    private Integer visible;
    private Double weight;
    private Integer quantity;
    private Integer unitTypeId;
    private Integer isLowStockAlert;
    private Double lowStockAlert;

    private Double lastGrnPrice;

    private Integer bussinessProfile;
    private String bussinessProfileName;

    private String mainItemCategoryName;
    private String subItemCategoryName;
}