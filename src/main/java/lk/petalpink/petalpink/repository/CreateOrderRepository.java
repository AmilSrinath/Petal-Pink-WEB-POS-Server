package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.dto.UpdateOrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class CreateOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // upsert customer by phone_one
    public Integer upsertCustomer(CreateOrderRequestDTO req) {
        String selectSql = "SELECT customer_id FROM pos_main_customer_tb WHERE phone_one = ?";

        List<Integer> existing = jdbcTemplate.query(selectSql,
                (rs, rowNum) -> rs.getInt("customer_id"), req.getPhoneOne());

        if (!existing.isEmpty()) {
            // update name and address if changed
            String updateSql =
                    "UPDATE pos_main_customer_tb " +
                            "SET customer_name = ?, address = ?, phone_two = ? " +
                            "WHERE phone_one = ?";
            jdbcTemplate.update(updateSql,
                    req.getCustomerName(),
                    req.getAddress(),
                    req.getPhoneTwo(),
                    req.getPhoneOne());
            return existing.get(0);
        } else {
            // insert new customer
            String insertSql =
                    "INSERT INTO pos_main_customer_tb " +
                            "(customer_name, phone_one, phone_two, address, status, user_id, visible) " +
                            "VALUES (?, ?, ?, ?, 1, ?, 1)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, req.getCustomerName());
                ps.setString(2, req.getPhoneOne());
                ps.setString(3, req.getPhoneTwo());
                ps.setString(4, req.getAddress());
                ps.setObject(5, req.getUserId());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue();
        }
    }

    // create delivery order — order_code empty, status=1 (pending), status_id=2
    public Integer createDeliveryOrder(CreateOrderRequestDTO req, Integer customerId) {
        String sql =
                "INSERT INTO pos_main_delivery_order_tb " +
                        "(customer_id, website_order_id, order_code, cod_amount, weight, remark, " +
                        "order_type, status, status_id, is_free_delivery, is_return, is_exchange, user_id) " +
                        "VALUES (?, ?, '', ?, ?, ?, ?, 1, 2, ?, 0, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setString(2, req.getWebsiteOrderId());
            ps.setBigDecimal(3, req.getCodAmount());
            ps.setString(4, req.getWeight());
            ps.setString(5, req.getRemark());
            ps.setString(6, req.getOrderType());
            ps.setObject(7, req.getIsFreeDelivery());
            ps.setObject(8, req.getIsExchange());
            ps.setObject(9, req.getUserId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // create order
    public Integer createOrder(CreateOrderRequestDTO req, Integer customerId, Integer deliveryOrderId) {
        String sql =
                "INSERT INTO pos_main_order_tb " +
                        "(customer_id, delivery_order_id, bill_no, sub_total_price, total_discount_price, " +
                        "delivery_fee, total_order_price, payment_type_id, user_id, status, visible, paid_amount) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 1, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, customerId);
            ps.setInt(2, deliveryOrderId);
            ps.setString(3, req.getBillNo());
            ps.setObject(4, req.getSubTotalPrice());
            ps.setObject(5, req.getTotalDiscountPrice());
            ps.setObject(6, req.getDeliveryFee());
            ps.setObject(7, req.getTotalOrderPrice());
            ps.setObject(8, req.getPaymentTypeId());
            ps.setObject(9, req.getUserId());
            ps.setObject(10, req.getPaidAmount());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // create order details
    public void createOrderDetails(List<OrderDetailItemDTO> items, Integer orderId, Integer userId) {
        String sql =
                "INSERT INTO pos_main_order_details_tb " +
                        "(order_id, item_id, item_bar_code, unit_type_id, printer_type_id, quantity, " +
                        "per_item_price, total_discount_price, total_item_price, remark, status, user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderDetailItemDTO item = items.get(i);
                ps.setInt(1, orderId);
                ps.setObject(2, item.getItemId());
                ps.setObject(3, item.getItemBarCode());
                ps.setObject(4, item.getUnitTypeId());
                ps.setObject(5, item.getPrinterTypeId());
                ps.setObject(6, item.getQuantity());
                ps.setObject(7, item.getPerItemPrice());
                ps.setObject(8, item.getTotalDiscountPrice());
                ps.setObject(9, item.getTotalItemPrice());
                ps.setString(10, item.getRemark());
                ps.setObject(11, userId);
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

    // create payment record with status_id = 9 (Not Paid)
    public void createPayment(Integer orderId, Integer customerId, CreateOrderRequestDTO req) {
        String sql =
                "INSERT INTO pos_payment_tb " +
                        "(order_id, customer_id, cod, total_amount, payment_status, created_Date, edited_Date, user_id, status_id) " +
                        "VALUES (?, ?, ?, ?, 0, NOW(), NOW(), ?, 9)";

        jdbcTemplate.update(sql,
                orderId,
                customerId,
                req.getCodAmount(),
                req.getTotalOrderPrice(),
                req.getUserId()
        );
    }

    // update order_code (tracking code) by delivery_id
    public void updateOrderCode(Integer deliveryId, String orderCode) {
        String sql = "UPDATE pos_main_delivery_order_tb SET order_code = ? WHERE delivery_id = ?";
        jdbcTemplate.update(sql, orderCode, deliveryId);
    }

    public void updateOrder(UpdateOrderRequestDTO req) {

        // 1. upsert customer by phone_one
        String selectSql = "SELECT customer_id FROM pos_main_customer_tb WHERE phone_one = ?";
        List<Integer> ids = jdbcTemplate.query(selectSql,
                (rs, rowNum) -> rs.getInt("customer_id"), req.getPhoneOne());

        Integer customerId;
        if (!ids.isEmpty()) {
            customerId = ids.get(0);
            jdbcTemplate.update(
                    "UPDATE pos_main_customer_tb SET customer_name=?, phone_two=?, address=?, customer_number=? WHERE phone_one=?",
                    req.getCustomerName(), req.getPhoneTwo(), req.getAddress(), req.getCustomerNumber() ,req.getPhoneOne()
            );
        } else {
            jdbcTemplate.update(
                    "INSERT INTO pos_main_customer_tb (customer_name, customer_number, phone_one, phone_two, address) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    req.getCustomerName(), req.getCustomerNumber(),
                    req.getPhoneOne(), req.getPhoneTwo(), req.getAddress()
            );
            customerId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        }

        // 2. update delivery order
        jdbcTemplate.update(
                "UPDATE pos_main_delivery_order_tb SET " +
                        "customer_id=?, order_code=?, cod_amount=?, weight=?, remark=?, order_type=?, " +
                        "is_free_delivery=?, is_return=?, is_exchange=?, user_id=?, status_id=? " +
                        "WHERE delivery_id=?",
                customerId, req.getOrderCode(), req.getCodAmount(), req.getWeight(),
                req.getRemark(), req.getOrderType(), req.getIsFreeDelivery(),
                req.getIsReturn(), req.getIsExchange(), req.getUserId(),
                req.getStatusId(), req.getDeliveryId()
        );

        // 3. get order_id by delivery_id
        Integer orderId = jdbcTemplate.queryForObject(
                "SELECT order_id FROM pos_main_order_tb WHERE delivery_order_id = ?",
                Integer.class, req.getDeliveryId()
        );

        // 4. update order — bill_no same as order_code
        jdbcTemplate.update(
                "UPDATE pos_main_order_tb SET " +
                        "customer_id=?, bill_no=?, sub_total_price=?, total_discount_price=?, " +
                        "delivery_fee=?, total_order_price=?, paid_amount=?, payment_type_id=? " +
                        "WHERE order_id=?",
                customerId, req.getOrderCode(), req.getSubTotalPrice(),
                req.getTotalDiscountPrice(), req.getDeliveryFee(),
                req.getTotalOrderPrice(), req.getPaidAmount(),
                req.getPaymentTypeId(), orderId
        );

        // 5. delete old order details and re-insert
        jdbcTemplate.update(
                "DELETE FROM pos_main_order_details_tb WHERE order_id = ?", orderId
        );

        for (OrderDetailItemDTO item : req.getItems()) {
            jdbcTemplate.update(
                    "INSERT INTO pos_main_order_details_tb " +
                            "(order_id, item_id, item_bar_code, unit_type_id, printer_type_id, " +
                            "quantity, per_item_price, total_discount_price, total_item_price, remark, status, user_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?)",
                    orderId, item.getItemId(), item.getItemBarCode(),
                    item.getUnitTypeId(), item.getPrinterTypeId(),
                    item.getQuantity(), item.getPerItemPrice(),
                    item.getTotalDiscountPrice(), item.getTotalItemPrice(),
                    item.getRemark(), req.getUserId()
            );
        }
    }
}