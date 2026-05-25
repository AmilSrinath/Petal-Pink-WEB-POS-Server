package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.GrnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        String sql = "SELECT * FROM pos_inv_grn_tb WHERE status != 0";
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
}