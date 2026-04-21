package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.SubItemCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SubItemCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<SubItemCategoryDTO> rowMapper = new RowMapper<SubItemCategoryDTO>() {
        @Override
        public SubItemCategoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            SubItemCategoryDTO dto = new SubItemCategoryDTO();
            dto.setSubItemCategoryId(rs.getInt("sub_item_category_id"));
            dto.setMainItemCategoryId(rs.getObject("main_item_category_id") != null ? rs.getInt("main_item_category_id") : null);
            dto.setMainItemCategoryName(rs.getString("main_item_category_name"));
            dto.setSubItemCategoryName(rs.getString("sub_item_category_name"));
            dto.setImagePath(rs.getString("image_path"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
            dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
            return dto;
        }
    };

    // GET ALL
    public List<SubItemCategoryDTO> getAll() {
        String sql = "SELECT * FROM pos_sub_item_category_tb";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // GET BY ID
    public SubItemCategoryDTO getById(int id) {
        String sql = "SELECT * FROM pos_sub_item_category_tb WHERE sub_item_category_id = ?";
        List<SubItemCategoryDTO> result = jdbcTemplate.query(sql, rowMapper, id);
        return result.isEmpty() ? null : result.get(0);
    }

    // CREATE
    public int save(SubItemCategoryDTO dto) {
        String sql = "INSERT INTO pos_sub_item_category_tb " +
                "(main_item_category_id, main_item_category_name, sub_item_category_name, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryId(),
                dto.getMainItemCategoryName(),
                dto.getSubItemCategoryName(),
                dto.getImagePath(),
                dto.getStatus(),
                dto.getUserId(),
                dto.getVisible()
        );
    }

    // UPDATE
    public int update(int id, SubItemCategoryDTO dto) {
        String sql = "UPDATE pos_sub_item_category_tb SET " +
                "main_item_category_id = ?, main_item_category_name = ?, sub_item_category_name = ?, " +
                "image_path = ?, status = ?, user_id = ?, visible = ? " +
                "WHERE sub_item_category_id = ?";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryId(),
                dto.getMainItemCategoryName(),
                dto.getSubItemCategoryName(),
                dto.getImagePath(),
                dto.getStatus(),
                dto.getUserId(),
                dto.getVisible(),
                id
        );
    }

    // DELETE
    public int delete(int id) {
        String sql = "DELETE FROM pos_sub_item_category_tb WHERE sub_item_category_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}