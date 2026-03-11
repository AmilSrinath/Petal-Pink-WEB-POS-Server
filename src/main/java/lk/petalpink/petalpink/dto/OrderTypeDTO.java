package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderTypeDTO {
    private Integer id;
    private String type;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime editedDate;
}