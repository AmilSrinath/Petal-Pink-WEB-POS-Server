package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteOrderDetailsResponseDTO {
    private WebsiteOrderDTO          order;
    private List<WebsiteOrderItemDTO> items;
}
