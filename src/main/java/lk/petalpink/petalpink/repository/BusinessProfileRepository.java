package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.BusinessProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusinessProfileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<BusinessProfileDTO> findAll() {
        String sql = "SELECT * FROM pos_main_bussiness_profile WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BusinessProfileDTO.class));
    }
}