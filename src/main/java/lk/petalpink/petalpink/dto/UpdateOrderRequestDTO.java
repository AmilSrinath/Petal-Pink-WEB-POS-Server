package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOrderRequestDTO {

    private Integer deliveryId;

    // customer
    private String customerName;
    private String phoneOne;
    private String phoneTwo;
    private String address;
    private String customerNumber;

    // delivery order
    private String orderCode;  // tracking code - can update now
    private BigDecimal codAmount;
    private String weight;
    private String remark;
    private String orderType;
    private Integer isFreeDelivery;
    private Integer isReturn;
    private Integer isExchange;
    private Integer userId;
    private Integer statusId;

    // order
    private Double subTotalPrice;
    private Double totalDiscountPrice;
    private Double deliveryFee;
    private Double totalOrderPrice;
    private Double paidAmount;
    private Integer paymentTypeId;

    // order details
    private List<OrderDetailItemDTO> items;
}