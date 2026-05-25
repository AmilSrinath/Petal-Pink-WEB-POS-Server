package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ConfigTableDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigTableDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(ConfigTableDetailsDTO dto) {
        String sql = "INSERT INTO pos_con_config_table_details_tb " +
                "(config_table_id, main_table_location_id, sub_table_location_id, table_name, " +
                "width, height, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getConfigTableId(), dto.getMainTableLocationId(), dto.getSubTableLocationId(),
                dto.getTableName(), dto.getWidth(), dto.getHeight(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<ConfigTableDetailsDTO> findAll() {
        String sql = "SELECT * FROM pos_con_config_table_details_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ConfigTableDetailsDTO.class));
    }

    public List<ConfigTableDetailsDTO> findByConfigTableId(int configTableId) {
        String sql = "SELECT * FROM pos_con_config_table_details_tb WHERE config_table_id = ? AND status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ConfigTableDetailsDTO.class), configTableId);
    }

    public int update(ConfigTableDetailsDTO dto) {
        String sql = "UPDATE pos_con_config_table_details_tb " +
                "SET config_table_id=?, main_table_location_id=?, sub_table_location_id=?, table_name=?, " +
                "width=?, height=?, image_path=?, status=?, user_id=?, visible=? " +
                "WHERE table_id=?";
        return jdbcTemplate.update(sql,
                dto.getConfigTableId(), dto.getMainTableLocationId(), dto.getSubTableLocationId(),
                dto.getTableName(), dto.getWidth(), dto.getHeight(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getTableId());
    }

    public int softDelete(int tableId) {
        String sql = "UPDATE pos_con_config_table_details_tb SET status = 0 WHERE table_id = ?";
        return jdbcTemplate.update(sql, tableId);
    }
}