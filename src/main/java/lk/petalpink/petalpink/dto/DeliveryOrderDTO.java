package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryOrderDTO {
    // delivery order fields
    private Integer deliveryId;
    private String websiteOrderId;
    private String orderCode;
    private BigDecimal codAmount;
    private String weight;
    private String remark;
    private String orderType;
    private Integer status;
    private Integer statusId;
    private Integer isFreeDelivery;
    private Integer isReturn;
    private Integer isExchange;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp deliveredDate;

    // customer fields
    private Integer customerId;
    private String customerName;
    private String customerNumber;
    private String phoneOne;
    private String phoneTwo;
    private String address;

    // order fields
    private Integer orderId;
    private String billNo;
    private Double subTotalPrice;
    private Double totalDiscountPrice;
    private Double deliveryFee;
    private Double totalOrderPrice;
    private Double paidAmount;
    private Integer paymentTypeId;
}