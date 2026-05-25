package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.PurchaseOrderDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseOrderDetailsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(PurchaseOrderDetailsDTO dto) {
        String sql = "INSERT INTO pos_inv_purchase_order_details_tb " +
                "(po_id, item_id, item_name, qty, expected_price, last_grn_price, total_price, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getPoId(), dto.getItemId(), dto.getItemName(),
                dto.getQty(), dto.getExpectedPrice(), dto.getLastGrnPrice(),
                dto.getTotalPrice(), dto.getUserId());
    }

    public List<PurchaseOrderDetailsDTO> findAll() {
        String sql = "SELECT * FROM pos_inv_purchase_order_details_tb";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PurchaseOrderDetailsDTO.class));
    }

    public List<PurchaseOrderDetailsDTO> findByPoId(int poId) {
        String sql = "SELECT * FROM pos_inv_purchase_order_details_tb WHERE po_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PurchaseOrderDetailsDTO.class), poId);
    }

    public int update(PurchaseOrderDetailsDTO dto) {
        String sql = "UPDATE pos_inv_purchase_order_details_tb " +
                "SET po_id=?, item_id=?, item_name=?, qty=?, expected_price=?, last_grn_price=?, total_price=?, user_id=? " +
                "WHERE po_details_id=?";
        return jdbcTemplate.update(sql,
                dto.getPoId(), dto.getItemId(), dto.getItemName(),
                dto.getQty(), dto.getExpectedPrice(), dto.getLastGrnPrice(),
                dto.getTotalPrice(), dto.getUserId(),
                dto.getPoDetailsId());
    }

    public int delete(int poDetailsId) {
        String sql = "DELETE FROM pos_inv_purchase_order_details_tb WHERE po_details_id = ?";
        return jdbcTemplate.update(sql, poDetailsId);
    }
}