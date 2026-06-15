package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class WebsiteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ══════════════════════════════════════════════════════════════════════════
    //  CONFIGURATION
    // ══════════════════════════════════════════════════════════════════════════

    public int saveConfig(WebsiteConfigDTO dto) {
        // Deactivate old entries for this config_name
        jdbcTemplate.update(
                "UPDATE petal_pink_configuration_tb SET status = 0 WHERE config_name = ?",
                dto.getConfigName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_configuration_tb (config_name, config_value, created_date, status, user_id) VALUES (?, ?, NOW(), 1, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getConfigName());
            ps.setString(2, dto.getConfigValue());
            ps.setInt(3, dto.getUserId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<WebsiteConfigDTO> getAllConfigs() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_configuration_tb WHERE status = 1",
                new BeanPropertyRowMapper<>(WebsiteConfigDTO.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BUSINESS PROFILE  (petal_pink_business_profile_tb)
    // ══════════════════════════════════════════════════════════════════════════

    public int createBusinessProfile(WebsiteBusinessProfileDTO dto) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_business_profile_tb (business_name, description) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getBusinessName());
            ps.setString(2, dto.getDescription());
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public List<WebsiteBusinessProfileDTO> getAllBusinessProfiles() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_business_profile_tb WHERE status = 1",
                new BeanPropertyRowMapper<>(WebsiteBusinessProfileDTO.class));
    }

    public int updateBusinessProfile(Integer businessId, WebsiteBusinessProfileDTO dto) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_business_profile_tb SET business_name = ?, description = ? WHERE business_id = ? AND status = 1",
                dto.getBusinessName(), dto.getDescription(), businessId);
    }

    public int deleteBusinessProfile(Integer businessId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_business_profile_tb SET status = 0 WHERE business_id = ?",
                businessId);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BANNERS
    // ══════════════════════════════════════════════════════════════════════════

    public int saveBanner(String title, String subtitle, String imageUrl, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_banners_tb (title, subtitle, image_url, created_date, status, user_id) VALUES (?, ?, ?, NOW(), 1, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, subtitle);
            ps.setString(3, imageUrl);
            ps.setInt(4, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public int updateBanner(Integer id, String title, String subtitle, String imageUrl, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_banners_tb SET title = ?, subtitle = ?, image_url = ?, user_id = ? WHERE id = ? AND status = 1",
                title, subtitle, imageUrl, userId, id);
    }

    public int deleteBanner(Integer id) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_banners_tb SET status = 0 WHERE id = ? AND status = 1", id);
    }

    public List<WebsiteBannerDTO> getAllBanners() {
        return jdbcTemplate.query(
                "SELECT id, title, subtitle, image_url, created_date, user_id FROM petal_pink_banners_tb WHERE status = 1 ORDER BY created_date DESC",
                new BeanPropertyRowMapper<>(WebsiteBannerDTO.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  COMMENTS
    // ══════════════════════════════════════════════════════════════════════════

    public int addComment(String content, String clientImg, String clientName, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_comment_tb (content, clientImg, clientName, user_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, content);
            ps.setString(2, clientImg);
            ps.setString(3, clientName);
            ps.setInt(4, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public String getCommentImage(Integer id) {
        List<String> result = jdbcTemplate.queryForList(
                "SELECT clientImg FROM petal_pink_comment_tb WHERE comment_id = ?", String.class, id);
        if (result.isEmpty()) throw new RuntimeException("Comment not found.");
        return result.get(0);
    }

    public int updateComment(Integer id, String content, String clientImg, String clientName, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_comment_tb SET content = ?, clientImg = ?, clientName = ?, user_id = ? WHERE comment_id = ?",
                content, clientImg, clientName, userId, id);
    }

    public int deleteComment(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM petal_pink_comment_tb WHERE comment_id = ?", id);
    }

    public List<WebsiteCommentDTO> getAllComments() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_comment_tb",
                new BeanPropertyRowMapper<>(WebsiteCommentDTO.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CUSTOMERS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteCustomerDTO> getAllCustomers() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_customer_tb ORDER BY cus_id DESC",
                new BeanPropertyRowMapper<>(WebsiteCustomerDTO.class));
    }

    public long getCustomerCount() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM petal_pink_customer_tb", Long.class);
        return count != null ? count : 0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ORDERS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteOrderDTO> getAllOrders() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_order_tb ORDER BY order_id DESC",
                new BeanPropertyRowMapper<>(WebsiteOrderDTO.class));
    }

    public WebsiteOrderDTO getOrderWithCustomer(String orderId) {
        List<WebsiteOrderDTO> result = jdbcTemplate.query(
                """
                SELECT o.order_id, o.created_date, o.payment, o.total, o.delivery, o.sub_total,
                       o.order_status, o.tracking_number,
                       c.cus_id, c.first_name, c.last_name, c.address1, c.address2,
                       c.city, c.email, c.phone_1, c.phone_2, c.province, c.country
                FROM petal_pink_order_tb o
                JOIN petal_pink_customer_tb c ON o.cus_id = c.cus_id
                WHERE o.order_id = ?
                """,
                new BeanPropertyRowMapper<>(WebsiteOrderDTO.class), orderId);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<WebsiteOrderItemDTO> getOrderItems(String orderId) {
        return jdbcTemplate.query(
                """
                SELECT d.product_name, d.quantity, d.price, d.sub_total, p.image_url
                FROM petal_pink_order_details_tb d
                LEFT JOIN petal_pink_product_tb p ON d.product_name = p.product_name
                WHERE d.order_id = ?
                """,
                new BeanPropertyRowMapper<>(WebsiteOrderItemDTO.class), orderId);
    }

    public List<WebsiteOrderItemDTO> getOrderItemsSimple(String orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_order_details_tb WHERE order_id = ?",
                new BeanPropertyRowMapper<>(WebsiteOrderItemDTO.class), orderId);
    }

    public void markOrderInactive(String orderId) {
        jdbcTemplate.update(
                "UPDATE petal_pink_order_tb SET status = 0 WHERE order_id = ?", orderId);
    }

    public List<WebsiteNewOrderDTO> getNewOrders() {
        return jdbcTemplate.query(
                """
                SELECT c.first_name, c.address1, c.address2, c.city, c.province, c.email,
                       c.phone_1, c.phone_2,
                       o.payment, o.total, o.delivery, o.sub_total, o.order_id
                FROM petal_pink_order_tb o
                JOIN petal_pink_customer_tb c ON o.cus_id = c.cus_id
                WHERE o.get_data = 0
                """,
                new BeanPropertyRowMapper<>(WebsiteNewOrderDTO.class));
    }

    public void markOrdersAsReceived() {
        jdbcTemplate.update(
                "UPDATE petal_pink_order_tb SET get_data = 1 WHERE get_data = 0");
    }

    public int updateOrderStatus(String orderId, String status) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_order_tb SET order_status = ? WHERE order_id = ?",
                status, orderId);
    }

    public void updateTrackingNumber(String orderId, String trackingNumber) {
        jdbcTemplate.update(
                "UPDATE petal_pink_order_tb SET tracking_number = ? WHERE order_id = ?",
                trackingNumber, orderId);
    }

    @Transactional
    public String saveOrder(WebsiteSaveOrderRequestDTO dto) {
        // Step 1 — generate new order ID
        List<String> lastOrderList = jdbcTemplate.queryForList(
                "SELECT last_order_id FROM petal_pink_order_tb ORDER BY last_order_id DESC LIMIT 1",
                String.class);
        String lastOrderId = lastOrderList.isEmpty() ? "P100000" : lastOrderList.get(0);
        int numericPart = Integer.parseInt(lastOrderId.substring(1));
        String newOrderId = "P" + String.format("%06d", numericPart + 1);

        // Step 2 — find or create customer
        List<Integer> existingCustomer = jdbcTemplate.queryForList(
                "SELECT cus_id FROM petal_pink_customer_tb WHERE phone_1 = ?", Integer.class, dto.getPhone1());

        int customerId;
        if (!existingCustomer.isEmpty()) {
            customerId = existingCustomer.get(0);
            jdbcTemplate.update(
                    "UPDATE petal_pink_customer_tb SET first_name=?, last_name=?, address1=?, address2=?, city=?, email=?, phone_2=?, province=?, country=?, status=1 WHERE cus_id=?",
                    dto.getFirstName(), dto.getLastName(), dto.getAddress1(), dto.getAddress2(),
                    dto.getCity(), dto.getEmail(), dto.getPhone2(), dto.getProvince(), dto.getCountry(), customerId);
        } else {
            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO petal_pink_customer_tb (first_name, last_name, address1, address2, city, email, phone_1, phone_2, province, country, created_date, status) VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),1)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, dto.getFirstName());
                ps.setString(2, dto.getLastName());
                ps.setString(3, dto.getAddress1());
                ps.setString(4, dto.getAddress2());
                ps.setString(5, dto.getCity());
                ps.setString(6, dto.getEmail());
                ps.setString(7, dto.getPhone1());
                ps.setString(8, dto.getPhone2());
                ps.setString(9, dto.getProvince());
                ps.setString(10, dto.getCountry());
                return ps;
            }, kh);
            customerId = Objects.requireNonNull(kh.getKey()).intValue();
        }

        // Step 3 — insert order
        String finalNewOrderId = newOrderId;
        int finalCustomerId = customerId;
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_order_tb (order_id, cus_id, created_date, payment, status, total, delivery, sub_total, last_order_id, order_status, get_data) VALUES (?,?,NOW(),?,1,?,?,?,?,?,0)");
            ps.setString(1, finalNewOrderId);
            ps.setInt(2, finalCustomerId);
            ps.setString(3, dto.getPaymentMethod());
            ps.setDouble(4, dto.getTotal());
            ps.setDouble(5, dto.getDelivery());
            ps.setDouble(6, dto.getSubTotal());
            ps.setString(7, finalNewOrderId);
            ps.setString(8, "Pending");
            return ps;
        });

        // Step 4 — insert order details
        for (WebsiteCartItemDTO item : dto.getCartItems()) {
            jdbcTemplate.update(
                    "INSERT INTO petal_pink_order_details_tb (order_id, quantity, product_name, price, sub_total, created_date, status) VALUES (?,?,?,?,?,NOW(),1)",
                    newOrderId, item.getQuantity(), item.getProductName(), item.getPrice(), item.getSubTotal());
        }

        // Step 5 — update last order ID reference on all rows
        jdbcTemplate.update("UPDATE petal_pink_order_tb SET last_order_id = ?", newOrderId);

        return newOrderId;
    }

    public double getTodaySales() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT SUM(sub_total) FROM petal_pink_order_tb WHERE DATE(created_date) = CURDATE()",
                Double.class);
        return result != null ? result : 0.0;
    }

    public List<Double> getYearSales() {
        List<double[]> rows = jdbcTemplate.query(
                """
                SELECT MONTH(created_date) AS month, SUM(sub_total) AS monthlySales
                FROM petal_pink_order_tb
                WHERE YEAR(created_date) = YEAR(CURDATE())
                GROUP BY MONTH(created_date)
                ORDER BY MONTH(created_date)
                """,
                (RowMapper<double[]>) (rs, rowNum) ->
                        new double[]{rs.getDouble("month"), rs.getDouble("monthlySales")});

        List<Double> monthlySales = new ArrayList<>();
        for (int i = 0; i < 12; i++) monthlySales.add(0.0);
        for (double[] row : rows) {
            monthlySales.set((int) row[0] - 1, row[1]);
        }
        return monthlySales;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PRODUCTS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteProductDTO> getAllProducts() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_product_tb WHERE status = 1",
                new BeanPropertyRowMapper<>(WebsiteProductDTO.class));
    }

    public WebsiteProductDTO getProductById(Integer productId) {
        List<WebsiteProductDTO> result = jdbcTemplate.query(
                "SELECT * FROM petal_pink_product_tb WHERE product_id = ?",
                new BeanPropertyRowMapper<>(WebsiteProductDTO.class), productId);
        return result.isEmpty() ? null : result.get(0);
    }

    public WebsiteProductDTO getProductByName(String productName) {
        List<WebsiteProductDTO> result = jdbcTemplate.query(
                "SELECT * FROM petal_pink_product_tb WHERE product_name = ?",
                new BeanPropertyRowMapper<>(WebsiteProductDTO.class), productName);
        return result.isEmpty() ? null : result.get(0);
    }

    public void saveProduct(WebsiteProductSaveRequestDTO req, String img1, String img2, String img3) {
        jdbcTemplate.update(
                """
                INSERT INTO petal_pink_product_tb
                    (product_name, unit_type, product_price, quantity, discount, status, visible,
                     create_date, edit_date, image_url, image_url_2, image_url_3,
                     user_id, business_name, weight, amount, description, keyPoints, faq, howToUse)
                VALUES (?,?,?,?,?,1,1,NOW(),NOW(),?,?,?,?,?,?,?,?,?,?,?)
                """,
                req.getProductName(), req.getUnitType() != null ? req.getUnitType() : "ml",
                req.getProductPrice(), req.getQuantity() != null ? req.getQuantity() : 0,
                req.getDiscount() != null ? req.getDiscount() : 0,
                img1, img2, img3,
                req.getUserId() != null ? req.getUserId() : "U001",
                req.getBusinessName(),
                req.getWeight(), req.getAmount() != null ? req.getAmount() : 0,
                req.getDescription() != null ? req.getDescription() : "",
                req.getKeyPoints() != null ? req.getKeyPoints() : "",
                req.getFaq() != null ? req.getFaq() : "",
                req.getHowToUse() != null ? req.getHowToUse() : "");
    }

    public int updateProduct(WebsiteProductUpdateRequestDTO req, String img1, String img2, String img3) {
        StringBuilder sql = new StringBuilder(
                """
                UPDATE petal_pink_product_tb SET
                    product_name=?, unit_type=?, product_price=?, quantity=?, discount=?,
                    weight=?, amount=?, description=?, keyPoints=?, faq=?, howToUse=?
                """);
        List<Object> params = new ArrayList<>();
        params.add(req.getProductName());
        params.add(req.getUnitType() != null ? req.getUnitType() : "");
        params.add(req.getProductPrice() != null ? req.getProductPrice() : 0);
        params.add(req.getQuantity() != null ? req.getQuantity() : 0);
        params.add(req.getDiscount() != null ? req.getDiscount() : 0);
        params.add(req.getWeight() != null ? req.getWeight() : 0);
        params.add(req.getAmount() != null ? req.getAmount() : 0);
        params.add(req.getDescription() != null ? req.getDescription() : "");
        params.add(req.getKeyPoints() != null ? req.getKeyPoints() : "");
        params.add(req.getFaq() != null ? req.getFaq() : "");
        params.add(req.getHowToUse() != null ? req.getHowToUse() : "");

        if (img1 != null) { sql.append(", image_url=?");   params.add(img1); }
        if (img2 != null) { sql.append(", image_url_2=?"); params.add(img2); }
        if (img3 != null) { sql.append(", image_url_3=?"); params.add(img3); }

        sql.append(" WHERE product_id=?");
        params.add(req.getProductId());

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    public int deleteProduct(Integer productId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_product_tb SET status = 0 WHERE product_id = ?", productId);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  WEBSITE USERS  (petal_pink_user_tb)
    // ══════════════════════════════════════════════════════════════════════════

    public void saveUser(String employeeId, String email, String password, String name,
                         String nic, String role, Integer status, Integer visible, String imageUrl) {
        // Generate next user_id
        List<String> lastIdList = jdbcTemplate.queryForList(
                "SELECT user_id FROM petal_pink_user_tb ORDER BY user_id DESC LIMIT 1", String.class);
        String lastId = lastIdList.isEmpty() ? null : lastIdList.get(0);
        String newUserId = generateNextUserId(lastId);

        jdbcTemplate.update(
                "INSERT INTO petal_pink_user_tb (user_id, employee_id, email, password, role, status, visible, name, nic, image_url) VALUES (?,?,?,?,?,?,?,?,?,?)",
                newUserId, employeeId, email, password, role, status, visible, name, nic, imageUrl);
    }

    public List<WebsiteUserDTO> getAllWebsiteUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_user_tb WHERE visible = 1",
                new BeanPropertyRowMapper<>(WebsiteUserDTO.class));
    }

    public WebsiteUserDTO getUserByEmail(String email) {
        List<WebsiteUserDTO> result = jdbcTemplate.query(
                "SELECT * FROM petal_pink_user_tb WHERE email = ? AND visible = 1",
                new BeanPropertyRowMapper<>(WebsiteUserDTO.class), email);
        return result.isEmpty() ? null : result.get(0);
    }

    public int updateUser(String userId, WebsiteUserUpdateDTO dto) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET employee_id=?, email=?, role=?, status=?, visible=? WHERE user_id=?",
                dto.getEmployeeId(), dto.getEmail(), dto.getRole(),
                dto.getStatus() != null ? dto.getStatus() : 1,
                dto.getVisible() != null ? dto.getVisible() : 1,
                userId);
    }

    public int deleteUser(String userId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET visible = 0 WHERE user_id = ?", userId);
    }

    public int setResetCode(String email, String code) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET reset_code = ?, reset_code_expiry = DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE email = ?",
                code, email);
    }

    public void verifyResetCode(String email, String code) {
        List<Object[]> rows = jdbcTemplate.query(
                "SELECT reset_code, reset_code_expiry FROM petal_pink_user_tb WHERE email = ?",
                (rs, rowNum) -> new Object[]{rs.getString("reset_code"), rs.getTimestamp("reset_code_expiry")},
                email);

        if (rows.isEmpty()) throw new RuntimeException("User not found.");
        String storedCode = (String) rows.get(0)[0];
        java.sql.Timestamp expiry = (java.sql.Timestamp) rows.get(0)[1];

        if (!code.equals(storedCode)) throw new RuntimeException("Invalid reset code.");
        if (expiry != null && expiry.before(new java.util.Date())) throw new RuntimeException("Reset code has expired.");
    }

    public void resetPassword(String email, String password) {
        jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET password = ?, reset_code = NULL, reset_code_expiry = NULL WHERE email = ?",
                password, email);
    }

    public int updatePassword(String email, String password) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET password = ? WHERE email = ?", password, email);
    }

    public int updateProfilePicture(String email, String imageUrl) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET image_url = ? WHERE email = ?", imageUrl, email);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private String generateNextUserId(String lastId) {
        if (lastId == null || lastId.isBlank()) return "U001";
        int num = Integer.parseInt(lastId.replace("U", ""));
        return "U" + String.format("%03d", num + 1);
    }
}
