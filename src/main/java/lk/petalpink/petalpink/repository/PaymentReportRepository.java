package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.PaymentReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PaymentReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PaymentReportDTO> findByDateRange(LocalDate from, LocalDate to) {
        String sql = "SELECT p.payment_id, p.cod, p.total_amount, p.payment_status, " +
                "o.order_id, o.customer_id, o.delivery_order_id, o.bill_no, o.sub_total_price, " +
                "o.delivery_fee, o.total_order_price, o.payment_type_id, " +
                "o.created_Date, o.remark, o.is_print, p.status_id, d.order_code, " +
                "d.status_id AS delivery_status_id, c.customer_number " +
                "FROM pos_payment_tb p " +
                "INNER JOIN pos_main_order_tb o ON p.order_id = o.order_id " +
                "INNER JOIN pos_main_delivery_order_tb d ON d.delivery_id = o.delivery_order_id " +
                "INNER JOIN pos_main_customer_tb c ON o.customer_id = c.customer_id " +
                "WHERE DATE(p.created_Date) BETWEEN ? AND ? " +
                "AND d.status_id IN (5)";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PaymentReportDTO.class), from, to);
    }

    public int updatePaymentStatusByOrderId(Integer orderId, Integer statusId) {
        String sql = "UPDATE pos_payment_tb SET status_id = ? WHERE order_id = ?";
        return jdbcTemplate.update(sql, statusId, orderId);
    }
}