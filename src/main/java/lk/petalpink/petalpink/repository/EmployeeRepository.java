package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BASE =
            "SELECT e.employee_id, e.first_name, e.last_name, e.gmail, e.phone, " +
                    "       e.designation_id, e.title_id, " +
                    "       d.designation  AS designation_name, " +
                    "       t.title_name   AS title_name, " +
                    "       DATE_FORMAT(e.joining_date, '%Y-%m-%d') AS joining_date, " +
                    "       e.status " +
                    "FROM   pos_emp_employee_management_tb e " +
                    "LEFT JOIN pos_emp_employee_designation_tb d ON d.designation_id = e.designation_id " +
                    "LEFT JOIN pos_emp_employee_title_tb t       ON t.title_id = e.title_id ";

    private final RowMapper<EmployeeDTO> rowMapper = new RowMapper<>() {
        @Override
        public EmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setEmployeeId(rs.getInt("employee_id"));
            dto.setFirstName(rs.getString("first_name"));
            dto.setLastName(rs.getString("last_name"));
            dto.setEmail(rs.getString("gmail"));
            dto.setPhone(rs.getString("phone"));
            dto.setDesignationId(rs.getObject("designation_id") != null ? rs.getInt("designation_id") : null);
            dto.setTitleId(rs.getObject("title_id") != null ? rs.getInt("title_id") : null);
            dto.setDesignationName(rs.getString("designation_name"));
            dto.setTitleName(rs.getString("title_name"));
            dto.setJoiningDate(rs.getString("joining_date"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            return dto;
        }
    };

    // ── GET all (active) employees ──────────────────────────────────────────
    public List<EmployeeDTO> findAll() {
        String sql = SELECT_BASE + "WHERE e.status != 0 ORDER BY e.first_name, e.last_name";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── GET one employee ────────────────────────────────────────────────────
    public EmployeeDTO findById(int employeeId) {
        String sql = SELECT_BASE + "WHERE e.employee_id = ?";
        List<EmployeeDTO> results = jdbcTemplate.query(sql, rowMapper, employeeId);
        return results.isEmpty() ? null : results.get(0);
    }

    // ── INSERT ───────────────────────────────────────────────────────────────
    public int save(EmployeeDTO dto) {
        String fullName = buildFullName(dto.getFirstName(), dto.getLastName());
        String sql = "INSERT INTO pos_emp_employee_management_tb " +
                "(first_name, last_name, employee_name, gmail, phone, designation_id, title_id, " +
                " employee_designation, employee_title, joining_date, status, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, " +
                "        (SELECT designation FROM pos_emp_employee_designation_tb WHERE designation_id = ?), " +
                "        (SELECT title_name FROM pos_emp_employee_title_tb WHERE title_id = ?), " +
                "        ?, ?, 1)";
        return jdbcTemplate.update(sql,
                dto.getFirstName(), dto.getLastName(), fullName, dto.getEmail(), dto.getPhone(),
                dto.getDesignationId(), dto.getTitleId(),
                dto.getDesignationId(), dto.getTitleId(),
                dto.getJoiningDate(), dto.getStatus());
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public int update(EmployeeDTO dto) {
        String fullName = buildFullName(dto.getFirstName(), dto.getLastName());
        String sql = "UPDATE pos_emp_employee_management_tb SET " +
                "first_name = ?, last_name = ?, employee_name = ?, gmail = ?, phone = ?, " +
                "designation_id = ?, title_id = ?, " +
                "employee_designation = (SELECT designation FROM pos_emp_employee_designation_tb WHERE designation_id = ?), " +
                "employee_title = (SELECT title_name FROM pos_emp_employee_title_tb WHERE title_id = ?), " +
                "joining_date = ?, status = ? " +
                "WHERE employee_id = ?";
        return jdbcTemplate.update(sql,
                dto.getFirstName(), dto.getLastName(), fullName, dto.getEmail(), dto.getPhone(),
                dto.getDesignationId(), dto.getTitleId(),
                dto.getDesignationId(), dto.getTitleId(),
                dto.getJoiningDate(), dto.getStatus(),
                dto.getEmployeeId());
    }

    // ── soft DELETE ──────────────────────────────────────────────────────────
    public int softDelete(int employeeId) {
        String sql = "UPDATE pos_emp_employee_management_tb SET status = 0 WHERE employee_id = ?";
        return jdbcTemplate.update(sql, employeeId);
    }

    private String buildFullName(String firstName, String lastName) {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();
        return (first + " " + last).trim();
    }
}