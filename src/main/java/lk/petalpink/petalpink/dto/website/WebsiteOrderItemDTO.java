package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteOrderItemDTO {
    private String  productName;
    private Integer quantity;
    private Double  price;
    private Double  subTotal;
    private String  imageUrl;
}
