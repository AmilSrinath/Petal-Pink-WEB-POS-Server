package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ReasonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReasonRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(ReasonDTO dto) {
        String sql = "INSERT INTO pos_config_reson_tb (reson, status, created_date, edited_date, user_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getReson(), dto.getStatus(), LocalDateTime.now(), LocalDateTime.now(), dto.getUserId());
    }

    public List<ReasonDTO> findAll() {
        String sql = "SELECT * FROM pos_config_reson_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ReasonDTO.class));
    }

    public int update(ReasonDTO dto) {
        String sql = "UPDATE pos_config_reson_tb SET reson=?, status=?, edited_date=?, user_id=? WHERE reson_id=?";
        return jdbcTemplate.update(sql, dto.getReson(), dto.getStatus(), LocalDateTime.now(), dto.getUserId(), dto.getResonId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_config_reson_tb SET status = 0 WHERE reson_id = ?", id);
    }
}