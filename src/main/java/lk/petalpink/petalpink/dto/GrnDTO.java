package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrnDTO {
    private Integer grnId;
    private String invoiceNo;
    private Integer supplierId;
    private String supplierName;
    private Double totalPrice;
    private Double totalDiscount;
    private LocalDate createdDate;
    private Integer status;
    private Integer stockLocationId;
    private Integer userId;
    private Integer visible;
}