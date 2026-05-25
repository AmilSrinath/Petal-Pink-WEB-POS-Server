package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.BatchRegDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class BatchRegRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveAndGetId(BatchRegDTO dto) {
        String sql = "INSERT INTO pos_inv_batch_reg " +
                "(batch_reg_prefix, batch_reg_code, is_active, user_id, remark) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getBatchRegPrefix() != null ? dto.getBatchRegPrefix() : "BA");
            ps.setObject(2, dto.getBatchRegCode());
            ps.setObject(3, dto.getIsActive());
            ps.setObject(4, dto.getUserId());
            ps.setString(5, dto.getRemark());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
}