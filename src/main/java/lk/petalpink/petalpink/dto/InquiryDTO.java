package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InquiryDTO {
    // inquiry fields
    private Integer inquiryId;
    private String wayBill;
    private Integer customerId;
    private String customerName;
    private String customerPhone1;
    private String customerPhone2;
    private String company;
    private String branch;
    private String branchContact;
    private String reason;
    private String remark;
    private Integer status;
    private Date createdDate;
    private Date editedDate;
    private Integer userId;
    private Integer statusId;

    // customer fields (from join)
    private String customerAddress;
    private String customerNic;
    private String customerNumber;
    private String customerPhoneOne;
    private String customerPhoneTwo;
    private Integer isLoyalty;
}