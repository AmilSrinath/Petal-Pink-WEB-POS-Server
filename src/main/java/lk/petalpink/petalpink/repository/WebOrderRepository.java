package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class WebOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ─────────────────────────────────────────────────
    //  SEQUENCE — date-based sequential order ref
    // ─────────────────────────────────────────────────

    /**
     * Count how many orders already exist for today's prefix (NPP-YYYYMMDD)
     * and return the next sequence number (1-based).
     *
     * Uses LIKE 'NPP-20260627%' so it works even when seq grows past 9.
     * Example results: 1, 2, 3 ... 99, 100, 999 ...
     */
    public int getNextDailySequence(String prefix) {
        String sql = "SELECT COUNT(*) FROM web_order_tb WHERE order_ref LIKE ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, prefix + "%");
        return (count == null ? 0 : count) + 1;
    }

    // ─────────────────────────────────────────────────
    //  SAVE
    // ─────────────────────────────────────────────────

    /** Insert header row -> returns generated web_order_id */
    public Integer saveOrder(WebsiteSaveOrderRequestDTO req, String orderRef) {
        String sql = """
                INSERT INTO web_order_tb
                    (order_ref, first_name, last_name, email, phone1, phone2,
                     address1, address2, city, province, country,
                     payment_method, sub_total, delivery_fee, total, total_weight,
                     order_status, created_date, updated_date)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'PENDING',NOW(),NOW())
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,  orderRef);
            ps.setString(2,  req.getFirstName());
            ps.setString(3,  req.getLastName());
            ps.setString(4,  req.getEmail());
            ps.setString(5,  req.getPhone1());
            ps.setString(6,  req.getPhone2());
            ps.setString(7,  req.getAddress1());
            ps.setString(8,  req.getAddress2());
            ps.setString(9,  req.getCity());
            ps.setString(10, req.getProvince());
            ps.setString(11, req.getCountry());
            ps.setString(12, req.getPaymentMethod());
            ps.setObject(13, req.getSubTotal());
            ps.setObject(14, req.getDelivery());
            ps.setObject(15, req.getTotal());
            ps.setObject(16, req.getTotalWeight());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    /** Insert one detail row per cart item (with full product snapshot) */
    public void saveOrderDetails(Integer webOrderId, List<WebsiteCartItemDTO> items) {
        String sql = """
                INSERT INTO web_order_details_tb
                    (web_order_id, product_id, product_name, unit_type, image_url,
                     main_category_name, sub_category_name,
                     quantity, unit_price, discount, sub_total,
                     selected_size, selected_color)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        for (WebsiteCartItemDTO item : items) {
            jdbcTemplate.update(sql,
                    webOrderId,
                    item.getProductId(),
                    item.getProductName(),
                    item.getUnitType(),
                    item.getImageUrl(),
                    item.getMainCategoryName(),
                    item.getSubCategoryName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getDiscount(),
                    item.getSubTotal(),
                    item.getSelectedSize(),
                    item.getSelectedColor()
            );
        }
    }

    /** Links this website order to the Sales-page delivery order created for it. */
    public void linkDeliveryOrder(Integer webOrderId, Integer deliveryOrderId) {
        String sql = "UPDATE web_order_tb SET delivery_order_id = ? WHERE web_order_id = ?";
        jdbcTemplate.update(sql, deliveryOrderId, webOrderId);
    }

    // ─────────────────────────────────────────────────
    //  FETCH — all orders (header only)
    // ─────────────────────────────────────────────────

    private static final RowMapper<WebsiteOrderDTO> ORDER_ROW_MAPPER = (rs, rowNum) -> {
        WebsiteOrderDTO dto = new WebsiteOrderDTO();
        dto.setOrderId(rs.getString("order_ref"));
        dto.setCreatedDate(rs.getTimestamp("created_date"));
        dto.setPayment(rs.getString("payment_method"));
        dto.setSubTotal(rs.getDouble("sub_total"));
        dto.setDelivery(rs.getDouble("delivery_fee"));
        dto.setTotal(rs.getDouble("total"));
        dto.setOrderStatus(rs.getString("order_status"));
        dto.setTrackingNumber(rs.getString("tracking_number"));
        try {
            dto.setDeliveryOrderId(rs.getObject("delivery_order_id") != null ? rs.getInt("delivery_order_id") : null);
        } catch (Exception ignored) {
            // column not present until the migration is applied — degrade gracefully
        }
        dto.setFirstName(rs.getString("first_name"));
        dto.setLastName(rs.getString("last_name"));
        dto.setEmail(rs.getString("email"));
        dto.setPhone1(rs.getString("phone1"));
        dto.setPhone2(rs.getString("phone2"));
        dto.setAddress1(rs.getString("address1"));
        dto.setAddress2(rs.getString("address2"));
        dto.setCity(rs.getString("city"));
        dto.setProvince(rs.getString("province"));
        dto.setCountry(rs.getString("country"));
        return dto;
    };

    public List<WebsiteOrderDTO> getAllOrders() {
        String sql = "SELECT * FROM web_order_tb ORDER BY created_date DESC";
        return jdbcTemplate.query(sql, ORDER_ROW_MAPPER);
    }

    // ─────────────────────────────────────────────────
    //  FETCH — single order header by order_ref
    // ─────────────────────────────────────────────────

    public WebsiteOrderDTO getOrderByRef(String orderRef) {
        String sql = "SELECT * FROM web_order_tb WHERE order_ref = ?";
        List<WebsiteOrderDTO> result = jdbcTemplate.query(sql, ORDER_ROW_MAPPER, orderRef);
        return result.isEmpty() ? null : result.get(0);
    }

    // ─────────────────────────────────────────────────
    //  FETCH — order details (items) by order_ref
    // ─────────────────────────────────────────────────

    public List<WebsiteOrderItemDTO> getOrderItems(String orderRef) {
        String sql = """
                SELECT d.*
                FROM web_order_details_tb d
                JOIN web_order_tb o ON o.web_order_id = d.web_order_id
                WHERE o.order_ref = ?
                ORDER BY d.web_order_detail_id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            WebsiteOrderItemDTO item = new WebsiteOrderItemDTO();
            item.setProductName(rs.getString("product_name"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getDouble("unit_price"));
            item.setSubTotal(rs.getDouble("sub_total"));
            item.setImageUrl(rs.getString("image_url"));
            item.setMainCategoryName(rs.getString("main_category_name"));
            item.setSubCategoryName(rs.getString("sub_category_name"));
            item.setSelectedSize(rs.getString("selected_size"));
            item.setSelectedColor(rs.getString("selected_color"));
            return item;
        }, orderRef);
    }

    // ─────────────────────────────────────────────────
    //  DASHBOARD — sales figures
    // ─────────────────────────────────────────────────

    /** Sum of `total` for every order created today (server date). */
    public double getTodaySales() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM web_order_tb WHERE DATE(created_date) = CURDATE()";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result == null ? 0.0 : result;
    }

    /** Sum of `total` per calendar month (Jan..Dec) for the given year — index 0 = January. */
    public List<Double> getMonthlySales(int year) {
        String sql = """
                SELECT MONTH(created_date) AS m, COALESCE(SUM(total), 0) AS total
                FROM web_order_tb
                WHERE YEAR(created_date) = ?
                GROUP BY MONTH(created_date)
                """;

        Double[] months = new Double[12];
        java.util.Arrays.fill(months, 0.0);

        jdbcTemplate.query(sql, rs -> {
            int month = rs.getInt("m");       // 1..12
            double total = rs.getDouble("total");
            if (month >= 1 && month <= 12) {
                months[month - 1] = total;
            }
        }, year);

        return java.util.Arrays.asList(months);
    }

    // ─────────────────────────────────────────────────
    //  UPDATE — status / tracking number
    // ─────────────────────────────────────────────────

    public int updateOrderStatus(String orderRef, String orderStatus) {
        String sql = "UPDATE web_order_tb SET order_status = ?, updated_date = NOW() WHERE order_ref = ?";
        return jdbcTemplate.update(sql, orderStatus, orderRef);
    }

    public int updateTrackingNumber(String orderRef, String trackingNumber) {
        String sql = "UPDATE web_order_tb SET tracking_number = ?, updated_date = NOW() WHERE order_ref = ?";
        return jdbcTemplate.update(sql, trackingNumber, orderRef);
    }
}