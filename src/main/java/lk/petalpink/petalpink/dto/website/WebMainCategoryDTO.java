package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebMainCategoryDTO {
    private Integer mainCategoryId;
    private String mainCategoryName;
    private Timestamp createdData;
    private Timestamp editedDate;
    private Integer userId;
    private Integer status;
}