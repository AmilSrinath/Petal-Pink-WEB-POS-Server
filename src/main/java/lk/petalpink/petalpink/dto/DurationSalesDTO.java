package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DurationSalesDTO {

    // ── Order Counts ──────────────────────────────────────────────────────────
    private Long allOrdersCount;
    private Long activeOrdersCount;       // status_id = 1
    private Long pendingOrdersCount;      // status_id = 2
    private Long wrappingOrdersCount;     // status_id = 3
    private Long despatchOrdersCount;     // status_id = 4
    private Long deliveredOrdersCount;    // status_id = 5
    private Long returnOrdersCount;       // status_id = 6
    private Long cancelOrdersCount;       // status_id = 7, 15
    private Long returningOrdersCount;    // status_id = 12
    private Long checkingOrdersCount;     // status_id = 13
    private Long returnedOrdersCount;     // status_id = 14

    // ── Order Percentages ─────────────────────────────────────────────────────
    private Double allOrdersPercent;
    private Double activeOrdersPercent;
    private Double pendingOrdersPercent;
    private Double wrappingOrdersPercent;
    private Double despatchOrdersPercent;
    private Double deliveredOrdersPercent;
    private Double returnOrdersPercent;
    private Double cancelOrdersPercent;
    private Double returningOrdersPercent;
    private Double checkingOrdersPercent;
    private Double returnedOrdersPercent;

    // ── Financial Totals ──────────────────────────────────────────────────────
    private Double totalReportCash;        // COD  (excluding return & cancel)
    private Double totalReportCard;        // Card (excluding return & cancel)
    private Double returnOrdersTotal;      // Total value of return orders   (status_id = 6)
    private Double cancelOrdersTotal;      // Total value of cancelled orders (status_id = 7, 15)
    private Double totalDeliveryCharge;    // Sum of all delivery fees
    private Double grandTotal;             // Cash + Card + Returns + Cancels
}