package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.NotPaidPaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class NotPaidPaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Returns all payments where payment_status = 9 (Not Paid)
     * for deliveries that are in Delivered status (status_id = 5),
     * filtered by the payment created_date range.
     */
    public List<NotPaidPaymentDTO> findNotPaidByDateRange(LocalDate from, LocalDate to) {
        String sql =
                "SELECT p.payment_id, " +
                        "       o.order_id, " +
                        "       o.customer_id, " +
                        "       c.customer_number, " +
                        "       d.order_code, " +
                        "       p.total_amount, " +
                        "       o.total_order_price, " +
                        "       p.payment_status, " +
                        "       o.delivery_order_id, " +
                        "       p.created_date " +
                        "FROM pos_payment_tb p " +
                        "INNER JOIN pos_main_order_tb o          ON p.order_id = o.order_id " +
                        "INNER JOIN pos_main_delivery_order_tb d ON d.delivery_id = o.delivery_order_id " +
                        "INNER JOIN pos_main_customer_tb c       ON o.customer_id = c.customer_id " +
                        "WHERE DATE(p.created_date) BETWEEN ? AND ? " +
                        "  AND p.status_id = 9 " +
                        "  AND d.status_id = 5 " +
                        "ORDER BY p.created_date DESC";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(NotPaidPaymentDTO.class),
                from, to
        );
    }
}