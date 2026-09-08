package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountDTO {
    private Integer userId;
    private String username;
    private String password;      // only ever populated on create/update requests, never returned
    private Integer employeeId;
    private String employeeName;  // joined, read-only
    private Integer roleId;
    private String roleName;      // joined, read-only
    private Integer status;
}