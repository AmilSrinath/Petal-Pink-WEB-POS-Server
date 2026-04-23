package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(CategoryDTO dto) {
        String sql = "INSERT INTO pos_main_item_category_tb " +
                "(main_item_category_name, image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<CategoryDTO> findAll() {
        String sql = "SELECT * FROM pos_main_item_category_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoryDTO.class));
    }

    public int update(CategoryDTO dto) {
        String sql = "UPDATE pos_main_item_category_tb " +
                "SET main_item_category_name=?, image_path=?, status=?, user_id=?, visible=?, edited_by=? " +
                "WHERE main_item_category_id=?";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryName(), dto.getImagePath(), dto.getStatus(),
                dto.getUserId(), dto.getVisible(), dto.getEditedBy(),
                dto.getMainItemCategoryId());
    }

    public int softDelete(int mainItemCategoryId) {
        String sql = "UPDATE pos_main_item_category_tb SET status = 0 WHERE main_item_category_id = ?";
        return jdbcTemplate.update(sql, mainItemCategoryId);
    }
}