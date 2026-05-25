package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDetailsDTO {
    private Integer stockDetailsId;
    private Integer stockLocationId;   // from GRN header
    private Integer batchRegId;        // from saved batch reg
    private Integer stockAdjTypeId;    // fixed: e.g. 1 = GRN
    private Integer stockId;
    private String  stockName;         // from BatchProfileDTO item name
    private Double  costPrice;         // from BatchProfileDTO
    private Double  lastGrnPrice;      // same as costPrice on GRN
    private Double  plusQty;           // quantity being added
    private Double  minusQty;          // null / 0 for GRN
    private Integer isInitQty;         // 0 for normal GRN
    private Integer status;
    private Integer visible;
    private LocalDate createdDate;
    private Integer userId;
}