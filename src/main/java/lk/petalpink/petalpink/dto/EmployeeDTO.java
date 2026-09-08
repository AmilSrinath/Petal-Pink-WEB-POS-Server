package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    private Integer employeeId;
    private String  firstName;
    private String  lastName;
    private String  email;          // maps to `gmail` column
    private String  phone;
    private Integer designationId;
    private Integer titleId;
    private String  designationName; // joined, read-only
    private String  titleName;       // joined, read-only
    private String  joiningDate;     // "YYYY-MM-DD"
    private Integer status;
}