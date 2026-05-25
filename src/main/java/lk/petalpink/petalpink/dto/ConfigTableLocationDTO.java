package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigTableLocationDTO {
    private Integer configTableLocationId;
    private Integer mainTableLocationId;
    private Integer subTableLocationId;
    private String mainLocationName;
    private String subLocationName;
    private String tablePrefix;
    private Integer noOfTables;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
}