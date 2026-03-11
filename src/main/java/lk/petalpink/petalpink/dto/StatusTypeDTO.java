package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusTypeDTO {
    private Integer statusId;
    private String statusType;
    private Integer regId;
    private Integer status;
}