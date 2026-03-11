package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.UnitTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UnitTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(UnitTypeDTO dto) {
        String sql = "INSERT INTO pos_main_unit_type_tb (unit_type, status, user_id, visible) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getUnitType(), dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<UnitTypeDTO> findAll() {
        String sql = "SELECT * FROM pos_main_unit_type_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UnitTypeDTO.class));
    }

    public int update(UnitTypeDTO dto) {
        String sql = "UPDATE pos_main_unit_type_tb SET unit_type=?, status=?, user_id=?, visible=? WHERE unit_type_id=?";
        return jdbcTemplate.update(sql, dto.getUnitType(), dto.getStatus(), dto.getUserId(), dto.getVisible(), dto.getUnitTypeId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_main_unit_type_tb SET status = 0 WHERE unit_type_id = ?", id);
    }
}