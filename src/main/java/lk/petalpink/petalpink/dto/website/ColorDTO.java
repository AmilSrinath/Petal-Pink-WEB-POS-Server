package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ColorDTO {
    private Integer colorId;
    private String  colorName;
    private String  colorCode; // optional hex code e.g. #FF0000
}
