package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.StockAdjTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StockAdjTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(StockAdjTypeDTO dto) {
        String sql = "INSERT INTO stock_adj_type_tb " +
                "(stock_adj_type_name, is_stock_add, user_id, status) " +
                "VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getStockAdjTypeName(),
                dto.getIsStockAdd(),
                dto.getUserId(),
                dto.getStatus());
    }

    public List<StockAdjTypeDTO> findAll() {
        String sql = "SELECT * FROM stock_adj_type_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(StockAdjTypeDTO.class));
    }

    public int update(StockAdjTypeDTO dto) {
        String sql = "UPDATE stock_adj_type_tb " +
                "SET stock_adj_type_name=?, is_stock_add=?, user_id=?, edited_date=CURRENT_TIMESTAMP, status=? " +
                "WHERE stock_adj_type_id=?";
        return jdbcTemplate.update(sql,
                dto.getStockAdjTypeName(),
                dto.getIsStockAdd(),
                dto.getUserId(),
                dto.getStatus(),
                dto.getStockAdjTypeId());
    }

    public int softDelete(int stockAdjTypeId) {
        String sql = "UPDATE stock_adj_type_tb SET status = 0 WHERE stock_adj_type_id = ?";
        return jdbcTemplate.update(sql, stockAdjTypeId);
    }
}