package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ProductionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ─── SAVE & return generated production_id ────────────────────────────────

    public int saveAndGetId(ProductionDTO dto) {
        String sql = """
            INSERT INTO pos_inv_production_tb
                (ref_no, man_date, exp_date, item_id, item_name,
                 unit_type, qty, wastage, status, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getRefNo());
            ps.setDate  (2, Date.valueOf(dto.getManDate()));
            ps.setDate  (3, dto.getExpDate() != null ? Date.valueOf(dto.getExpDate()) : null);
            ps.setInt   (4, dto.getItemId());
            ps.setString(5, dto.getItemName());
            if (dto.getUnitType() != null) ps.setInt(6, dto.getUnitType());
            else ps.setNull(6, java.sql.Types.INTEGER);
            ps.setDouble(7, dto.getQty());
            ps.setDouble(8, dto.getWastage() != null ? dto.getWastage() : 0.0);
            ps.setInt   (9, dto.getUserId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // ─── GET ALL ──────────────────────────────────────────────────────────────

    public List<ProductionDTO> findAll() {
        String sql = """
            SELECT p.*,
                   u.unit_type AS unit_type_name
            FROM pos_inv_production_tb p
            LEFT JOIN pos_main_unit_type_tb u ON u.unit_type_id = p.unit_type
            WHERE p.status = 1
            ORDER BY p.production_id DESC
            """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductionDTO.class));
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────

    public ProductionDTO findById(int productionId) {
        String sql = """
            SELECT p.*,
                   u.unit_type AS unit_type_name
            FROM pos_inv_production_tb p
            LEFT JOIN pos_main_unit_type_tb u ON u.unit_type_id = p.unit_type
            WHERE p.production_id = ?
            """;
        List<ProductionDTO> result = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(ProductionDTO.class), productionId);
        return result.isEmpty() ? null : result.get(0);
    }

    // ─── NEXT REF-NO ─────────────────────────────────────────────────────────
    // Returns the next auto-generated reference number, e.g. "PRD-0001"

    public String nextRefNo() {
        String sql = "SELECT COALESCE(MAX(production_id), 0) + 1 FROM pos_inv_production_tb";
        Integer next = jdbcTemplate.queryForObject(sql, Integer.class);
        return String.format("PRD-%04d", next);
    }

    // ─── SOFT DELETE ─────────────────────────────────────────────────────────

    public int softDelete(int productionId) {
        String sql = "UPDATE pos_inv_production_tb SET status = 0 WHERE production_id = ?";
        return jdbcTemplate.update(sql, productionId);
    }
}