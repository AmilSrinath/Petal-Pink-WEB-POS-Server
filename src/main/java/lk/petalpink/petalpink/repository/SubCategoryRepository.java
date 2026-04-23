package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.SubCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(SubCategoryDTO dto) {
        String sql = "INSERT INTO pos_sub_item_category_tb " +
                "(main_item_category_id, main_item_category_name, sub_item_category_name, " +
                "image_path, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryId(), dto.getMainItemCategoryName(),
                dto.getSubItemCategoryName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<SubCategoryDTO> findAll() {
        String sql = "SELECT * FROM pos_sub_item_category_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SubCategoryDTO.class));
    }

    public List<SubCategoryDTO> findByMainCategory(int mainItemCategoryId) {
        String sql = "SELECT * FROM pos_sub_item_category_tb WHERE status != 0 AND main_item_category_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SubCategoryDTO.class), mainItemCategoryId);
    }

    public int update(SubCategoryDTO dto) {
        String sql = "UPDATE pos_sub_item_category_tb " +
                "SET main_item_category_id=?, main_item_category_name=?, sub_item_category_name=?, " +
                "image_path=?, status=?, user_id=?, visible=? " +
                "WHERE sub_item_category_id=?";
        return jdbcTemplate.update(sql,
                dto.getMainItemCategoryId(), dto.getMainItemCategoryName(),
                dto.getSubItemCategoryName(), dto.getImagePath(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getSubItemCategoryId());
    }

    public int softDelete(int subItemCategoryId) {
        String sql = "UPDATE pos_sub_item_category_tb SET status = 0 WHERE sub_item_category_id = ?";
        return jdbcTemplate.update(sql, subItemCategoryId);
    }
}