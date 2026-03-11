package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrinterTypeDTO {
    private Integer printerTypeId;
    private String printerType;
    private Integer status;
    private Integer userId;
    private Integer visible;
}