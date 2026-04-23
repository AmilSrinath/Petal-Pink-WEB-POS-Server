package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategoryDTO {
    private Integer subItemCategoryId;
    private Integer mainItemCategoryId;
    private String mainItemCategoryName;
    private String subItemCategoryName;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
}