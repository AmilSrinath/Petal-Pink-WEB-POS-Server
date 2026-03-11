package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitTypeDTO {
    private Integer unitTypeId;
    private String unitType;
    private Integer status;
    private Integer userId;
    private Integer visible;
}