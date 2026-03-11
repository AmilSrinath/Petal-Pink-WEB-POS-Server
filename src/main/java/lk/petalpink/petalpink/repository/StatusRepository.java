package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.StatusTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StatusRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<StatusTypeDTO> getStatusTypesByRegId(Integer regId) {
        String sql =
                "SELECT st.status_id, st.status_type, st.reg_id, st.status " +
                        "FROM pos_status_types st " +
                        "WHERE st.reg_id = ? AND st.status = 1";

        return jdbcTemplate.query(sql, new RowMapper<StatusTypeDTO>() {
            @Override
            public StatusTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                StatusTypeDTO dto = new StatusTypeDTO();
                dto.setStatusId(rs.getInt("status_id"));
                dto.setStatusType(rs.getString("status_type"));
                dto.setRegId(rs.getInt("reg_id"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                return dto;
            }
        }, regId);
    }
}