package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteUserDTO {
    private String  userId;
    private String  employeeId;
    private String  email;
    private String  name;
    private String  nic;
    private String  role;
    private Integer status;
    private Integer visible;
    private String  imageUrl;
}
