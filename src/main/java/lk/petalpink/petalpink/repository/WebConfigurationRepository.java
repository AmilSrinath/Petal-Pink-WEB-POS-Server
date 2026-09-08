package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteConfigDTO;
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
public class WebConfigurationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveConfig(WebsiteConfigDTO dto) {
        jdbcTemplate.update(
                "UPDATE petal_pink_configuration_tb SET status = 0 WHERE config_name = ?",
                dto.getConfigName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_configuration_tb (config_name, config_value, created_date, status, user_id) VALUES (?, ?, NOW(), 1, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getConfigName());
            ps.setString(2, dto.getConfigValue());
            ps.setInt(3, dto.getUserId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<WebsiteConfigDTO> getAllConfigs() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_configuration_tb WHERE status = 1",
                new BeanPropertyRowMapper<>(WebsiteConfigDTO.class));
    }
}