package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderDTO {
    private Integer poId;
    private String poPrefix;
    private Long poCode;
    private String poCodePrefix;
    private Integer supplierId;
    private String supplierName;
    private LocalDate poDate;
    private LocalDate expectedDate;
    private BigDecimal totalPrice;
    private Integer paymentType;
    private Integer status;
    private Integer userId;
    private Integer visible;

    // Holds the line items sent together with the order
    private List<PurchaseOrderDetailsDTO> details;
}