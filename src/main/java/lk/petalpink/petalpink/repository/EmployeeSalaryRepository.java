package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.EmployeeSalaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeSalaryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ── RowMapper ──────────────────────────────────────────────────────────────
    private final RowMapper<EmployeeSalaryDTO> rowMapper = new RowMapper<>() {
        @Override
        public EmployeeSalaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalaryDTO dto = new EmployeeSalaryDTO();
            dto.setSalaryId(rs.getInt("salary_id"));
            dto.setEmployeeId(rs.getInt("employee_id"));
            dto.setEmployeeName(rs.getString("employee_name"));
            dto.setMonth(rs.getString("month"));
            dto.setBasicSalary(rs.getDouble("basic_salary"));
            dto.setAllowances(rs.getDouble("allowances"));
            dto.setOvertime(rs.getDouble("overtime"));
            dto.setDeductions(rs.getDouble("deductions"));
            dto.setNetSalary(rs.getDouble("net_salary"));
            dto.setPaymentStatus(rs.getString("payment_status"));
            dto.setNote(rs.getString("note"));
            return dto;
        }
    };

    // ── GET all salary records for a given month ───────────────────────────────
    public List<EmployeeSalaryDTO> getSalaryByMonth(String month) {
        String sql = "SELECT s.salary_id, s.employee_id, " +
                "       e.employee_name, " +
                "       s.month, s.basic_salary, s.allowances, " +
                "       s.overtime, s.deductions, s.net_salary, " +
                "       s.payment_status, s.note " +
                "FROM   pos_emp_salary_tb s " +
                "JOIN   pos_emp_employee_management_tb e ON e.employee_id = s.employee_id " +
                "WHERE  s.month = ? " +
                "ORDER  BY e.employee_name";
        return jdbcTemplate.query(sql, rowMapper, month);
    }

    // ── GET all salary records for a given employee ────────────────────────────
    public List<EmployeeSalaryDTO> getSalaryByEmployee(Integer employeeId) {
        String sql = "SELECT s.salary_id, s.employee_id, " +
                "       e.employee_name, " +
                "       s.month, s.basic_salary, s.allowances, " +
                "       s.overtime, s.deductions, s.net_salary, " +
                "       s.payment_status, s.note " +
                "FROM   pos_emp_salary_tb s " +
                "JOIN   pos_emp_employee_management_tb e ON e.employee_id = s.employee_id " +
                "WHERE  s.employee_id = ? " +
                "ORDER  BY s.month DESC";
        return jdbcTemplate.query(sql, rowMapper, employeeId);
    }

    // ── UPSERT (INSERT or UPDATE) a single salary record ──────────────────────
    public void upsertSalary(EmployeeSalaryDTO dto) {
        double net = dto.getBasicSalary()
                + dto.getAllowances()
                + dto.getOvertime()
                - dto.getDeductions();

        String sql = "INSERT INTO pos_emp_salary_tb " +
                "  (employee_id, month, basic_salary, allowances, overtime, " +
                "   deductions, net_salary, payment_status, note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "  basic_salary    = VALUES(basic_salary), " +
                "  allowances      = VALUES(allowances), " +
                "  overtime        = VALUES(overtime), " +
                "  deductions      = VALUES(deductions), " +
                "  net_salary      = VALUES(net_salary), " +
                "  payment_status  = VALUES(payment_status), " +
                "  note            = VALUES(note)";

        jdbcTemplate.update(sql,
                dto.getEmployeeId(),
                dto.getMonth(),
                dto.getBasicSalary(),
                dto.getAllowances(),
                dto.getOvertime(),
                dto.getDeductions(),
                net,
                dto.getPaymentStatus(),
                dto.getNote()
        );
    }

    // ── DELETE a single salary record ─────────────────────────────────────────
    public void deleteSalary(Integer salaryId) {
        String sql = "DELETE FROM pos_emp_salary_tb WHERE salary_id = ?";
        jdbcTemplate.update(sql, salaryId);
    }
}