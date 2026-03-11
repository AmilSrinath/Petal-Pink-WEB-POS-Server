package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTypeDTO {
    private Integer paymentTypeId;
    private String paymentType;
    private Integer status;
    private Integer userId;
    private Integer visible;
}