package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteNewOrderDTO {
    private String  firstName;
    private String  address1;
    private String  address2;
    private String  city;
    private String  province;
    private String  email;
    private String  phone1;
    private String  phone2;
    private String  payment;
    private Double  total;
    private Double  delivery;
    private Double  subTotal;
    private String  orderId;
    private List<WebsiteOrderItemDTO> items;
}
