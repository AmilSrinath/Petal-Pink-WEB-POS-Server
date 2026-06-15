package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.BatchRegDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class BatchRegRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveAndGetId(BatchRegDTO dto) {
        String sql = "INSERT INTO pos_inv_batch_reg " +
                "(batch_reg_prefix, batch_reg_code, is_active, user_id, remark) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getBatchRegPrefix() != null ? dto.getBatchRegPrefix() : "BA");
            ps.setObject(2, dto.getBatchRegCode());
            ps.setObject(3, dto.getIsActive());
            ps.setObject(4, dto.getUserId());
            ps.setString(5, dto.getRemark());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  BATCH EXHAUSTION CHECK
    //
    //  Called after every minus-qty write (sale, production, adjustment).
    //  Computes the net remaining qty for the given reg_id across ALL stock
    //  detail rows.  If the net has dropped to 0 (or gone negative due to any
    //  manual correction), the batch is marked is_active = 0 so it is no
    //  longer offered in FIFO queries or the UI.
    //
    //  A NULL reg_id is silently skipped — some detail rows (e.g. direct
    //  master-stock deductions) legitimately have no batch.
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Sets is_active = 0 on pos_inv_batch_reg for the given reg_id if the
     * net remaining qty (SUM plus_qty - SUM minus_qty) across
     * pos_inv_stock_details_tb is ≤ 0.
     *
     * @param regId the batch_reg_id to check; does nothing when null
     */
    public void deactivateBatchIfExhausted(Integer regId) {
        if (regId == null) return;

        String sql = """
            UPDATE pos_inv_batch_reg br
            SET br.is_active = 0,
                br.edited_date = CURRENT_TIMESTAMP
            WHERE br.reg_id = ?
              AND br.is_active = 1
              AND (
                    SELECT COALESCE(SUM(sd.plus_qty), 0) - COALESCE(SUM(sd.minus_qty), 0)
                    FROM pos_inv_stock_details_tb sd
                    WHERE sd.batch_reg_id = br.reg_id
                      AND sd.status = 1
                  ) <= 0
            """;

        jdbcTemplate.update(sql, regId);
    }
}