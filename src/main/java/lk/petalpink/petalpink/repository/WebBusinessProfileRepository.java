package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteBusinessProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class WebBusinessProfileRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createBusinessProfile(WebsiteBusinessProfileDTO dto) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_business_profile_tb (business_name, description) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getBusinessName());
            ps.setString(2, dto.getDescription());
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public List<WebsiteBusinessProfileDTO> getAllBusinessProfiles() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_business_profile_tb WHERE status = 1",
                new BeanPropertyRowMapper<>(WebsiteBusinessProfileDTO.class));
    }

    public int updateBusinessProfile(Integer businessId, WebsiteBusinessProfileDTO dto) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_business_profile_tb SET business_name = ?, description = ? WHERE business_id = ? AND status = 1",
                dto.getBusinessName(), dto.getDescription(), businessId);
    }

    public int deleteBusinessProfile(Integer businessId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_business_profile_tb SET status = 0 WHERE business_id = ?",
                businessId);
    }
}