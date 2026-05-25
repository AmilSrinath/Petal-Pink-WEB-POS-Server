package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.StockCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StockCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(StockCategoryDTO dto) {
        String sql = "INSERT INTO pos_inv_stock_location_tb " +
                "(stock_name, location, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getStockName(), dto.getLocation(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<StockCategoryDTO> findAll() {
        String sql = "SELECT * FROM pos_inv_stock_location_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockCategoryDTO.class));
    }

    public int update(StockCategoryDTO dto) {
        String sql = "UPDATE pos_inv_stock_location_tb " +
                "SET stock_name=?, location=?, status=?, user_id=?, visible=? " +
                "WHERE stock_category_id=?";
        return jdbcTemplate.update(sql,
                dto.getStockName(), dto.getLocation(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getStockCategoryId());
    }

    public int softDelete(int stockCategoryId) {
        String sql = "UPDATE pos_inv_stock_location_tb SET status = 0 WHERE stock_category_id = ?";
        return jdbcTemplate.update(sql, stockCategoryId);
    }
}