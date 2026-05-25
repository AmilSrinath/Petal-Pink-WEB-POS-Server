package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchMergeRequestDTO {
    private List<Integer> sourceProfileIds;  // profile_ids to merge
    private Integer targetGrnId;             // GRN to attach the new merged batch to
    private String  remark;                  // optional note on the merged batch
    private Integer userId;
}