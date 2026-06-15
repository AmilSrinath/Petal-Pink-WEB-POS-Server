package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/stocks/add and POST /api/stocks/reduce
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockAdjustmentDTO {

    /** Foreign key to pos_main_item_tb.item_id */
    private Integer itemId;

    /** Human-readable name recorded in the detail row */
    private String stockName;

    /** Adjustment type from pos_inv_stock_adj_type_tb */
    private Integer stockAdjTypeId;

    /** Quantity to add or remove (always a positive number) */
    private Double qty;

    /** Cost price to record on the detail row */
    private Double costPrice;

    /** Last GRN price to record on the detail row */
    private Double lastGrnPrice;

    /** Optional reason / remark */
    private String reason;

    /** User performing the adjustment */
    private Integer userId;
}