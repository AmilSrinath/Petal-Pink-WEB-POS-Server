package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Integer userId;
    private Integer employeeId;
    private Integer roleId;
    private String username;
    private String password;
    private Integer status;
    private Integer visible;
    private String token;
}