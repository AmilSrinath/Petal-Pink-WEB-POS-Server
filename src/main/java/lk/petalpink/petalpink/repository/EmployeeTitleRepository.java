package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.EmployeeTitleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeTitleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<EmployeeTitleDTO> rowMapper = new RowMapper<>() {
        @Override
        public EmployeeTitleDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeTitleDTO dto = new EmployeeTitleDTO();
            dto.setTitleId(rs.getInt("title_id"));
            dto.setTitleName(rs.getString("title_name"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            return dto;
        }
    };

    public List<EmployeeTitleDTO> findAll() {
        String sql = "SELECT title_id, title_name, status FROM pos_emp_employee_title_tb " +
                "WHERE visible = 1 OR visible IS NULL ORDER BY title_id DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public EmployeeTitleDTO findById(int titleId) {
        String sql = "SELECT title_id, title_name, status FROM pos_emp_employee_title_tb WHERE title_id = ?";
        List<EmployeeTitleDTO> results = jdbcTemplate.query(sql, rowMapper, titleId);
        return results.isEmpty() ? null : results.get(0);
    }

    public int save(EmployeeTitleDTO dto) {
        String sql = "INSERT INTO pos_emp_employee_title_tb (title_name, status, visible) VALUES (?, ?, 1)";
        return jdbcTemplate.update(sql, dto.getTitleName(), dto.getStatus());
    }

    public int update(EmployeeTitleDTO dto) {
        String sql = "UPDATE pos_emp_employee_title_tb SET title_name = ?, status = ? WHERE title_id = ?";
        return jdbcTemplate.update(sql, dto.getTitleName(), dto.getStatus(), dto.getTitleId());
    }

    public int softDelete(int titleId) {
        String sql = "UPDATE pos_emp_employee_title_tb SET visible = 0 WHERE title_id = ?";
        return jdbcTemplate.update(sql, titleId);
    }
}