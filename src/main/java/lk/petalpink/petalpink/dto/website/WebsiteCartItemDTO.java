package lk.petalpink.petalpink.dto.website;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class WebsiteCartItemDTO {
    private Integer productId;
    private String  productName;
    private String  unitType;
    private String  imageUrl;
    private String  mainCategoryName;
    private String  subCategoryName;
    private Integer quantity;
    private Double  price;
    private Double  discount;
    private Double  subTotal;
    private String  selectedSize;
    private String  selectedColor;
}
