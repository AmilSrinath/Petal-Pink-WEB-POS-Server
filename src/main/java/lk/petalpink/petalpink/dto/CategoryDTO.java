package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private Integer mainItemCategoryId;
    private String mainItemCategoryName;
    private String imagePath;
    private Integer status;
    private Integer userId;
    private Integer visible;
    private Integer editedBy;
}