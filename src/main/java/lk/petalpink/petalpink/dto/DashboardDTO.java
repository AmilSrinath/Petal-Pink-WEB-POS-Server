package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response payload for GET /api/dashboard/summary.
 * All four KPIs + today's item-wise sale counts are returned in a single round-trip.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    /** Number of delivery orders created today (any status). */
    private Long todayOrdersCount;

    /** Total revenue today = SUM(total_order_price) for non-return / non-cancel orders. */
    private Double todayRevenue;

    /**
     * Pending deliveries = orders currently in an in-progress status
     * (Pending=2, Wrapping=3, Despatch=4).
     */
    private Long pendingDeliveriesCount;

    /**
     * Active inquiries = inquiries whose status_id = 11.
     */
    private Long activeInquiriesCount;

    /**
     * Item-wise sale counts for today.
     * Each entry contains itemId, itemName, totalQuantitySold, totalItemRevenue.
     * Ordered by totalQuantitySold DESC.
     */
    private List<ItemSaleCountDTO> todayItemSaleCounts;
}