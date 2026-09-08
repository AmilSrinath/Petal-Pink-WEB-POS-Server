package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteProductSaveRequestDTO {
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
    private String    userId;
    private String    businessName;
    private String    imageUrl;
    private String    imageUrl2;
    private String    imageUrl3;

    private Integer mainCategoryId;
    private Integer subCategoryId;

    // Clothing-only variants: sent only when mainCategoryId is the Clothing category.
    // Each entry is one size together with the EXACT colors it comes in for this
    // product (a size can have many colors and a color can belong to many sizes,
    // but each pairing is explicit here — no cross-joining across unrelated sizes).
    private List<ProductSizeGroupRequestDTO> sizeGroups;
}
