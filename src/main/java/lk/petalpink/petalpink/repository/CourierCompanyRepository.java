package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.CourierCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CourierCompanyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(CourierCompanyDTO dto) {
        String sql = "INSERT INTO pos_courier_company_tb (company_name, company_contact, address, email, created_date, edited_date, status, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getCompanyName(), dto.getCompanyContact(), dto.getAddress(), dto.getEmail(), LocalDateTime.now(), LocalDateTime.now(), dto.getStatus(), dto.getUserId());
    }

    public List<CourierCompanyDTO> findAll() {
        String sql = "SELECT * FROM pos_courier_company_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CourierCompanyDTO.class));
    }

    public int update(CourierCompanyDTO dto) {
        String sql = "UPDATE pos_courier_company_tb SET company_name=?, company_contact=?, address=?, email=?, edited_date=?, status=?, user_id=? WHERE company_id=?";
        return jdbcTemplate.update(sql, dto.getCompanyName(), dto.getCompanyContact(), dto.getAddress(), dto.getEmail(), LocalDateTime.now(), dto.getStatus(), dto.getUserId(), dto.getCompanyId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_courier_company_tb SET status = 0 WHERE company_id = ?", id);
    }
}