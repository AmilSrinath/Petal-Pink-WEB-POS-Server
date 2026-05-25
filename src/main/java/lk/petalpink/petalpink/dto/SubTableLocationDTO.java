package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubTableLocationDTO {
    private Integer subTableLocationId;
    private Integer mainTableLocationId;
    private String mainLocationName;
    private String subLocationName;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
}