package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrnUpdateRequestDTO {
    private Integer grnId;
    private String invoiceNo;
    private Integer supplierId;
    private Integer stockLocationId;
    private LocalDate createdDate;
    private Double totalPrice;
    private Double totalDiscount;
    private Integer userId;
    private List<GrnItemUpdateDTO> items;
}
