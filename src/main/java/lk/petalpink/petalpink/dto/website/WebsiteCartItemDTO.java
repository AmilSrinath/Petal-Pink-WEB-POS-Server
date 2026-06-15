package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteCartItemDTO {
    private Integer quantity;
    private String  productName;
    private Double  price;
    private Double  subTotal;
}
