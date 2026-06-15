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

    // ─────────────────────────────────────────────────────────────────────────
    //  NEW — look up master record by stock_id (PK)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Find a stock master row by its own PK (stock_id).
     * Used by the adjustment endpoints so they can validate the record
     * and check the current qty before writing a detail row.
     */
    public StockDTO findByStockId(int stockId) {
        String sql = """
            SELECT s.*, i.item_code_prefix, u.unit_type AS unit_type_name
            FROM pos_inv_stock_tb s
            LEFT JOIN pos_main_item_tb  i ON s.item_id      = i.item_id
            LEFT JOIN pos_main_unit_type_tb u ON s.unit_type = u.unit_type_id
            WHERE s.stock_id = ? AND s.status != 0
            LIMIT 1
            """;
        List<StockDTO> result = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(StockDTO.class), stockId);

        System.out.println("stockId : "+stockId);
        return result.isEmpty() ? null : result.get(0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  NEW — insert a manual adjustment detail row
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Inserts one row into pos_inv_stock_details_tb for a manual adjustment.
     * batch_reg_id is intentionally NULL for manual adjustments
     * (only GRN / production rows carry a batch reference).
     */
    public void insertAdjustmentDetail(StockDetailsDTO dto) {
        String sql = """
            INSERT INTO pos_inv_stock_details_tb
                (stock_location_id, batch_reg_id, stock_adj_type_id, stock_id, stock_name,
                 cost_price, last_grn_price, plus_qty, minus_qty,
                 is_init_qty, status, visible, created_date, edited_date, user_id)
            VALUES (?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)
            """;
        jdbcTemplate.update(sql,
                dto.getStockLocationId(),
                dto.getStockAdjTypeId(),
                dto.getStockId(),
                dto.getStockName(),
                dto.getCostPrice(),
                dto.getLastGrnPrice(),
                dto.getPlusQty(),
                dto.getMinusQty(),
                dto.getIsInitQty(),
                dto.getStatus(),
                dto.getVisible(),
                dto.getCreatedDate(),
                dto.getUserId()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  EXISTING METHODS (unchanged)
    // ─────────────────────────────────────────────────────────────────────────

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

    public int updateMasterQty(int itemId, double quantityChange) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET qty = qty + ?, eddited_date = CURRENT_TIMESTAMP " +
                "WHERE item_id = ?";
        return jdbcTemplate.update(sql, quantityChange, itemId);
    }

    public List<StockDTO> findAllMaster() {
        String sql = """
            SELECT s.*, i.item_code_prefix, u.unit_type AS unit_type_name
            FROM pos_inv_stock_tb s
            LEFT JOIN pos_main_item_tb i ON s.item_id = i.item_id
            LEFT JOIN pos_main_unit_type_tb u ON s.unit_type = u.unit_type_id
            WHERE s.status != 0
            """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockDTO.class));
    }

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

    public void insert(int itemId, String itemName, double qty, Integer unitType) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, unit_type, status, is_low_stock_alert, low_stock_alert) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?)";
        jdbcTemplate.update(sql, itemId, itemName, qty, unitType, null, null);
    }

    public void incrementQty(int itemId, double qty) {
        String sql = "UPDATE pos_inv_stock_tb SET qty = qty + ?, eddited_date = NOW() WHERE item_id = ?";
        jdbcTemplate.update(sql, qty, itemId);
    }

    public int updateLowStockAlert(int itemId, int isLowStockAlert, double lowStockAlert) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET is_low_stock_alert = ?, low_stock_alert = ?, eddited_date = CURRENT_TIMESTAMP " +
                "WHERE item_id = ?";
        return jdbcTemplate.update(sql, isLowStockAlert, lowStockAlert, itemId);
    }

    public int insertMaster(int itemId, String itemName, double initialQty,
                            Integer unitType, Integer isLowStockAlert, Double lowStockAlert,
                            Integer isSellingItem) {
        String sql = "INSERT INTO pos_inv_stock_tb " +
                "(item_id, item_name, qty, unit_type, status, is_low_stock_alert, low_stock_alert, is_selling_item) " +
                "VALUES (?, ?, ?, ?, 1, ?, ?, ?)";
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
            if (isSellingItem != null) ps.setInt(7, isSellingItem);
            else ps.setNull(7, java.sql.Types.INTEGER);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void updateAlertAndUnitType(int itemId, Integer unitType,
                                       Integer isLowStockAlert, Double lowStockAlert,
                                       Integer isSellingItem) {
        String sql = "UPDATE pos_inv_stock_tb " +
                "SET unit_type = ?, is_low_stock_alert = ?, low_stock_alert = ?, is_selling_item = ?, " +
                "eddited_date = CURRENT_TIMESTAMP WHERE item_id = ?";
        jdbcTemplate.update(sql, unitType, isLowStockAlert, lowStockAlert, isSellingItem, itemId);
    }

    public int insertAndGetId(int itemId, String itemName, int qty) {
        String sql = "INSERT INTO pos_inv_stock_tb (item_id, item_name, qty, status) VALUES (?, ?, ?, 1)";
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
}