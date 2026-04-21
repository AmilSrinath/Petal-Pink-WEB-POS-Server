package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.DurationSalesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DurationSalesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * Status ID reference:
     *  1  = Active
     *  2  = Pending
     *  3  = Wrapping
     *  4  = Despatch
     *  5  = Delivered
     *  6  = Return       ← excluded from Cash/Card totals
     *  7  = Cancel       ← excluded from Cash/Card totals
     *  8  = Paid
     *  9  = Not Paid
     *  10 = Delivered
     *  11 = Not Delivered
     *  12 = Returning
     *  13 = Checking
     *  14 = Returned
     *  15 = Cancel       ← excluded from Cash/Card totals
     */
    public DurationSalesDTO getDurationSales(String dateFrom, String dateTo) {

        String sql = """
                SELECT
                    -- ── Order Counts ──────────────────────────────────────────────────────
                    COUNT(pmo.order_id)                                                              AS allOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 1             THEN 1 END)                        AS activeOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 2             THEN 1 END)                        AS pendingOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 3             THEN 1 END)                        AS wrappingOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 4             THEN 1 END)                        AS despatchOrdersCount,
                    COUNT(CASE WHEN pmd.status_id IN (5, 10)      THEN 1 END)                        AS deliveredOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 6             THEN 1 END)                        AS returnOrdersCount,
                    COUNT(CASE WHEN pmd.status_id IN (7, 15)      THEN 1 END)                        AS cancelOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 12            THEN 1 END)                        AS returningOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 13            THEN 1 END)                        AS checkingOrdersCount,
                    COUNT(CASE WHEN pmd.status_id = 14            THEN 1 END)                        AS returnedOrdersCount,

                    -- ── Financial Totals ──────────────────────────────────────────────────
                    SUM(CASE WHEN pmd.status_id NOT IN (6, 7, 15) THEN pmd.cod_amount        ELSE 0 END) AS totalReportCash,
                    SUM(CASE WHEN pmd.status_id NOT IN (6, 7, 15) THEN pmo.paid_amount       ELSE 0 END) AS totalReportCard,
                    SUM(CASE WHEN pmd.status_id = 6               THEN pmo.total_order_price ELSE 0 END) AS returnOrdersTotal,
                    SUM(CASE WHEN pmd.status_id IN (7, 15)        THEN pmo.total_order_price ELSE 0 END) AS cancelOrdersTotal,
                    SUM(pmo.delivery_fee)                                                                 AS totalDeliveryCharge,
                    (
                        SUM(CASE WHEN pmd.status_id NOT IN (6, 7, 15) THEN pmd.cod_amount        ELSE 0 END) +
                        SUM(CASE WHEN pmd.status_id NOT IN (6, 7, 15) THEN pmo.paid_amount       ELSE 0 END) -
                        SUM(CASE WHEN pmd.status_id = 6               THEN pmo.total_order_price ELSE 0 END) -
                        SUM(CASE WHEN pmd.status_id IN (7, 15)        THEN pmo.total_order_price ELSE 0 END) -
                        SUM(pmo.delivery_fee)
                    )                                                                                     AS grandTotal

                FROM pos_main_order_tb pmo
                JOIN pos_main_delivery_order_tb pmd ON pmo.delivery_order_id = pmd.delivery_id
                WHERE DATE(pmo.created_Date) BETWEEN ? AND ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

            DurationSalesDTO dto = new DurationSalesDTO();

            // ── Counts ────────────────────────────────────────────────────────
            long total = rs.getLong("allOrdersCount");
            dto.setAllOrdersCount(total);
            dto.setActiveOrdersCount(rs.getLong("activeOrdersCount"));
            dto.setPendingOrdersCount(rs.getLong("pendingOrdersCount"));
            dto.setWrappingOrdersCount(rs.getLong("wrappingOrdersCount"));
            dto.setDespatchOrdersCount(rs.getLong("despatchOrdersCount"));
            dto.setDeliveredOrdersCount(rs.getLong("deliveredOrdersCount"));
            dto.setReturnOrdersCount(rs.getLong("returnOrdersCount"));
            dto.setCancelOrdersCount(rs.getLong("cancelOrdersCount"));
            dto.setReturningOrdersCount(rs.getLong("returningOrdersCount"));
            dto.setCheckingOrdersCount(rs.getLong("checkingOrdersCount"));
            dto.setReturnedOrdersCount(rs.getLong("returnedOrdersCount"));

            // ── Percentages (safe divide) ─────────────────────────────────────
            dto.setAllOrdersPercent(      100.00);
            dto.setActiveOrdersPercent(   calcPercent(dto.getActiveOrdersCount(),    total));
            dto.setPendingOrdersPercent(  calcPercent(dto.getPendingOrdersCount(),   total));
            dto.setWrappingOrdersPercent( calcPercent(dto.getWrappingOrdersCount(),  total));
            dto.setDespatchOrdersPercent( calcPercent(dto.getDespatchOrdersCount(),  total));
            dto.setDeliveredOrdersPercent(calcPercent(dto.getDeliveredOrdersCount(), total));
            dto.setReturnOrdersPercent(   calcPercent(dto.getReturnOrdersCount(),    total));
            dto.setCancelOrdersPercent(   calcPercent(dto.getCancelOrdersCount(),    total));
            dto.setReturningOrdersPercent(calcPercent(dto.getReturningOrdersCount(), total));
            dto.setCheckingOrdersPercent( calcPercent(dto.getCheckingOrdersCount(),  total));
            dto.setReturnedOrdersPercent( calcPercent(dto.getReturnedOrdersCount(),  total));

            // ── Financials ─────────────────────────────────────────────────────
            dto.setTotalReportCash(rs.getDouble("totalReportCash"));
            dto.setTotalReportCard(rs.getDouble("totalReportCard"));
            dto.setReturnOrdersTotal(rs.getDouble("returnOrdersTotal"));
            dto.setCancelOrdersTotal(rs.getDouble("cancelOrdersTotal"));
            dto.setTotalDeliveryCharge(rs.getDouble("totalDeliveryCharge"));
            dto.setGrandTotal(rs.getDouble("grandTotal"));

            return dto;

        }, dateFrom, dateTo);
    }

    /** Returns percentage rounded to 2 decimal places; 0.00 when total is 0. */
    private double calcPercent(long part, long total) {
        if (total == 0) return 0.00;
        return Math.round((part * 100.0 / total) * 100.0) / 100.0;
    }

    public String getNextTrackingCode() {
        String sql =
                "SELECT order_code FROM pos_main_delivery_order_tb " +
                        "WHERE order_code LIKE 'NPP%' " +
                        "ORDER BY order_code DESC LIMIT 1";
        try {
            String last = jdbcTemplate.queryForObject(sql, String.class);
            if (last != null && last.matches("NPP\\d{7}")) {
                long num = Long.parseLong(last.substring(3));
                return String.format("NPP%07d", num + 1);
            }
        } catch (Exception ignored) {}
        return "NPP0000001";
    }
}