package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    // order fields
    private Integer orderId;
    private Integer customerId;
    private Integer deliveryOrderId;
    private String billNo;
    private Integer discountId;
    private Double subTotalPrice;
    private Double totalDiscountPrice;
    private Double deliveryFee;
    private Double totalOrderPrice;
    private Integer paymentTypeId;
    private Integer tableId;
    private Timestamp createdDate;
    private Timestamp editedDate;
    private Integer adult;
    private Integer child;
    private String remark;
    private Integer userId;
    private Integer editedBy;
    private Integer status;
    private Integer visible;
    private Integer isPrint;
    private Double paidAmount;

    // customer fields
    private String customerName;
    private String nic;
    private String address;
    private String phoneOne;
    private String phoneTwo;
    private Integer isLoyalty;
    private Double loyaltyAmount;
    private String customerNumber;

    // add these new fields to OrderDTO.java
    private Integer deliveryId;
    private String websiteOrderId;
    private String orderCode;
    private BigDecimal codAmount;
    private String weight;
    private String deliveryRemark;       // aliased to avoid conflict with order remark
    private String orderType;
    private Integer deliveryStatus;      // aliased to avoid conflict with order status
    private Integer statusId;
    private Integer isFreeDelivery;
    private Integer isReturn;
    private Integer isExchange;
    private Timestamp deliveryCreatedDate; // aliased to avoid conflict with order createdDate
    private Timestamp deliveredDate;
}