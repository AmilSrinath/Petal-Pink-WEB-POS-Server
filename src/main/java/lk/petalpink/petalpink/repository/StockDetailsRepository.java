package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.StockDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StockDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveAll(List<StockDetailsDTO> details) {
        String sql = "INSERT INTO pos_inv_stock_details_tb " +
                "(stock_location_id, batch_reg_id, stock_adj_type_id, stock_id, stock_name, " +
                "cost_price, last_grn_price, plus_qty, minus_qty, is_init_qty, " +
                "status, visible, created_date, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, details, details.size(), (ps, dto) -> {
            ps.setInt(1,    dto.getStockLocationId());
            ps.setInt(2,    dto.getBatchRegId());
            ps.setInt(3,    dto.getStockAdjTypeId());
            ps.setInt(4,    dto.getStockId());          // ✅ NEW
            ps.setString(5, dto.getStockName());
            ps.setDouble(6, dto.getCostPrice());
            ps.setDouble(7, dto.getLastGrnPrice());
            ps.setDouble(8, dto.getPlusQty());
            ps.setDouble(9, dto.getMinusQty());
            ps.setInt(10,   dto.getIsInitQty());
            ps.setInt(11,   dto.getStatus());
            ps.setInt(12,   dto.getVisible());
            ps.setObject(13, dto.getCreatedDate());
            ps.setInt(14,   dto.getUserId());
        });
    }

    public void insertSaleDetail(
            Integer stockId,              // ✅ NEW parameter
            Integer batchRegId,
            Integer stockAdjTypeId,
            String stockName,
            Double costPrice,
            Double lastGrnPrice,
            Double minusQty,
            Integer stockUnitType,
            Integer userId) {

        String sql = """
            INSERT INTO pos_inv_stock_details_tb
                (stock_location_id, batch_reg_id, stock_adj_type_id, stock_id, stock_name,
                 cost_price, last_grn_price, plus_qty, minus_qty,
                 is_init_qty, status, visible, created_date, edited_date, user_id, stock_unit_type)
            VALUES (1, ?, ?, ?, ?, ?, ?, 0, ?, 0, 1, 1, NOW(), NOW(), ?, ?)
            """;

        jdbcTemplate.update(sql,
                batchRegId,
                stockAdjTypeId,
                stockId,          // ✅ NEW
                stockName,
                costPrice,
                lastGrnPrice,
                minusQty,
                userId,
                stockUnitType
        );
    }
}