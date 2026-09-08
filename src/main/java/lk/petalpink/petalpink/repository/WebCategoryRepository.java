package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebMainCategoryDTO;
import lk.petalpink.petalpink.dto.website.WebSubCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class WebCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    public int saveMainCategory(String mainCategoryName, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO web_petal_pink_main_category_tb (main_category_name, created_data, edited_date, user_id, status) VALUES (?, NOW(), NOW(), ?, 1)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mainCategoryName);
            ps.setInt(2, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public List<WebMainCategoryDTO> getAllMainCategories() {
        return jdbcTemplate.query(
                "SELECT * FROM web_petal_pink_main_category_tb WHERE status = 1 ORDER BY created_data DESC",
                new BeanPropertyRowMapper<>(WebMainCategoryDTO.class));
    }

    public WebMainCategoryDTO getMainCategoryById(Integer mainCategoryId) {
        List<WebMainCategoryDTO> result = jdbcTemplate.query(
                "SELECT * FROM web_petal_pink_main_category_tb WHERE main_category_id = ? AND status = 1",
                new BeanPropertyRowMapper<>(WebMainCategoryDTO.class), mainCategoryId);
        return result.isEmpty() ? null : result.get(0);
    }

    public int updateMainCategory(Integer mainCategoryId, String mainCategoryName, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_main_category_tb SET main_category_name = ?, edited_date = NOW(), user_id = ? WHERE main_category_id = ? AND status = 1",
                mainCategoryName, userId, mainCategoryId);
    }

    public int deleteMainCategory(Integer mainCategoryId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_main_category_tb SET status = 0 WHERE main_category_id = ?",
                mainCategoryId);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SUB CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    public int saveSubCategory(String subCategoryName, Integer mainCategoryId, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO web_petal_pink_sub_category_tb (sub_category_name, main_category_id, created_date, edited_date, user_id, status) VALUES (?, ?, NOW(), NOW(), ?, 1)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, subCategoryName);
            ps.setInt(2, mainCategoryId);
            ps.setInt(3, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public List<WebSubCategoryDTO> getAllSubCategories() {
        return jdbcTemplate.query(
                "SELECT * FROM web_petal_pink_sub_category_tb WHERE status = 1 ORDER BY created_date DESC",
                new BeanPropertyRowMapper<>(WebSubCategoryDTO.class));
    }

    public List<WebSubCategoryDTO> getSubCategoriesByMainCategoryId(Integer mainCategoryId) {
        return jdbcTemplate.query(
                "SELECT * FROM web_petal_pink_sub_category_tb WHERE main_category_id = ? AND status = 1 ORDER BY created_date DESC",
                new BeanPropertyRowMapper<>(WebSubCategoryDTO.class), mainCategoryId);
    }

    public WebSubCategoryDTO getSubCategoryById(Integer subCategoryId) {
        List<WebSubCategoryDTO> result = jdbcTemplate.query(
                "SELECT * FROM web_petal_pink_sub_category_tb WHERE sub_category_id = ? AND status = 1",
                new BeanPropertyRowMapper<>(WebSubCategoryDTO.class), subCategoryId);
        return result.isEmpty() ? null : result.get(0);
    }

    public int updateSubCategory(Integer subCategoryId, String subCategoryName, Integer mainCategoryId, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_sub_category_tb SET sub_category_name = ?, main_category_id = ?, edited_date = NOW(), user_id = ? WHERE sub_category_id = ? AND status = 1",
                subCategoryName, mainCategoryId, userId, subCategoryId);
    }

    public int deleteSubCategory(Integer subCategoryId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_sub_category_tb SET status = 0 WHERE sub_category_id = ?",
                subCategoryId);
    }
}