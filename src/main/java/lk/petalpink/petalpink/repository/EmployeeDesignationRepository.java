package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.EmployeeDesignationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeDesignationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<EmployeeDesignationDTO> rowMapper = new RowMapper<>() {
        @Override
        public EmployeeDesignationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeDesignationDTO dto = new EmployeeDesignationDTO();
            dto.setDesignationId(rs.getInt("designation_id"));
            dto.setDesignationName(rs.getString("designation"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            return dto;
        }
    };

    public List<EmployeeDesignationDTO> findAll() {
        String sql = "SELECT designation_id, designation, status FROM pos_emp_employee_designation_tb " +
                "WHERE visible = 1 OR visible IS NULL ORDER BY designation_id DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public EmployeeDesignationDTO findById(int designationId) {
        String sql = "SELECT designation_id, designation, status FROM pos_emp_employee_designation_tb WHERE designation_id = ?";
        List<EmployeeDesignationDTO> results = jdbcTemplate.query(sql, rowMapper, designationId);
        return results.isEmpty() ? null : results.get(0);
    }

    public int save(EmployeeDesignationDTO dto) {
        String sql = "INSERT INTO pos_emp_employee_designation_tb (designation, status, visible) VALUES (?, ?, 1)";
        return jdbcTemplate.update(sql, dto.getDesignationName(), dto.getStatus());
    }

    public int update(EmployeeDesignationDTO dto) {
        String sql = "UPDATE pos_emp_employee_designation_tb SET designation = ?, status = ? WHERE designation_id = ?";
        return jdbcTemplate.update(sql, dto.getDesignationName(), dto.getStatus(), dto.getDesignationId());
    }

    public int softDelete(int designationId) {
        String sql = "UPDATE pos_emp_employee_designation_tb SET visible = 0 WHERE designation_id = ?";
        return jdbcTemplate.update(sql, designationId);
    }
}