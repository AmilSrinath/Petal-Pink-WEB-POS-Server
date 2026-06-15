package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteCommentDTO {
    private Integer commentId;
    private String  content;
    private String  clientImg;
    private String  clientName;
    private Integer userId;
}
