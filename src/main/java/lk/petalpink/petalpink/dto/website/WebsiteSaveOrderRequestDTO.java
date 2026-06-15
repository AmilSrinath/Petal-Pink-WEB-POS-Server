package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteSaveOrderRequestDTO {
    private String  firstName;
    private String  lastName;
    private String  address1;
    private String  address2;
    private String  city;
    private String  email;
    private String  phone1;
    private String  phone2;
    private String  province;
    private String  country;
    private List<WebsiteCartItemDTO> cartItems;
    private Double  total;
    private String  paymentMethod;
    private Double  delivery;
    private Double  subTotal;
    private Double  totalWeight;
}
