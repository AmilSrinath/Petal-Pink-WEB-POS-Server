package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteProductDTO {
    private Integer   productId;
    private String    product_name;
    private String    unit_type;
    private Double    product_price;
    private Double    discount;
    private Integer   status;
    private Integer   visible;
    private Timestamp createDate;
    private Timestamp editDate;
    private String    imageUrl;
    private String    image_url_2;
    private String    image_url_3;
    private String    userId;
    private String    businessName;
    private Double    weight;
    private Double    amount;
    private String    description;
    private String    keyPoints;
    private String    faq;
    private String    howToUse;
    private Integer   isDeliveryFree;

    private Integer   mainCategoryId;
    private String    mainCategoryName;
    private Integer   subCategoryId;
    private String    subCategoryName;

    // Clothing-only variants (empty lists for non-clothing products).
    // colors/sizes are flat, de-duplicated aggregates used by the product listing table.
    // sizeGroups carries the exact per-size color pairings, used to pre-fill the edit form.
    private List<ProductColorDTO> colors;
    private List<ProductSizeDTO>  sizes;
    private List<ProductSizeGroupDTO> sizeGroups;
}
