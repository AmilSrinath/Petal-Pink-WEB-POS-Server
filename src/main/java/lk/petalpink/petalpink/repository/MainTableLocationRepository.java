package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.MainTableLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MainTableLocationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(MainTableLocationDTO dto) {
        String sql = "INSERT INTO pos_con_main_table_location_tb " +
                "(main_location_name, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainLocationName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<MainTableLocationDTO> findAll() {
        String sql = "SELECT * FROM pos_con_main_table_location_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MainTableLocationDTO.class));
    }

    public int update(MainTableLocationDTO dto) {
        String sql = "UPDATE pos_con_main_table_location_tb " +
                "SET main_location_name=?, image_path=?, status=?, user_id=?, visible=? " +
                "WHERE main_table_location_id=?";
        return jdbcTemplate.update(sql,
                dto.getMainLocationName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getMainTableLocationId());
    }

    public int softDelete(int mainTableLocationId) {
        String sql = "UPDATE pos_con_main_table_location_tb SET status = 0 WHERE main_table_location_id = ?";
        return jdbcTemplate.update(sql, mainTableLocationId);
    }
}