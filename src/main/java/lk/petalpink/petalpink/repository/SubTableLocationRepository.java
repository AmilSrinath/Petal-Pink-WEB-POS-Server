package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.SubTableLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubTableLocationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(SubTableLocationDTO dto) {
        String sql = "INSERT INTO pos_con_sub_table_location_tb " +
                "(main_table_location_id, main_location_name, sub_location_name, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainTableLocationId(), dto.getMainLocationName(),
                dto.getSubLocationName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<SubTableLocationDTO> findAll() {
        String sql = "SELECT * FROM pos_con_sub_table_location_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SubTableLocationDTO.class));
    }

    public List<SubTableLocationDTO> findByMainLocationId(int mainTableLocationId) {
        String sql = "SELECT * FROM pos_con_sub_table_location_tb WHERE main_table_location_id = ? AND status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SubTableLocationDTO.class), mainTableLocationId);
    }

    public int update(SubTableLocationDTO dto) {
        String sql = "UPDATE pos_con_sub_table_location_tb " +
                "SET main_table_location_id=?, main_location_name=?, sub_location_name=?, image_path=?, status=?, user_id=?, visible=? " +
                "WHERE sub_table_location_id=?";
        return jdbcTemplate.update(sql,
                dto.getMainTableLocationId(), dto.getMainLocationName(),
                dto.getSubLocationName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getSubTableLocationId());
    }

    public int softDelete(int subTableLocationId) {
        String sql = "UPDATE pos_con_sub_table_location_tb SET status = 0 WHERE sub_table_location_id = ?";
        return jdbcTemplate.update(sql, subTableLocationId);
    }
}