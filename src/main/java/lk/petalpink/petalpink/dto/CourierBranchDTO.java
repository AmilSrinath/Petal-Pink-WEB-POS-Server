package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierBranchDTO {
    private Integer branchId;
    private String branchName;
    private String branchContact;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
    private String company;        // changed from Integer companyId
    private Integer companyId;     // keep this for save/update operations
    private Integer status;
    private Integer userId;
}