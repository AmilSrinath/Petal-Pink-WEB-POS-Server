package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteUserUpdateDTO {
    private String  employeeId;
    private String  email;
    private String  role;
    private Integer status;
    private Integer visible;
}
