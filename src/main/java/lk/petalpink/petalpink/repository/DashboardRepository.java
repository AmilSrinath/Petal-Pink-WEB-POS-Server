package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.DashboardDTO;
import lk.petalpink.petalpink.dto.ItemSaleCountDTO;
import lk.petalpink.petalpink.dto.StatusTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StatusRepository statusRepository;

    /*
     * reg_id = 3  →  closed / terminal inquiry statuses
     *   (Delivered, Not Delivered, Returned, Cancel)
     * These are loaded live from pos_status_types so no IDs are hardcoded here.
     */
    private static final int INQUIRY_CLOSED_STATUS_REG_ID = 3;

    public DashboardDTO getSummary() {

        // ── 1. Fetch closed inquiry status IDs from pos_status_types ─────────
        List<StatusTypeDTO> closedStatuses =
                statusRepository.getStatusTypesByRegId(INQUIRY_CLOSED_STATUS_REG_ID);

        List<Integer> closedStatusIds = closedStatuses.stream()
                .map(StatusTypeDTO::getStatusId)
                .collect(Collectors.toList());

        // Build a safe IN clause — falls back to "-1" (matches nothing) when empty.
        String closedIdsIn = closedStatusIds.isEmpty()
                ? "-1"
                : closedStatusIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        // ── 2. Single query for the main KPIs ─────────────────────────────────
        String sql = """
                SELECT
                    COUNT(DISTINCT d.delivery_id)                               AS todayOrdersCount,

                    COALESCE(
                        SUM(
                            CASE
                                WHEN d.status_id NOT IN (6, 7, 15)
                                THEN o.total_order_price
                                ELSE 0
                            END
                        ), 0
                    )                                                            AS todayRevenue,

                    (
                        SELECT COUNT(*)
                        FROM pos_main_delivery_order_tb pd
                        WHERE pd.status_id IN (2, 3, 4)
                    )                                                            AS pendingDeliveriesCount

                FROM pos_main_delivery_order_tb d
                LEFT JOIN pos_main_order_tb o
                       ON o.delivery_order_id = d.delivery_id
                WHERE DATE(d.created_date) = CURDATE()
                """;

        // ── 3. Active Inquiries ───────────────────────────────────────────────
        String inquirySql = "SELECT COUNT(*) FROM pos_inquiry_tb WHERE status_id = 11";
        Long activeInquiriesCount = jdbcTemplate.queryForObject(inquirySql, Long.class);

        // ── 4. Today's item-wise sale counts ──────────────────────────────────
        List<ItemSaleCountDTO> itemSaleCounts = getTodayItemSaleCounts();

        // ── 5. Map main KPI results ───────────────────────────────────────────
        DashboardDTO dto = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            DashboardDTO d = new DashboardDTO();
            d.setTodayOrdersCount(rs.getLong("todayOrdersCount"));
            d.setTodayRevenue(rs.getDouble("todayRevenue"));
            d.setPendingDeliveriesCount(rs.getLong("pendingDeliveriesCount"));
            return d;
        });

        dto.setActiveInquiriesCount(activeInquiriesCount != null ? activeInquiriesCount : 0L);
        dto.setTodayItemSaleCounts(itemSaleCounts);

        return dto;
    }

    /**
     * Returns the item-wise sale count for today.
     *
     * Joins:
     *   pos_main_delivery_order_tb  (delivery date filter + status filter)
     *   pos_main_order_tb           (links delivery → order)
     *   pos_main_order_details_tb   (individual line items)
     *   pos_main_item_tb            (item name)
     *
     * Excludes orders with status_id IN (6=Return, 7=Cancel, 15=Cancel).
     * Groups by item and orders by totalQuantitySold DESC.
     */
    public List<ItemSaleCountDTO> getTodayItemSaleCounts() {
        String sql = """
                SELECT
                    i.item_id                        AS itemId,
                    i.item_name                      AS itemName,
                    SUM(od.quantity)                 AS totalQuantitySold,
                    SUM(od.total_item_price)         AS totalItemRevenue
                FROM pos_main_delivery_order_tb d
                JOIN pos_main_order_tb o
                     ON o.delivery_order_id = d.delivery_id
                JOIN pos_main_order_details_tb od
                     ON od.order_id = o.order_id
                JOIN pos_main_item_tb i
                     ON i.item_id = od.item_id
                WHERE DATE(d.created_date) = CURDATE()
                  AND d.status_id NOT IN (6, 7, 15)
                GROUP BY i.item_id, i.item_name
                ORDER BY totalQuantitySold DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemSaleCountDTO dto = new ItemSaleCountDTO();
            dto.setItemId(rs.getInt("itemId"));
            dto.setItemName(rs.getString("itemName"));
            dto.setTotalQuantitySold(rs.getLong("totalQuantitySold"));
            dto.setTotalItemRevenue(rs.getDouble("totalItemRevenue"));
            return dto;
        });
    }
}