package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long save(ItemDTO dto) {
        String sql = "INSERT INTO pos_main_item_tb (item_bar_code, main_item_category_id, sub_item_category_id, " +
                "bussiness_profile, item_prefix, item_code_prefix, discount, item_name, unit_type, printer_type, cost_price, " +
                "unit_price, image_path, grn_status, selling_status, status, user_id, visible, weight) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dto.getItemBarCode());
            ps.setLong(2, dto.getMainItemCategoryId());
            ps.setLong(3, dto.getSubItemCategoryId());
            if (dto.getBussinessProfile() != null) ps.setInt(4, dto.getBussinessProfile());
            else ps.setNull(4, java.sql.Types.INTEGER);
            ps.setString(5, dto.getItemPrefix());
            ps.setString(6, dto.getItemCodePrefix());
            ps.setDouble(7, dto.getDiscount());
            ps.setString(8, dto.getItemName());
            ps.setString(9, dto.getUnitType());
            ps.setString(10, dto.getPrinterType());
            ps.setDouble(11, dto.getCostPrice());
            ps.setDouble(12, dto.getUnitPrice());
            ps.setString(13, dto.getImagePath());
            ps.setInt(14, dto.getGrnStatus());
            ps.setInt(15, dto.getSellingStatus());
            ps.setInt(16, dto.getStatus());
            ps.setLong(17, dto.getUserId());
            ps.setInt(18, dto.getVisible());
            ps.setDouble(19, dto.getWeight());

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ItemDTO> findAll() {
        String sql = """
        SELECT i.*,
               mc.main_item_category_name,
               sc.sub_item_category_name,
               bp_main.bussiness_profile_name,
               (
                   SELECT sd.last_grn_price
                   FROM pos_inv_stock_tb st
                   JOIN pos_inv_stock_details_tb sd ON sd.stock_id = st.stock_id
                   WHERE st.item_id = i.item_id
                     AND sd.last_grn_price IS NOT NULL
                   ORDER BY sd.stock_details_id DESC
                   LIMIT 1
               ) AS last_grn_price
        FROM pos_main_item_tb i
        LEFT JOIN pos_main_item_category_tb mc ON i.main_item_category_id = mc.main_item_category_id
        LEFT JOIN pos_sub_item_category_tb sc ON i.sub_item_category_id = sc.sub_item_category_id
        LEFT JOIN pos_main_bussiness_profile bp_main ON i.bussiness_profile = bp_main.bussiness_profile_id
        WHERE i.status != 0
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemDTO.class));
    }

    public int update(ItemDTO dto) {
        String sql = "UPDATE pos_main_item_tb SET " +
                "item_bar_code=?, " +
                "main_item_category_id=?, " +
                "sub_item_category_id=?, " +
                "bussiness_profile=?, " +
                "item_prefix=?, " +
                "item_code_prefix=?, " +
                "discount=?, " +
                "item_name=?, " +
                "unit_type=?, " +
                "printer_type=?, " +
                "cost_price=?, " +
                "unit_price=?, " +
                "image_path=?, " +
                "grn_status=?, " +
                "selling_status=?, " +
                "status=?, " +
                "user_id=?, " +
                "visible=?, " +
                "weight=? " +
                "WHERE item_id=?";

        return jdbcTemplate.update(sql,
                dto.getItemBarCode(),
                dto.getMainItemCategoryId(),
                dto.getSubItemCategoryId(),
                dto.getBussinessProfile(),      // ← must match position 4
                dto.getItemPrefix(),
                dto.getItemCodePrefix(),
                dto.getDiscount(),
                dto.getItemName(),
                dto.getUnitType(),
                dto.getPrinterType(),
                dto.getCostPrice(),
                dto.getUnitPrice(),
                dto.getImagePath(),
                dto.getGrnStatus(),
                dto.getSellingStatus(),
                dto.getStatus(),
                dto.getUserId(),
                dto.getVisible(),
                dto.getWeight(),
                dto.getItemId()                 // ← WHERE clause param, always last
        );
    }

    public int softDelete(int itemId) {
        String sql = "UPDATE pos_main_item_tb SET status = 0 WHERE item_id = ?";
        return jdbcTemplate.update(sql, itemId);
    }

    public List<ItemDTO> findAllGRN() {
        String sql = """
            SELECT i.*,
                   mc.main_item_category_name,
                   sc.sub_item_category_name,
                   bp.bussiness_profile_name
            FROM pos_main_item_tb i
            LEFT JOIN pos_main_item_category_tb mc ON i.main_item_category_id = mc.main_item_category_id
            LEFT JOIN pos_sub_item_category_tb sc ON i.sub_item_category_id = sc.sub_item_category_id
            LEFT JOIN pos_main_bussiness_profile bp ON i.bussiness_profile = bp.bussiness_profile_id
            WHERE i.status != 0
            """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemDTO.class));
    }

    // In your ItemRepository (or wherever pos_main_item_tb is queried)
    public String findItemNameById(int itemId) {
        String sql = "SELECT item_name FROM pos_main_item_tb WHERE item_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, itemId);
    }

    public List<ItemDTO> findBySubcategoryId(int subCategoryId) {
        String sql = """
        SELECT i.*,
               mc.main_item_category_name,
               sc.sub_item_category_name,
               bp.bussiness_profile_name
        FROM pos_main_item_tb i
        LEFT JOIN pos_main_item_category_tb mc ON i.main_item_category_id = mc.main_item_category_id
        LEFT JOIN pos_sub_item_category_tb sc ON i.sub_item_category_id = sc.sub_item_category_id
        LEFT JOIN pos_main_bussiness_profile bp ON i.bussiness_profile = bp.bussiness_profile_id
        WHERE i.status != 0
          AND i.sub_item_category_id = ?
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemDTO.class), subCategoryId);
    }
}