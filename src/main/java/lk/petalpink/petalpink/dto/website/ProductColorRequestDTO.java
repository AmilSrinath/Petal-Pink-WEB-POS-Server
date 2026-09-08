package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductColorRequestDTO {
    private String colorName;
    private String colorCode;    // optional hex code e.g. #FF0000
    private List<String> images; // 3+ already-uploaded GCS image URLs
}
