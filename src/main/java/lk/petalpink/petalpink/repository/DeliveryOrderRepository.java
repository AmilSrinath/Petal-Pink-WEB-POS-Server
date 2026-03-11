package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DeliveryOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DeliveryOrderDTO> getByDateRange(String startDate, String endDate) {
        String sql =
                "SELECT " +
                        "d.delivery_id, d.website_order_id, d.order_code, d.cod_amount, d.weight, " +
                        "d.remark, d.order_type, d.status, d.status_id, d.is_free_delivery, " +
                        "d.is_return, d.is_exchange, d.user_id, d.created_date, d.delivered_date, " +
                        "c.customer_id, c.customer_name, c.customer_number, c.phone_one, c.phone_two, c.address, " +
                        "o.order_id, o.bill_no, o.sub_total_price, o.total_discount_price, " +
                        "o.delivery_fee, o.total_order_price, o.paid_amount, o.payment_type_id " +
                        "FROM pos_main_delivery_order_tb d " +
                        "LEFT JOIN pos_main_customer_tb c ON d.customer_id = c.customer_id " +
                        "LEFT JOIN pos_main_order_tb o ON o.delivery_order_id = d.delivery_id " +
                        "WHERE d.created_date BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, new RowMapper<DeliveryOrderDTO>() {
            @Override
            public DeliveryOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                DeliveryOrderDTO dto = new DeliveryOrderDTO();

                // delivery order
                dto.setDeliveryId(rs.getInt("delivery_id"));
                dto.setWebsiteOrderId(rs.getString("website_order_id"));
                dto.setOrderCode(rs.getString("order_code"));
                dto.setCodAmount(rs.getBigDecimal("cod_amount"));
                dto.setWeight(rs.getString("weight"));
                dto.setRemark(rs.getString("remark"));
                dto.setOrderType(rs.getString("order_type"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setStatusId(rs.getObject("status_id") != null ? rs.getInt("status_id") : null);
                dto.setIsFreeDelivery(rs.getObject("is_free_delivery") != null ? rs.getInt("is_free_delivery") : null);
                dto.setIsReturn(rs.getObject("is_return") != null ? rs.getInt("is_return") : null);
                dto.setIsExchange(rs.getObject("is_exchange") != null ? rs.getInt("is_exchange") : null);
                dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                dto.setCreatedDate(rs.getTimestamp("created_date"));
                dto.setDeliveredDate(rs.getTimestamp("delivered_date"));

                // customer
                dto.setCustomerId(rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null);
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerNumber(rs.getString("customer_number"));
                dto.setPhoneOne(rs.getString("phone_one"));
                dto.setPhoneTwo(rs.getString("phone_two"));
                dto.setAddress(rs.getString("address"));

                // order
                dto.setOrderId(rs.getObject("order_id") != null ? rs.getInt("order_id") : null);
                dto.setBillNo(rs.getString("bill_no"));
                dto.setSubTotalPrice(rs.getObject("sub_total_price") != null ? rs.getDouble("sub_total_price") : null);
                dto.setTotalDiscountPrice(rs.getObject("total_discount_price") != null ? rs.getDouble("total_discount_price") : null);
                dto.setDeliveryFee(rs.getObject("delivery_fee") != null ? rs.getDouble("delivery_fee") : null);
                dto.setTotalOrderPrice(rs.getObject("total_order_price") != null ? rs.getDouble("total_order_price") : null);
                dto.setPaidAmount(rs.getObject("paid_amount") != null ? rs.getDouble("paid_amount") : null);
                dto.setPaymentTypeId(rs.getObject("payment_type_id") != null ? rs.getInt("payment_type_id") : null);

                return dto;
            }
        }, startDate + " 00:00:00", endDate + " 23:59:59");
    }

    public List<ItemDTO> getOrderItemsByDeliveryId(Integer deliveryId) {
        String sql =
                "SELECT " +
                        "i.item_id, i.item_bar_code, i.main_item_category_id, i.sub_item_category_id, " +
                        "i.item_prefix, i.item_code_prefix, i.discount, i.item_name, i.unit_type, " +
                        "i.printer_type, i.cost_price, i.unit_price, i.image_path, i.grn_status, " +
                        "i.selling_status, i.status, i.user_id, i.visible, i.weight, " +
                        "od.quantity, od.per_item_price, od.total_discount_price AS item_discount, od.total_item_price " +
                        "FROM pos_main_delivery_order_tb d " +
                        "JOIN pos_main_order_tb o ON o.delivery_order_id = d.delivery_id " +
                        "JOIN pos_main_order_details_tb od ON od.order_id = o.order_id " +
                        "JOIN pos_main_item_tb i ON i.item_id = od.item_id " +
                        "WHERE d.delivery_id = ? AND od.status = 1";

        return jdbcTemplate.query(sql, new RowMapper<ItemDTO>() {
            @Override
            public ItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemDTO dto = new ItemDTO();
                dto.setItemId(rs.getInt("item_id"));
                dto.setItemBarCode(rs.getObject("item_bar_code") != null ? rs.getInt("item_bar_code") : null);
                dto.setMainItemCategoryId(rs.getObject("main_item_category_id") != null ? rs.getInt("main_item_category_id") : null);
                dto.setSubItemCategoryId(rs.getObject("sub_item_category_id") != null ? rs.getInt("sub_item_category_id") : null);
                dto.setItemPrefix(rs.getString("item_prefix"));
                dto.setItemCodePrefix(rs.getString("item_code_prefix"));
                dto.setDiscount(rs.getObject("discount") != null ? rs.getDouble("discount") : null);
                dto.setItemName(rs.getString("item_name"));
                dto.setUnitType(rs.getString("unit_type"));
                dto.setPrinterType(rs.getString("printer_type"));
                dto.setCostPrice(rs.getObject("cost_price") != null ? rs.getDouble("cost_price") : null);
                dto.setUnitPrice(rs.getObject("unit_price") != null ? rs.getDouble("unit_price") : null);
                dto.setImagePath(rs.getString("image_path"));
                dto.setGrnStatus(rs.getObject("grn_status") != null ? rs.getInt("grn_status") : null);
                dto.setSellingStatus(rs.getObject("selling_status") != null ? rs.getInt("selling_status") : null);
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
                dto.setWeight(rs.getObject("weight") != null ? rs.getDouble("weight") : null);
                dto.setQuantity(rs.getObject("quantity") != null ? rs.getInt("quantity") : null);
                return dto;
            }
        }, deliveryId);
    }

    public void updateDeliveryStatus(Integer deliveryId, Integer statusId) {
        jdbcTemplate.update(
                "UPDATE pos_main_delivery_order_tb SET status_id = ? WHERE delivery_id = ?",
                statusId, deliveryId
        );
    }

    public String getRemarkByDeliveryId(Integer deliveryId) {
        String sql = "SELECT remark FROM pos_main_delivery_order_tb WHERE delivery_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, deliveryId);
    }

    public void updateRemark(Integer deliveryId, String remark) {
        String sql = "UPDATE pos_main_delivery_order_tb SET remark = ? WHERE delivery_id = ?";
        jdbcTemplate.update(sql, remark, deliveryId);
    }
}