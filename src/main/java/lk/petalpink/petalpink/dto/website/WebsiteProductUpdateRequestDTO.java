package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private Integer   mainCategoryId;
    private Integer   subCategoryId;
    private String    imageUrl;
    private String    imageUrl2;
    private String    imageUrl3;

    // Clothing-only variants: sent only when mainCategoryId is the Clothing category.
    // Each entry is one size together with the EXACT colors it comes in for this
    // product. If this field is null (not sent at all) existing variants are left
    // untouched; if it is an empty list, all existing variants for the product are removed.
    private List<ProductSizeGroupRequestDTO> sizeGroups;
}
