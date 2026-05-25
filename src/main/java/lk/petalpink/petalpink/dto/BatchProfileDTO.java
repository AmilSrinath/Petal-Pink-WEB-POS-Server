package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchProfileDTO {
    private Integer profileId;
    private Integer regId;
    private Integer itemId;
    private Integer grnId;
    private Double costPrice;
    private Double retailPrice;
    private Double wholeSalePrice;
    private String expDate;
    private Integer isReleaseForSell;
    private String poNo;
    private Integer unitType;
    private Integer isActive;
    private Integer userId;
    private String remark;
    private Double plusQty;
}