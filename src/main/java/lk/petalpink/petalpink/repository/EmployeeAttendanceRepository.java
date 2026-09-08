package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.EmployeeAttendanceDTO;
import lk.petalpink.petalpink.dto.EmployeeAttendanceReportDTO;
import lk.petalpink.petalpink.dto.EmployeeAttendanceReportRowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeAttendanceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ── RowMapper ──────────────────────────────────────────────────────────────
    private final RowMapper<EmployeeAttendanceDTO> rowMapper = new RowMapper<>() {
        @Override
        public EmployeeAttendanceDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeAttendanceDTO dto = new EmployeeAttendanceDTO();
            dto.setAttendanceId(rs.getInt("attendance_id"));
            dto.setEmployeeId(rs.getInt("employee_id"));
            dto.setEmployeeName(rs.getString("employee_name"));
            dto.setDate(rs.getString("date"));
            dto.setStatus(rs.getString("status"));
            dto.setCheckIn(rs.getString("check_in"));
            dto.setCheckOut(rs.getString("check_out"));
            dto.setNote(rs.getString("note"));
            return dto;
        }
    };

    // ── GET all attendance for a given date ────────────────────────────────────
    public List<EmployeeAttendanceDTO> getAttendanceByDate(String date) {
        String sql = "SELECT a.attendance_id, a.employee_id, " +
                "       e.employee_name, " +
                "       DATE_FORMAT(a.date, '%Y-%m-%d') AS date, " +
                "       a.status, " +
                "       TIME_FORMAT(a.check_in,  '%H:%i') AS check_in, " +
                "       TIME_FORMAT(a.check_out, '%H:%i') AS check_out, " +
                "       a.note " +
                "FROM   pos_emp_attendance_tb a " +
                "JOIN   pos_emp_employee_management_tb e ON e.employee_id = a.employee_id " +
                "WHERE  a.date = ? " +
                "ORDER  BY e.employee_name";
        return jdbcTemplate.query(sql, rowMapper, date);
    }

    // ── GET all attendance for a given employee ────────────────────────────────
    public List<EmployeeAttendanceDTO> getAttendanceByEmployee(Integer employeeId) {
        String sql = "SELECT a.attendance_id, a.employee_id, " +
                "       e.employee_name, " +
                "       DATE_FORMAT(a.date, '%Y-%m-%d') AS date, " +
                "       a.status, " +
                "       TIME_FORMAT(a.check_in,  '%H:%i') AS check_in, " +
                "       TIME_FORMAT(a.check_out, '%H:%i') AS check_out, " +
                "       a.note " +
                "FROM   pos_emp_attendance_tb a " +
                "JOIN   pos_emp_employee_management_tb e ON e.employee_id = a.employee_id " +
                "WHERE  a.employee_id = ? " +
                "ORDER  BY a.date DESC";
        return jdbcTemplate.query(sql, rowMapper, employeeId);
    }

    // ── UPSERT (INSERT or UPDATE) a single attendance record ──────────────────
    public void upsertAttendance(EmployeeAttendanceDTO dto) {
        String sql = "INSERT INTO pos_emp_attendance_tb " +
                "  (employee_id, date, status, check_in, check_out, note) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "  status    = VALUES(status), " +
                "  check_in  = VALUES(check_in), " +
                "  check_out = VALUES(check_out), " +
                "  note      = VALUES(note)";

        jdbcTemplate.update(sql,
                dto.getEmployeeId(),
                dto.getDate(),
                dto.getStatus(),
                dto.getCheckIn(),
                dto.getCheckOut(),
                dto.getNote()
        );
    }

    // ── DELETE a single attendance record ─────────────────────────────────────
    public void deleteAttendance(Integer attendanceId) {
        String sql = "DELETE FROM pos_emp_attendance_tb WHERE attendance_id = ?";
        jdbcTemplate.update(sql, attendanceId);
    }

    // ── RowMapper for per-employee report row (before percentages are filled) ──
    private final RowMapper<EmployeeAttendanceReportRowDTO> reportRowMapper = new RowMapper<>() {
        @Override
        public EmployeeAttendanceReportRowDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeAttendanceReportRowDTO row = new EmployeeAttendanceReportRowDTO();
            row.setEmployeeId(rs.getInt("employeeId"));
            row.setEmployeeName(rs.getString("employeeName"));
            row.setDesignationName(rs.getString("designationName"));
            row.setPresentCount(rs.getLong("presentCount"));
            row.setAbsentCount(rs.getLong("absentCount"));
            row.setHalfDayCount(rs.getLong("halfDayCount"));
            row.setLeaveCount(rs.getLong("leaveCount"));
            row.setTotalDays(rs.getLong("totalDays"));
            row.setTotalWorkedHours(rs.getDouble("totalWorkedHours"));
            return row;
        }
    };

    // ── GET employee attendance report for a date range ─────────────────────
    // GET /api/reports/employee-attendance?dateFrom=YYYY-MM-DD&dateTo=YYYY-MM-DD
    public EmployeeAttendanceReportDTO getEmployeeAttendanceReport(String dateFrom, String dateTo) {

        String sql = """
                SELECT
                    e.employee_id                                                                       AS employeeId,
                    e.employee_name                                                                      AS employeeName,
                    d.designation                                                                        AS designationName,
                    COUNT(CASE WHEN a.status = 'present'  THEN 1 END)                                    AS presentCount,
                    COUNT(CASE WHEN a.status = 'absent'   THEN 1 END)                                     AS absentCount,
                    COUNT(CASE WHEN a.status = 'half-day' THEN 1 END)                                     AS halfDayCount,
                    COUNT(CASE WHEN a.status = 'leave'    THEN 1 END)                                     AS leaveCount,
                    COUNT(a.attendance_id)                                                                AS totalDays,
                    COALESCE(SUM(
                        CASE WHEN a.check_in IS NOT NULL AND a.check_out IS NOT NULL
                             THEN TIMESTAMPDIFF(MINUTE, a.check_in, a.check_out) / 60.0
                             ELSE 0 END
                    ), 0)                                                                                 AS totalWorkedHours
                FROM   pos_emp_employee_management_tb e
                LEFT JOIN pos_emp_employee_designation_tb d ON d.designation_id = e.designation_id
                LEFT JOIN pos_emp_attendance_tb a
                       ON a.employee_id = e.employee_id AND a.date BETWEEN ? AND ?
                WHERE  e.status != 0
                GROUP  BY e.employee_id, e.employee_name, d.designation
                ORDER  BY e.employee_name
                """;

        List<EmployeeAttendanceReportRowDTO> rows = jdbcTemplate.query(sql, reportRowMapper, dateFrom, dateTo);

        long totalPresent = 0, totalAbsent = 0, totalHalfDay = 0, totalLeave = 0, totalRecords = 0;

        for (EmployeeAttendanceReportRowDTO row : rows) {
            totalPresent += row.getPresentCount();
            totalAbsent  += row.getAbsentCount();
            totalHalfDay += row.getHalfDayCount();
            totalLeave   += row.getLeaveCount();
            totalRecords += row.getTotalDays();

            long rowTotal = row.getTotalDays();
            row.setPresentPercent(calcPercent(row.getPresentCount(), rowTotal));
            row.setAbsentPercent( calcPercent(row.getAbsentCount(),  rowTotal));
            row.setHalfDayPercent(calcPercent(row.getHalfDayCount(), rowTotal));
            row.setLeavePercent(  calcPercent(row.getLeaveCount(),   rowTotal));
        }

        EmployeeAttendanceReportDTO report = new EmployeeAttendanceReportDTO();
        report.setTotalEmployees(rows.size());
        report.setTotalPresentCount(totalPresent);
        report.setTotalAbsentCount(totalAbsent);
        report.setTotalHalfDayCount(totalHalfDay);
        report.setTotalLeaveCount(totalLeave);
        report.setTotalRecords(totalRecords);
        report.setOverallPresentPercent(calcPercent(totalPresent, totalRecords));
        report.setOverallAbsentPercent( calcPercent(totalAbsent,  totalRecords));
        report.setOverallHalfDayPercent(calcPercent(totalHalfDay, totalRecords));
        report.setOverallLeavePercent(  calcPercent(totalLeave,   totalRecords));
        report.setRows(rows != null ? rows : new ArrayList<>());

        return report;
    }

    /** Returns percentage rounded to 2 decimal places; 0.00 when total is 0. */
    private double calcPercent(long part, long total) {
        if (total == 0) return 0.00;
        return Math.round((part * 100.0 / total) * 100.0) / 100.0;
    }
}