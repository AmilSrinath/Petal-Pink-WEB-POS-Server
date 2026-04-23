package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.InquiryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InquiryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<InquiryDTO> getAllInquiriesWithCustomer() {
        String sql =
                "SELECT " +
                        "i.inquiry_id, i.way_bill, i.customer_id, i.customer_name, " +
                        "i.customer_phone_1, i.customer_phone_2, i.company, i.branch, " +
                        "i.branch_contact, i.reson, i.remark, i.status, " +
                        "i.created_date, i.edited_date, i.user_id, i.status_id, " +
                        "c.address AS customer_address, c.nic AS customer_nic, " +
                        "c.customer_number, c.phone_one AS customer_phone_one, " +
                        "c.phone_two AS customer_phone_two, c.is_loyalty " +
                        "FROM pos_inquiry_tb i " +
                        "LEFT JOIN pos_main_customer_tb c ON i.customer_id = c.customer_id";

        return jdbcTemplate.query(sql, new RowMapper<InquiryDTO>() {
            @Override
            public InquiryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                InquiryDTO dto = new InquiryDTO();

                // inquiry fields
                dto.setInquiryId(rs.getInt("inquiry_id"));
                dto.setWayBill(rs.getString("way_bill"));
                dto.setCustomerId(rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null);
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerPhone1(rs.getString("customer_phone_1"));
                dto.setCustomerPhone2(rs.getString("customer_phone_2"));
                dto.setCompany(rs.getString("company"));
                dto.setBranch(rs.getString("branch"));
                dto.setBranchContact(rs.getString("branch_contact"));
                dto.setReason(rs.getString("reson")); // note: typo in DB column kept as-is
                dto.setRemark(rs.getString("remark"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setCreatedDate(rs.getDate("created_date"));
                dto.setEditedDate(rs.getDate("edited_date"));
                dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                dto.setStatusId(rs.getObject("status_id") != null ? rs.getInt("status_id") : null);

                // customer fields
                dto.setCustomerAddress(rs.getString("customer_address"));
                dto.setCustomerNic(rs.getString("customer_nic"));
                dto.setCustomerNumber(rs.getString("customer_number"));
                dto.setCustomerPhoneOne(rs.getString("customer_phone_one"));
                dto.setCustomerPhoneTwo(rs.getString("customer_phone_two"));
                dto.setIsLoyalty(rs.getObject("is_loyalty") != null ? rs.getInt("is_loyalty") : null);

                return dto;
            }
        });
    }

    public List<InquiryDTO> getInquiriesByDateRange(String startDate, String endDate) {
        String sql =
                "SELECT " +
                        "i.inquiry_id, i.way_bill, i.customer_id, i.customer_name, " +
                        "i.customer_phone_1, i.customer_phone_2, i.company, i.branch, " +
                        "i.branch_contact, i.reson, i.remark, i.status, " +
                        "i.created_date, i.edited_date, i.user_id, i.status_id, " +
                        "c.address AS customer_address, c.nic AS customer_nic, " +
                        "c.customer_number, c.phone_one AS customer_phone_one, " +
                        "c.phone_two AS customer_phone_two, c.is_loyalty " +
                        "FROM pos_inquiry_tb i " +
                        "LEFT JOIN pos_main_customer_tb c ON i.customer_id = c.customer_id " +
                        "WHERE i.created_date BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, new RowMapper<InquiryDTO>() {
            @Override
            public InquiryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                InquiryDTO dto = new InquiryDTO();

                dto.setInquiryId(rs.getInt("inquiry_id"));
                dto.setWayBill(rs.getString("way_bill"));
                dto.setCustomerId(rs.getObject("customer_id") != null ? rs.getInt("customer_id") : null);
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerPhone1(rs.getString("customer_phone_1"));
                dto.setCustomerPhone2(rs.getString("customer_phone_2"));
                dto.setCompany(rs.getString("company"));
                dto.setBranch(rs.getString("branch"));
                dto.setBranchContact(rs.getString("branch_contact"));
                dto.setReason(rs.getString("reson"));
                dto.setRemark(rs.getString("remark"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setCreatedDate(rs.getDate("created_date"));
                dto.setEditedDate(rs.getDate("edited_date"));
                dto.setUserId(rs.getObject("user_id") != null ? rs.getInt("user_id") : null);
                dto.setStatusId(rs.getObject("status_id") != null ? rs.getInt("status_id") : null);

                dto.setCustomerAddress(rs.getString("customer_address"));
                dto.setCustomerNic(rs.getString("customer_nic"));
                dto.setCustomerNumber(rs.getString("customer_number"));
                dto.setCustomerPhoneOne(rs.getString("customer_phone_one"));
                dto.setCustomerPhoneTwo(rs.getString("customer_phone_two"));
                dto.setIsLoyalty(rs.getObject("is_loyalty") != null ? rs.getInt("is_loyalty") : null);

                return dto;
            }
        }, startDate, endDate);
    }

    public int updateInquiryStatus(int inquiryId, int statusId) {
        String sql = "UPDATE pos_inquiry_tb SET status_id = ?, edited_date = CURDATE() WHERE inquiry_id = ?";
        return jdbcTemplate.update(sql, statusId, inquiryId);
    }

    public int saveInquiry(InquiryDTO dto) {
        String sql =
                "INSERT INTO pos_inquiry_tb " +
                        "(way_bill, customer_id, customer_name, customer_phone_1, customer_phone_2, " +
                        "company, branch, branch_contact, reson, remark, status, created_date, user_id, status_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?, ?)";

        return jdbcTemplate.update(sql,
                dto.getWayBill(),
                dto.getCustomerId(),
                dto.getCustomerName(),
                dto.getCustomerPhone1(),
                dto.getCustomerPhone2(),
                dto.getCompany(),
                dto.getBranch(),
                dto.getBranchContact(),
                dto.getReason(),
                dto.getRemark(),
                dto.getStatus(),
                dto.getUserId(),
                dto.getStatusId()
        );
    }
}