package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.GrnDTO;
import lk.petalpink.petalpink.dto.GrnItemDetailDTO;
import lk.petalpink.petalpink.dto.GrnItemUpdateDTO;
import lk.petalpink.petalpink.dto.GrnUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class GrnRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(GrnDTO dto) {
        String sql = "INSERT INTO pos_inv_grn_tb " +
                "(invoice_no, supplier_id, total_price, total_discount, created_Date, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getInvoiceNo(), dto.getSupplierId(), dto.getTotalPrice(),
                dto.getTotalDiscount(), dto.getCreatedDate(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<GrnDTO> findAll() {
        String sql = "SELECT g.*, s.company_name AS supplier_name, loc.stock_name AS stock_location_name " +
                "FROM pos_inv_grn_tb g " +
                "LEFT JOIN pos_inv_supplier_tb s ON g.supplier_id = s.supplier_id " +
                "LEFT JOIN pos_inv_stock_location_tb loc ON g.stock_location_id = loc.stock_category_id " +
                "WHERE g.status != 0 " +
                "ORDER BY g.grn_id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GrnDTO.class));
    }

    public int update(GrnDTO dto) {
        String sql = "UPDATE pos_inv_grn_tb " +
                "SET invoice_no=?, supplier_id=?, total_price=?, total_discount=?, created_Date=?, status=?, user_id=?, visible=? " +
                "WHERE grn_id=?";
        return jdbcTemplate.update(sql,
                dto.getInvoiceNo(), dto.getSupplierId(), dto.getTotalPrice(),
                dto.getTotalDiscount(), dto.getCreatedDate(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getGrnId());
    }

    public int softDelete(int grnId) {
        String sql = "UPDATE pos_inv_grn_tb SET status = 0 WHERE grn_id = ?";
        return jdbcTemplate.update(sql, grnId);
    }

    public int saveAndGetId(GrnDTO dto) {
        String sql = "INSERT INTO pos_inv_grn_tb " +
                "(invoice_no, supplier_id, total_price, total_discount, created_Date, " +
                "status, stock_location_id, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getInvoiceNo());
            ps.setInt(2, dto.getSupplierId());
            ps.setDouble(3, dto.getTotalPrice());
            ps.setDouble(4, dto.getTotalDiscount());
            ps.setObject(5, dto.getCreatedDate());
            ps.setInt(6, dto.getStatus());
            ps.setObject(7, dto.getStockLocationId());  // ← NEW
            ps.setInt(8, dto.getUserId());
            ps.setInt(9, dto.getVisible());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<GrnItemDetailDTO> findGrnItems(int grnId) {
        String sql = """
            SELECT 
                bp.profile_id,
                bp.reg_id,
                bp.item_id,
                bp.grn_id,
                bp.cost_price,
                bp.retail_price,
                bp.whole_sale_price,
                bp.exp_date,
                bp.is_release_for_sell,
                bp.po_no,
                bp.unit_type,
                bp.remark,
                COALESCE(i.item_name, 'Unknown Item') AS item_name,
                COALESCE(i.item_code_prefix, '') AS item_code_prefix,
                COALESCE(i.item_bar_code, '') AS item_bar_code,
                COALESCE(u.unit_type, '') AS unit_type_name,
                COALESCE(MAX(sd.plus_qty), 0.0) AS quantity
            FROM pos_inv_batch_profile bp
            LEFT JOIN pos_main_item_tb i ON bp.item_id = i.item_id
            LEFT JOIN pos_main_unit_type_tb u ON bp.unit_type = u.unit_type_id
            LEFT JOIN pos_inv_stock_tb s ON s.item_id = bp.item_id
            LEFT JOIN pos_inv_stock_details_tb sd ON sd.batch_reg_id = bp.reg_id 
                AND sd.stock_id = s.stock_id
                AND sd.plus_qty > 0
            WHERE bp.grn_id = ?
            GROUP BY bp.profile_id, bp.reg_id, bp.item_id, bp.grn_id, bp.cost_price, 
                     bp.retail_price, bp.whole_sale_price, bp.exp_date, bp.is_release_for_sell, 
                     bp.po_no, bp.unit_type, bp.remark, i.item_name, i.item_code_prefix, 
                     i.item_bar_code, u.unit_type
            ORDER BY bp.profile_id ASC
            """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GrnItemDetailDTO.class), grnId);
    }

    public String getNextInvoiceNo() {
        String sql = "SELECT invoice_no FROM pos_inv_grn_tb WHERE invoice_no IS NOT NULL AND invoice_no != '' ORDER BY grn_id DESC LIMIT 100";
        List<String> list = jdbcTemplate.queryForList(sql, String.class);

        int maxSeq = 0;
        for (String inv : list) {
            if (inv != null) {
                String digits = inv.replaceAll("\\D+", "");
                if (!digits.isEmpty()) {
                    try {
                        int num = Integer.parseInt(digits);
                        if (num > maxSeq && num < 1000000) {
                            maxSeq = num;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        int next = maxSeq + 1;
        return String.format("INV-%04d", next);
    }

    public int updateGrnHeader(GrnUpdateRequestDTO dto) {
        String sql = "UPDATE pos_inv_grn_tb " +
                "SET invoice_no = ?, supplier_id = ?, stock_location_id = ?, " +
                "    created_Date = ?, total_price = ?, total_discount = ?, user_id = ? " +
                "WHERE grn_id = ?";
        return jdbcTemplate.update(sql,
                dto.getInvoiceNo(),
                dto.getSupplierId(),
                dto.getStockLocationId(),
                dto.getCreatedDate(),
                dto.getTotalPrice(),
                dto.getTotalDiscount(),
                dto.getUserId(),
                dto.getGrnId()
        );
    }

    public int updateBatchProfile(GrnItemUpdateDTO item) {
        String sql = "UPDATE pos_inv_batch_profile " +
                "SET cost_price = ?, retail_price = ?, whole_sale_price = ?, " +
                "    exp_date = ?, is_release_for_sell = ?, po_no = ?, unit_type = ?, remark = ? " +
                "WHERE profile_id = ?";
        return jdbcTemplate.update(sql,
                item.getCostPrice(),
                item.getRetailPrice(),
                item.getWholeSalePrice(),
                item.getExpDate(),
                item.getIsReleaseForSell(),
                item.getPoNo(),
                (item.getUnitType() != null && item.getUnitType() > 0) ? item.getUnitType() : 1,
                item.getRemark(),
                item.getProfileId()
        );
    }

    public Integer findBatchRegIdByGrnId(int grnId) {
        String sql = "SELECT reg_id FROM pos_inv_batch_profile WHERE grn_id = ? LIMIT 1";
        List<Integer> list = jdbcTemplate.queryForList(sql, Integer.class, grnId);
        return list.isEmpty() ? null : list.get(0);
    }

    public Double findCurrentPlusQty(int batchRegId, int stockId) {
        String sql = "SELECT plus_qty FROM pos_inv_stock_details_tb " +
                "WHERE batch_reg_id = ? AND stock_id = ? AND stock_adj_type_id = 1 AND plus_qty > 0 LIMIT 1";
        List<Double> list = jdbcTemplate.queryForList(sql, Double.class, batchRegId, stockId);
        return list.isEmpty() ? null : list.get(0);
    }

    public int updateStockDetail(int batchRegId, int stockId, double newQty, double costPrice, Integer locationId, LocalDate createdDate) {
        String sql = "UPDATE pos_inv_stock_details_tb " +
                "SET plus_qty = ?, cost_price = ?, last_grn_price = ?, stock_location_id = ?, created_date = ?, edited_date = NOW() " +
                "WHERE batch_reg_id = ? AND stock_id = ? AND stock_adj_type_id = 1 AND plus_qty > 0";
        return jdbcTemplate.update(sql, newQty, costPrice, costPrice, locationId, createdDate, batchRegId, stockId);
    }

    public int deactivateBatchProfile(int profileId) {
        String sql = "UPDATE pos_inv_batch_profile SET is_active = 0 WHERE profile_id = ?";
        return jdbcTemplate.update(sql, profileId);
    }

    public int removeStockDetail(int batchRegId, int stockId) {
        String sql = "UPDATE pos_inv_stock_details_tb SET status = 0, visible = 0, plus_qty = 0, edited_date = NOW() " +
                "WHERE batch_reg_id = ? AND stock_id = ? AND stock_adj_type_id = 1";
        return jdbcTemplate.update(sql, batchRegId, stockId);
    }
}