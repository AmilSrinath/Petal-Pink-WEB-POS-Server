package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotPaidPaymentDTO {
    private Integer paymentId;
    private Integer orderId;
    private Integer customerId;
    private String  customerNumber;
    private String  orderCode;
    private Double  totalAmount;
    private Double  totalOrderPrice;
    private Integer paymentStatus;
    private Integer deliveryOrderId;
    private LocalDateTime createdDate;
}