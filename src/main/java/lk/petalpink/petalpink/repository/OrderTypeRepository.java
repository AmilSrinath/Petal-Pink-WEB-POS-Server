package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.OrderTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(OrderTypeDTO dto) {
        String sql = "INSERT INTO pos_main_config_order_type_tb (type, status, created_at, edited_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getType(), dto.getStatus(), LocalDateTime.now(), LocalDateTime.now());
    }

    public List<OrderTypeDTO> findAll() {
        String sql = "SELECT * FROM pos_main_config_order_type_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderTypeDTO.class));
    }

    public int update(OrderTypeDTO dto) {
        String sql = "UPDATE pos_main_config_order_type_tb SET type=?, status=?, edited_date=? WHERE id=?";
        return jdbcTemplate.update(sql, dto.getType(), dto.getStatus(), LocalDateTime.now(), dto.getId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_main_config_order_type_tb SET status = 0 WHERE id = ?", id);
    }
}