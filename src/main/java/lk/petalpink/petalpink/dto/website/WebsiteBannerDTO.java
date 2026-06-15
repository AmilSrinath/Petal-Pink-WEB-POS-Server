package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteBannerDTO {
    private Integer   id;
    private String    title;
    private String    subtitle;
    private String    imageUrl;
    private Timestamp createdDate;
    private Integer   userId;
}
