package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteProductDTO {
    private Integer   productId;
    private String    productName;
    private String    unitType;
    private Double    productPrice;
    private Integer   quantity;
    private Double    discount;
    private Integer   status;
    private Integer   visible;
    private Timestamp createDate;
    private Timestamp editDate;
    private String    imageUrl;
    private String    imageUrl2;
    private String    imageUrl3;
    private String    userId;
    private String    businessName;
    private Double    weight;
    private Double    amount;
    private String    description;
    private String    keyPoints;
    private String    faq;
    private String    howToUse;
}
