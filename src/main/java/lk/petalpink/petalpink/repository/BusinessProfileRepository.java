package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.BatchProfileDTO;
import lk.petalpink.petalpink.dto.BusinessProfileDTO;
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
public class BusinessProfileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ── FETCH ALL (active only) ──────────────────────────────────────────────
    public List<BusinessProfileDTO> findAll() {
        String sql = "SELECT * FROM pos_main_bussiness_profile WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BusinessProfileDTO.class));
    }

    // ── FETCH BY ID ──────────────────────────────────────────────────────────
    public Optional<BusinessProfileDTO> findById(Integer id) {
        String sql = "SELECT * FROM pos_main_bussiness_profile WHERE bussiness_profile_id = ? AND status != 0";
        List<BusinessProfileDTO> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(BusinessProfileDTO.class), id);
        return result.stream().findFirst();
    }

    // ── SAVE ─────────────────────────────────────────────────────────────────
    public int save(BusinessProfileDTO dto) {
        String sql = """
                INSERT INTO pos_main_bussiness_profile
                    (bussiness_profile_name, status, user_id)
                VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getBussinessProfileName());
            ps.setInt(2, dto.getStatus() != null ? dto.getStatus() : 1);
            ps.setInt(3, dto.getUserId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); // returns generated ID
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public int update(BusinessProfileDTO dto) {
        String sql = """
                UPDATE pos_main_bussiness_profile
                SET bussiness_profile_name = ?,
                    status                 = ?,
                    user_id                = ?,
                    edited_date            = CURRENT_TIMESTAMP
                WHERE bussiness_profile_id = ?
                  AND status != 0
                """;
        return jdbcTemplate.update(sql,
                dto.getBussinessProfileName(),
                dto.getStatus() != null ? dto.getStatus() : 1,
                dto.getUserId() != null ? dto.getUserId() : 1,
                dto.getBussinessProfileId());
    }

    // ── SOFT DELETE (status → 0) ─────────────────────────────────────────────
    public int softDelete(Integer id) {
        String sql = """
                UPDATE pos_main_bussiness_profile
                SET status      = 0,
                    edited_date = CURRENT_TIMESTAMP
                WHERE bussiness_profile_id = ?
                """;
        return jdbcTemplate.update(sql, id);
    }

    // BatchProfileRepository.java — add this
    public List<BatchProfileDTO> findActiveByItemId(int itemId) {
        String sql = "SELECT * FROM pos_inv_batch_profile WHERE item_id = ? AND is_active = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BatchProfileDTO.class), itemId);
    }
}