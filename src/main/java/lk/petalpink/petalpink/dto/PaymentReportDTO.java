package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentReportDTO {
    private Integer paymentId;
    private Double cod;
    private Double totalAmount;
    private Integer paymentStatus;
    private Integer orderId;
    private Integer customerId;
    private Integer deliveryOrderId;
    private String billNo;
    private Double subTotalPrice;
    private Double deliveryFee;
    private Double totalOrderPrice;
    private Integer paymentTypeId;
    private LocalDateTime createdDate;
    private String remark;
    private Integer isPrint;
    private Integer statusId;
    private String orderCode;
    private Integer deliveryStatusId;
    private String customerNumber;
}