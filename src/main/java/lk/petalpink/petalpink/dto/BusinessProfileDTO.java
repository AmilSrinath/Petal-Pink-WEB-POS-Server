package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessProfileDTO {
    private Integer bussinessProfileId;
    private String bussinessProfileName;
    private Integer status;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp editedDate;
}