package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductSizeDTO {
    private Integer sizeId;
    private Integer productId;
    private String  sizeName;
}
