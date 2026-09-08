package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductSizeGroupDTO {
    private Integer sizeId;
    private String  sizeName;
    private List<ProductColorDTO> colors; // exactly the colors this size comes in
}
