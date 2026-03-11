package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OrderDTO> getOrdersWithCustomers() {
        String sql =
                "SELECT " +
                        "o.order_id, o.customer_id, o.delivery_order_id, o.bill_no, o.discount_id, " +
                        "o.sub_total_price, o.total_discount_price, o.delivery_fee, o.total_order_price, " +
                        "o.payment_type_id, o.table_id, o.created_Date, o.edited_Date, o.adult, o.child, " +
                        "o.remark, o.user_id, o.edited_by, o.status, o.visible, o.is_print, o.paid_amount, " +
                        "c.customer_name, c.nic, c.address, c.phone_one, c.phone_two, " +
                        "c.is_loyalty, c.loyalty_amount, c.customer_number, " +
                        "d.delivery_id, d.website_order_id, d.order_code, d.cod_amount, d.weight, " +
                        "d.remark AS delivery_remark, d.order_type, d.status AS delivery_status, " +
                        "d.status_id, d.is_free_delivery, d.is_return, d.is_exchange, " +
                        "d.created_date AS delivery_created_date, d.delivered_date " +
                        "FROM pos_main_order_tb o " +
                        "LEFT JOIN pos_main_customer_tb c ON o.customer_id = c.customer_id " +
                        "LEFT JOIN pos_main_delivery_order_tb d ON o.delivery_order_id = d.delivery_id " +
                        "WHERE o.visible = 1";

        return jdbcTemplate.query(sql, new RowMapper<OrderDTO>() {
            @Override
            public OrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderDTO dto = new OrderDTO();

                // order fields
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerId(rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null);
                dto.setDeliveryOrderId(rs.getObject("delivery_order_id") != null ? rs.getInt("delivery_order_id") : null);
                dto.setBillNo(rs.getString("bill_no"));
                dto.setDiscountId(rs.getObject("discount_id") != null ? rs.getInt("discount_id") : null);
                dto.setSubTotalPrice(rs.getObject("sub_total_price") != null ? rs.getDouble("sub_total_price") : null);
                dto.setTotalDiscountPrice(rs.getObject("total_discount_price") != null ? rs.getDouble("total_discount_price") : null);
                dto.setDeliveryFee(rs.getObject("delivery_fee") != null ? rs.getDouble("delivery_fee") : null);
                dto.setTotalOrderPrice(rs.getObject("total_order_price") != null ? rs.getDouble("total_order_price") : null);
                dto.setPaymentTypeId(rs.getObject("payment_type_id") != null ? rs.getInt("payment_type_id") : null);
                dto.setTableId(rs.getObject("table_id") != null ? rs.getInt("table_id") : null);
                dto.setCreatedDate(rs.getTimestamp("created_Date"));
                dto.setEditedDate(rs.getTimestamp("edited_Date"));
                dto.setAdult(rs.getObject("adult") != null ? rs.getInt("adult") : null);
                dto.setChild(rs.getObject("child") != null ? rs.getInt("child") : null);
                dto.setRemark(rs.getString("remark"));
                dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                dto.setEditedBy(rs.getObject("edited_by") != null ? rs.getInt("edited_by") : null);
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
                dto.setIsPrint(rs.getObject("is_print") != null ? rs.getInt("is_print") : null);
                dto.setPaidAmount(rs.getObject("paid_amount") != null ? rs.getDouble("paid_amount") : null);

                // customer fields
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setNic(rs.getString("nic"));
                dto.setAddress(rs.getString("address"));
                dto.setPhoneOne(rs.getString("phone_one"));
                dto.setPhoneTwo(rs.getString("phone_two"));
                dto.setIsLoyalty(rs.getObject("is_loyalty") != null ? rs.getInt("is_loyalty") : null);
                dto.setLoyaltyAmount(rs.getObject("loyalty_amount") != null ? rs.getDouble("loyalty_amount") : null);
                dto.setCustomerNumber(rs.getString("customer_number"));

                // delivery order fields
                dto.setDeliveryId(rs.getObject("delivery_id") != null ? rs.getInt("delivery_id") : null);
                dto.setWebsiteOrderId(rs.getString("website_order_id"));
                dto.setOrderCode(rs.getString("order_code"));
                dto.setCodAmount(rs.getBigDecimal("cod_amount"));
                dto.setWeight(rs.getString("weight"));
                dto.setDeliveryRemark(rs.getString("delivery_remark"));
                dto.setOrderType(rs.getString("order_type"));
                dto.setDeliveryStatus(rs.getObject("delivery_status") != null ? rs.getInt("delivery_status") : null);
                dto.setStatusId(rs.getObject("status_id") != null ? rs.getInt("status_id") : null);
                dto.setIsFreeDelivery(rs.getObject("is_free_delivery") != null ? rs.getInt("is_free_delivery") : null);
                dto.setIsReturn(rs.getObject("is_return") != null ? rs.getInt("is_return") : null);
                dto.setIsExchange(rs.getObject("is_exchange") != null ? rs.getInt("is_exchange") : null);
                dto.setDeliveryCreatedDate(rs.getTimestamp("delivery_created_date"));
                dto.setDeliveredDate(rs.getTimestamp("delivered_date"));

                return dto;
            }
        });
    }
}
