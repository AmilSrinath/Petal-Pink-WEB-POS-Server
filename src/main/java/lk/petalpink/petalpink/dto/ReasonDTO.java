package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReasonDTO {
    private Integer resonId;
    private String reson;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
    private Integer userId;
}