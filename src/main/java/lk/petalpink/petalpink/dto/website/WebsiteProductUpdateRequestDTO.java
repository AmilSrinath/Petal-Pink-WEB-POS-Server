package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteProductUpdateRequestDTO {
    private Integer   productId;
    private String    productName;
    private String    unitType;
    private Double    productPrice;
    private Integer   quantity;
    private Double    discount;
    private Double    weight;
    private Double    amount;
    private String    description;
    private String    keyPoints;
    private String    faq;
    private String    howToUse;
    private MultipartFile imageUrl;
    private MultipartFile imageUrl2;
    private MultipartFile imageUrl3;
}
