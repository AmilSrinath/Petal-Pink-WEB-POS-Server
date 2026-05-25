package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockCheckRequestDTO {
    private Integer itemId;
    private Double quantity; // how many of this item the customer wants
}