package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.PaymentTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PaymentTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(PaymentTypeDTO dto) {
        String sql = "INSERT INTO pos_main_payment_types_tb (payment_type, status, user_id, visible) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getPaymentType(), dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<PaymentTypeDTO> findAll() {
        String sql = "SELECT * FROM pos_main_payment_types_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PaymentTypeDTO.class));
    }

    public int update(PaymentTypeDTO dto) {
        String sql = "UPDATE pos_main_payment_types_tb SET payment_type=?, status=?, user_id=?, visible=? WHERE payment_type_id=?";
        return jdbcTemplate.update(sql, dto.getPaymentType(), dto.getStatus(), dto.getUserId(), dto.getVisible(), dto.getPaymentTypeId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_main_payment_types_tb SET status = 0 WHERE payment_type_id = ?", id);
    }
}