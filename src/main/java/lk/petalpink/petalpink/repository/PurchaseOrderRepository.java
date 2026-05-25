package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.PurchaseOrderDTO;
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
public class PurchaseOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Inserts a new purchase order and returns the auto-generated po_id.
     * Returns -1 if the insert did not affect any rows.
     */
    public int save(PurchaseOrderDTO dto) {
        String sql = "INSERT INTO pos_inv_purchase_order_tb " +
                "(po_prefix, po_code, po_code_prefix, supplier_id, supplier_name, po_date, expected_date, " +
                "total_price, payment_type, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1,  dto.getPoPrefix());
            ps.setObject(2,  dto.getPoCode());
            ps.setObject(3,  dto.getPoCodePrefix());
            ps.setObject(4,  dto.getSupplierId());
            ps.setObject(5,  dto.getSupplierName());
            ps.setObject(6,  dto.getPoDate());
            ps.setObject(7,  dto.getExpectedDate());
            ps.setObject(8,  dto.getTotalPrice());
            ps.setObject(9,  dto.getPaymentType());
            ps.setObject(10, dto.getStatus());
            ps.setObject(11, dto.getUserId());
            ps.setObject(12, dto.getVisible());
            return ps;
        }, keyHolder);

        if (rows > 0 && keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue(); // return the new po_id
        }
        return -1;
    }

    public List<PurchaseOrderDTO> findAll() {
        String sql = "SELECT * FROM pos_inv_purchase_order_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PurchaseOrderDTO.class));
    }

    public int update(PurchaseOrderDTO dto) {
        String sql = "UPDATE pos_inv_purchase_order_tb " +
                "SET po_prefix=?, po_code=?, po_code_prefix=?, supplier_id=?, supplier_name=?, po_date=?, " +
                "expected_date=?, total_price=?, payment_type=?, status=?, user_id=?, visible=? " +
                "WHERE po_id=?";
        return jdbcTemplate.update(sql,
                dto.getPoPrefix(), dto.getPoCode(), dto.getPoCodePrefix(),
                dto.getSupplierId(), dto.getSupplierName(), dto.getPoDate(),
                dto.getExpectedDate(), dto.getTotalPrice(), dto.getPaymentType(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getPoId());
    }

    public int softDelete(int poId) {
        String sql = "UPDATE pos_inv_purchase_order_tb SET status = 0 WHERE po_id = ?";
        return jdbcTemplate.update(sql, poId);
    }
}