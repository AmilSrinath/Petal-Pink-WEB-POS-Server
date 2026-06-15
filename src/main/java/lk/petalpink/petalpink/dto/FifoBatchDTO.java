package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents one batch entry returned by the FIFO query in StockDetailsRepository.
 * Each row is one plus_qty (GRN / production-in) detail record together with
 * how much of it has already been consumed (sum of minus_qty rows for the same
 * batch_reg_id + stock_id), so the service can drain them oldest-first.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FifoBatchDTO {

    /** batch_reg_id from pos_inv_stock_details_tb – FK to pos_inv_batch_reg.reg_id */
    private Integer batchRegId;

    /** stock_details_id of the originating plus-qty row (for audit / traceability) */
    private Integer stockDetailsId;

    /** Original quantity received in this batch */
    private Double plusQty;

    /** Sum of all minus_qty rows already charged against this batch */
    private Double consumedQty;

    /** plusQty - consumedQty: how much is still available to consume */
    private Double availableQty;

    /** Cost price carried on the original GRN / IN row */
    private Double costPrice;

    /** Last GRN price carried on the original GRN / IN row */
    private Double lastGrnPrice;
}