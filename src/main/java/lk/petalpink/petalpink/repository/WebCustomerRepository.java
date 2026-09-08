package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteCustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WebCustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<WebsiteCustomerDTO> getAllCustomers() {
        return jdbcTemplate.query(
                "SELECT * FROM pos_main_customer_tb ORDER BY cus_id DESC",
                new BeanPropertyRowMapper<>(WebsiteCustomerDTO.class));
    }

    public long getCustomerCount() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pos_main_customer_tb", Long.class);
        return count != null ? count : 0;
    }
}