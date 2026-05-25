package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.StockDTO;
import lk.petalpink.petalpink.dto.StockDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StockRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StockDTO findByItemId(int itemId) {
        String sql = """
        SELECT s.*, i.item_code_prefix
        FROM pos_inv_stock_tb s
        LEFT JOIN pos_main_item_tb i ON s.item_id = i.item_id
        WHERE s.item_id = ? LIMIT 1
        """;
        List<StockDTO> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(StockDTO.class), itemId);
        return result.isEmpty() ? null : result.get(0);
    }

    public int insertMaster(int itemId, String itemName, double initialQty, Integer unitType) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, unit_type, status, is_low_stock_alert, low_stock_alert) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, itemId);
            ps.setString(2, itemName);
            ps.setDouble(3, initialQty);
            if (unitType != null) ps.setInt(4, unitType);
            else ps.setNull(4, java.sql.Types.INTEGER);
            ps.setNull(5, java.sql.Types.INTEGER);
            ps.setNull(6, java.sql.Types.DECIMAL);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int updateMasterQty(int itemId, int quantityChange) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET qty = qty + ?, eddited_date = CURRENT_TIMESTAMP " +
                "WHERE item_id = ?";
        return jdbcTemplate.update(sql, quantityChange, itemId);
    }

    public List<StockDTO> findAllMaster() {
        String sql = """
        SELECT s.*, i.item_code_prefix
        FROM pos_inv_stock_tb s
        LEFT JOIN pos_main_item_tb i ON s.item_id = i.item_id
        WHERE s.status != 0
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockDTO.class));
    }

//    public int insertDetail(StockDetailsDTO dto) {
//        String sql = "INSERT INTO pos_inv_stock_details_tb " +
//                "(stock_id, grn_id, item_id, " +
//                "stock_category_id, stock_name, cost_price, last_grn_price, quantity, status, user_id, visible) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        return jdbcTemplate.update(sql,
//                dto.getStockId(), dto.getGrnId(), dto.getItemId(),
//                dto.getStockCategoryId(), dto.getStockName(),
//                dto.getCostPrice(), dto.getLastGrnPrice(), dto.getQuantity(),
//                dto.getStatus(), dto.getUserId(), dto.getVisible());
//    }

    public List<StockDetailsDTO> findDetailsByStockId(int stockId) {
        String sql = "SELECT * FROM pos_inv_stock_details_tb WHERE stock_id = ? ORDER BY stock_details_id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockDetailsDTO.class), stockId);
    }

    public List<StockDetailsDTO> findAllDetails() {
        String sql = "SELECT * FROM pos_inv_stock_details_tb ORDER BY stock_details_id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockDetailsDTO.class));
    }

    public Optional<StockDTO> findByItemIdForGRN(int itemId) {
        String sql = "SELECT * FROM pos_inv_stock_tb WHERE item_id = ? LIMIT 1";
        List<StockDTO> result = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(StockDTO.class), itemId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void insert(int itemId, String itemName, int qty) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, status) " +
                "VALUES (?, ?, ?, 1)";
        jdbcTemplate.update(sql, itemId, itemName, qty);
    }

    public void incrementQty(int itemId, int qty) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET qty = qty + ?, eddited_date = NOW() WHERE item_id = ?";
        jdbcTemplate.update(sql, qty, itemId);
    }

    public void insert(int itemId, String itemName, double qty) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, status) " +
                "VALUES (?, ?, ?, 1)";
        jdbcTemplate.update(sql, itemId, itemName, qty);
    }

    public void incrementQty(int itemId, double qty) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET qty = qty + ?, eddited_date = NOW() WHERE item_id = ?";
        jdbcTemplate.update(sql, qty, itemId);
    }

    public void insert(int itemId, String itemName, double qty, Integer unitType) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, unit_type, status, is_low_stock_alert, low_stock_alert) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?)";
        jdbcTemplate.update(sql, itemId, itemName, qty, unitType, null, null);
    }

    public int updateMasterQty(int itemId, double quantityChange) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET qty = qty + ?, eddited_date = CURRENT_TIMESTAMP " +
                "WHERE item_id = ?";
        return jdbcTemplate.update(sql, quantityChange, itemId);
    }

    public int updateLowStockAlert(int itemId, int isLowStockAlert, double lowStockAlert) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET is_low_stock_alert = ?, low_stock_alert = ?, eddited_date = CURRENT_TIMESTAMP " +
                "WHERE item_id = ?";
        return jdbcTemplate.update(sql, isLowStockAlert, lowStockAlert, itemId);
    }

    public int insertMaster(int itemId, String itemName, double initialQty,
                            Integer unitType, Integer isLowStockAlert, Double lowStockAlert) {
        String sql = "INSERT INTO pos_inv_stock_tb " +
                "(item_id, item_name, qty, unit_type, status, is_low_stock_alert, low_stock_alert) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, itemId);
            ps.setString(2, itemName);
            ps.setDouble(3, initialQty);
            if (unitType != null) ps.setInt(4, unitType);
            else ps.setNull(4, java.sql.Types.INTEGER);
            if (isLowStockAlert != null) ps.setInt(5, isLowStockAlert);
            else ps.setNull(5, java.sql.Types.INTEGER);
            if (lowStockAlert != null) ps.setDouble(6, lowStockAlert);
            else ps.setNull(6, java.sql.Types.DECIMAL);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void updateAlertAndUnitType(int itemId, Integer unitType,
                                       Integer isLowStockAlert, Double lowStockAlert) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET unit_type = ?, is_low_stock_alert = ?, low_stock_alert = ?, " +
                "eddited_date = CURRENT_TIMESTAMP WHERE item_id = ?";
        jdbcTemplate.update(sql, unitType, isLowStockAlert, lowStockAlert, itemId);
    }

    // Replace your existing void insert() with this:
    public int insertAndGetId(int itemId, String itemName, int qty) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, status) " +
                "VALUES (?, ?, ?, 1)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, itemId);
            ps.setString(2, itemName);
            ps.setInt(3, qty);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // Recalculate master stock qty by summing plusQty of all active batch profiles for an item
//    public int recalculateQtyByItemId(int itemId) {
//        String sql = """
//        UPDATE pos_inv_stock_tb s
//        SET s.qty = (
//            SELECT COALESCE(SUM(bp.plus_qty), 0)
//            FROM pos_inv_batch_profile bp
//            WHERE bp.item_id = ?
//              AND bp.is_active = 1
//        )
//        WHERE s.item_id = ?
//        """;
//        return jdbcTemplate.update(sql, itemId, itemId);
//    }
}