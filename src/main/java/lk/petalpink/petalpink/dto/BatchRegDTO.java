package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchRegDTO {
    private Integer regId;
    private String batchRegPrefix;   // default "BA"
    private Integer batchRegCode;
    private Integer isActive;
    private Integer userId;
    private String remark;
}