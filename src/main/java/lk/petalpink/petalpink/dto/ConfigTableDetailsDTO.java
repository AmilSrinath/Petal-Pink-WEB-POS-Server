package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigTableDetailsDTO {
    private Integer tableId;
    private Integer configTableId;
    private Integer mainTableLocationId;
    private Integer subTableLocationId;
    private String tableName;
    private Integer width;
    private Integer height;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
}