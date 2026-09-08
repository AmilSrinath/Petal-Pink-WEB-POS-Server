package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttendanceReportRowDTO {
    private Integer employeeId;
    private String  employeeName;
    private String  designationName;

    private Long presentCount;
    private Long absentCount;
    private Long halfDayCount;
    private Long leaveCount;
    private Long totalDays;

    private Double presentPercent;
    private Double absentPercent;
    private Double halfDayPercent;
    private Double leavePercent;

    private Double totalWorkedHours;
}