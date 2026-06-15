package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.FifoBatchDTO;
import lk.petalpink.petalpink.dto.StockDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StockDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Injected to trigger is_active = 0 after every minus-qty write
    @Autowired
    private BatchRegRepository batchRegRepository;

    // ─────────────────────────────────────────────────────────────────────────
    //  FIFO HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns all un-exhausted batches for a given stock in FIFO order
     * (oldest created_date first).
     */
    public List<FifoBatchDTO> findFifoBatchesForStock(int stockId) {
        String sql = """
            SELECT
                d.batch_reg_id,
                d.stock_details_id,
                d.plus_qty,
                COALESCE(
                    (SELECT SUM(m.minus_qty)
                     FROM pos_inv_stock_details_tb m
                     WHERE m.stock_id     = d.stock_id
                       AND m.batch_reg_id = d.batch_reg_id
                       AND m.minus_qty    > 0
                       AND m.status       = 1
                    ), 0
                )                                                AS consumed_qty,
                (d.plus_qty - COALESCE(
                    (SELECT SUM(m.minus_qty)
                     FROM pos_inv_stock_details_tb m
                     WHERE m.stock_id     = d.stock_id
                       AND m.batch_reg_id = d.batch_reg_id
                       AND m.minus_qty    > 0
                       AND m.status       = 1
                    ), 0)
                )                                                AS available_qty,
                d.cost_price,
                d.last_grn_price
            FROM pos_inv_stock_details_tb d
            WHERE d.stock_id      = ?
              AND d.batch_reg_id IS NOT NULL
              AND d.plus_qty     > 0
              AND d.status       = 1
            HAVING available_qty > 0
            ORDER BY d.created_date ASC, d.stock_details_id ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FifoBatchDTO b = new FifoBatchDTO();
            b.setBatchRegId(rs.getInt("batch_reg_id"));
            b.setStockDetailsId(rs.getInt("stock_details_id"));
            b.setPlusQty(rs.getDouble("plus_qty"));
            b.setConsumedQty(rs.getDouble("consumed_qty"));
            b.setAvailableQty(rs.getDouble("available_qty"));
            b.setCostPrice(rs.getDouble("cost_price"));
            b.setLastGrnPrice(rs.getDouble("last_grn_price"));
            return b;
        }, stockId);
    }

    /**
     * Creates a new batch registration row in pos_inv_batch_reg and returns
     * its generated reg_id. Used to stamp a batch onto every production output.
     */
    public int createBatchReg(int userId) {
        String sql = """
            INSERT INTO pos_inv_batch_reg
                (batch_reg_prefix, is_active, user_id, remark)
            VALUES ('PRD', 1, ?, 'Auto-created for production output')
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  BULK SAVE  (used by ProductionService)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Batch-save a list of stock detail rows, then check every distinct
     * batch_reg_id that received a minus-qty row and deactivate any that are
     * now fully exhausted.
     */
    public void saveAll(List<StockDetailsDTO> details) {
        String sql = "INSERT INTO pos_inv_stock_details_tb " +
                "(stock_location_id, batch_reg_id, stock_adj_type_id, stock_id, stock_name, " +
                "cost_price, last_grn_price, plus_qty, minus_qty, is_init_qty, " +
                "status, visible, created_date, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, details, details.size(), (ps, dto) -> {
            ps.setInt   (1,  dto.getStockLocationId());
            if (dto.getBatchRegId() != null) ps.setInt(2, dto.getBatchRegId());
            else                              ps.setNull(2, java.sql.Types.INTEGER);
            ps.setInt   (3,  dto.getStockAdjTypeId());
            ps.setInt   (4,  dto.getStockId());
            ps.setString(5,  dto.getStockName());
            ps.setDouble(6,  dto.getCostPrice());
            ps.setDouble(7,  dto.getLastGrnPrice());
            ps.setDouble(8,  dto.getPlusQty());
            ps.setDouble(9,  dto.getMinusQty());
            ps.setInt   (10, dto.getIsInitQty());
            ps.setInt   (11, dto.getStatus());
            ps.setInt   (12, dto.getVisible());
            ps.setObject(13, dto.getCreatedDate());
            ps.setInt   (14, dto.getUserId());
        });

        // After all rows are inserted, check every batch that was *consumed*
        // (minus_qty > 0) and flip is_active = 0 if it is now fully exhausted.
        details.stream()
                .filter(d -> d.getMinusQty() != null && d.getMinusQty() > 0
                        && d.getBatchRegId() != null)
                .map(StockDetailsDTO::getBatchRegId)
                .distinct()
                .forEach(batchRegRepository::deactivateBatchIfExhausted);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SINGLE SALE INSERT  (used by OrderStockService)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Inserts one sale/deduction detail row, then checks whether the batch
     * is now exhausted and deactivates it if so.
     */
    public void insertSaleDetail(
            Integer stockId,
            Integer batchRegId,
            Integer stockAdjTypeId,
            String  stockName,
            Double  costPrice,
            Double  lastGrnPrice,
            Double  minusQty,
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
                stockId,
                stockName,
                costPrice,
                lastGrnPrice,
                minusQty,
                userId,
                stockUnitType
        );

        // Deactivate the batch in pos_inv_batch_reg if it is now fully consumed
        batchRegRepository.deactivateBatchIfExhausted(batchRegId);
    }
}