package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrnRequestDTO {
    private GrnDTO grn;
    private List<BatchProfileDTO> batchItems;  // each item becomes one batch profile
}