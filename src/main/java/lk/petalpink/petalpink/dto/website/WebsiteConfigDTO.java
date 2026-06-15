package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteConfigDTO {
    private Integer configId;
    private String  configName;
    private String  configValue;
    private Timestamp createdDate;
    private Integer status;
    private Integer userId;
}
