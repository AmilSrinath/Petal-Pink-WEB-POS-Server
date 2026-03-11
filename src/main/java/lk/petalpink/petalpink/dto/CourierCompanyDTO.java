package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourierCompanyDTO {
    private Integer companyId;
    private String companyName;
    private String companyContact;
    private String address;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;
    private Integer status;
    private Integer userId;
}