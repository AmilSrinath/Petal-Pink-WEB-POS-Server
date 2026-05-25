package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.BatchProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BatchProfileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(BatchProfileDTO dto) {
        String sql = "INSERT INTO pos_inv_batch_profile " +
                "(reg_id, item_id, grn_id, cost_price, retail_price, whole_sale_price, " +
                "exp_date, is_release_for_sell, po_no, unit_type, is_active, user_id, remark) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                dto.getRegId(),
                dto.getItemId(),
                dto.getGrnId(),
                dto.getCostPrice(),
                dto.getRetailPrice(),
                dto.getWholeSalePrice(),
                dto.getExpDate(),
                dto.getIsReleaseForSell(),
                dto.getPoNo(),
                dto.getUnitType(),
                dto.getIsActive(),
                dto.getUserId(),
                dto.getRemark()
        );
    }

    public void saveAll(List<BatchProfileDTO> items) {
        items.forEach(this::save);
    }

    /** Find a single active or inactive profile by its primary key. */
    public BatchProfileDTO findById(int profileId) {
        String sql = "SELECT * FROM pos_inv_batch_profile WHERE profile_id = ?";
        List<BatchProfileDTO> results = jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(BatchProfileDTO.class), profileId
        );
        return results.isEmpty() ? null : results.get(0);
    }

    /** Return all active batch profiles for a given item. */
    public List<BatchProfileDTO> findActiveByItemId(int itemId) {
        String sql = "SELECT * FROM pos_inv_batch_profile WHERE item_id = ? AND is_active = 1";
        return jdbcTemplate.query(
                sql, new BeanPropertyRowMapper<>(BatchProfileDTO.class), itemId
        );
    }

    /** Soft-deactivate a batch profile after merging. */
    public int deactivate(int profileId) {
        String sql = "UPDATE pos_inv_batch_profile SET is_active = 0 WHERE profile_id = ?";
        return jdbcTemplate.update(sql, profileId);
    }

    public List<BatchProfileDTO> findActiveBatchesFIFO(int itemId) {
        String sql = """
                SELECT
                    bp.profile_id,
                    bp.reg_id,
                    bp.item_id,
                    bp.cost_price,
                    bp.retail_price,
                    bp.unit_type,
                    COALESCE(SUM(sd.plus_qty), 0) - COALESCE(SUM(sd.minus_qty), 0) AS plus_qty
                FROM pos_inv_batch_profile bp
                LEFT JOIN pos_inv_stock_details_tb sd
                    ON sd.batch_reg_id = bp.reg_id
                   AND sd.status = 1
                   AND sd.visible = 1
                WHERE bp.item_id       = ?
                  AND bp.is_active     = 1
                  AND bp.is_release_for_sell = 1
                GROUP BY bp.profile_id, bp.reg_id, bp.item_id,
                         bp.cost_price, bp.retail_price, bp.unit_type
                HAVING plus_qty > 0
                ORDER BY bp.created_date ASC
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BatchProfileDTO.class), itemId);
    }
}