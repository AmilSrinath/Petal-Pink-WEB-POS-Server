package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteOrderDTO {
    // Order fields
    private String    orderId;
    private Timestamp createdDate;
    private String    payment;
    private Double    total;
    private Double    delivery;
    private Double    subTotal;
    private String    orderStatus;
    private String    trackingNumber;
    // Customer fields (populated for getOrderDetails)
    private Integer   cusId;
    private String    firstName;
    private String    lastName;
    private String    address1;
    private String    address2;
    private String    city;
    private String    email;
    private String    phone1;
    private String    phone2;
    private String    province;
    private String    country;
}
