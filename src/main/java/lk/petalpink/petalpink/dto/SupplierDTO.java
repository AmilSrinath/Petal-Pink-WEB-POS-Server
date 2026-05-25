package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierDTO {
    private Integer supplierId;
    private String salesmanName;
    private String companyName;
    private String brandName;
    private String telephone;
    private String phone;
    private String addree;
    private String gmail;
    private Integer status;
    private Integer userId;
    private Integer visible;
}