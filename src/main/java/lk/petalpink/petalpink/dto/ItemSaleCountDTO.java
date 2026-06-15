package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the sale count and revenue for a single item sold today.
 * Returned as a list from GET /api/dashboard/today-item-sales
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSaleCountDTO {

    /** The item's primary key. */
    private Integer itemId;

    /** Human-readable item name. */
    private String itemName;

    /** Total units sold today across all non-cancelled/non-returned orders. */
    private Long totalQuantitySold;

    /** Total revenue for this item today (quantity × per_item_price, net of discounts). */
    private Double totalItemRevenue;
}