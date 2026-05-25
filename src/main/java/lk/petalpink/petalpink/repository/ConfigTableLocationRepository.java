package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ConfigTableLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigTableLocationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(ConfigTableLocationDTO dto) {
        String sql = "INSERT INTO pos_con_config_table_location_tb " +
                "(main_table_location_id, sub_table_location_id, main_location_name, sub_location_name, " +
                "table_prefix, no_of_tables, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainTableLocationId(), dto.getSubTableLocationId(),
                dto.getMainLocationName(), dto.getSubLocationName(),
                dto.getTablePrefix(), dto.getNoOfTables(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<ConfigTableLocationDTO> findAll() {
        String sql = "SELECT * FROM pos_con_config_table_location_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ConfigTableLocationDTO.class));
    }

    public int update(ConfigTableLocationDTO dto) {
        String sql = "UPDATE pos_con_config_table_location_tb " +
                "SET main_table_location_id=?, sub_table_location_id=?, main_location_name=?, sub_location_name=?, " +
                "table_prefix=?, no_of_tables=?, image_path=?, status=?, user_id=?, visible=? " +
                "WHERE config_table_location_id=?";
        return jdbcTemplate.update(sql,
                dto.getMainTableLocationId(), dto.getSubTableLocationId(),
                dto.getMainLocationName(), dto.getSubLocationName(),
                dto.getTablePrefix(), dto.getNoOfTables(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getConfigTableLocationId());
    }

    public int softDelete(int configTableLocationId) {
        String sql = "UPDATE pos_con_config_table_location_tb SET status = 0 WHERE config_table_location_id = ?";
        return jdbcTemplate.update(sql, configTableLocationId);
    }
}