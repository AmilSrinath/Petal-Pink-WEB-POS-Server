package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebSubCategoryDTO {
    private Integer subCategoryId;
    private String subCategoryName;
    private Integer mainCategoryId;
    private Timestamp createdDate;
    private Timestamp editedDate;
    private Integer userId;
    private Integer status;
}