package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response payload for GET /api/reports/employee-attendance?dateFrom=...&dateTo=...
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttendanceReportDTO {

    private Integer totalEmployees;

    private Long totalPresentCount;
    private Long totalAbsentCount;
    private Long totalHalfDayCount;
    private Long totalLeaveCount;
    private Long totalRecords;

    private Double overallPresentPercent;
    private Double overallAbsentPercent;
    private Double overallHalfDayPercent;
    private Double overallLeavePercent;

    private List<EmployeeAttendanceReportRowDTO> rows;
}