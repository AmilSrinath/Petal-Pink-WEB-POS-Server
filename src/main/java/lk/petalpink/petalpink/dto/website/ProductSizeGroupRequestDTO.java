package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductSizeGroupRequestDTO {
    private String sizeName;
    private List<ProductColorRequestDTO> colors; // exactly the colors this size comes in
}
