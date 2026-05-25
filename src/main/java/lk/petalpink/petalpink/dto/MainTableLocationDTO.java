package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainTableLocationDTO {
    private Integer mainTableLocationId;
    private String mainLocationName;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
}