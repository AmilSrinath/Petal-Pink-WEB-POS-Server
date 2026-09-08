package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttendanceDTO {
    private Integer attendanceId;
    private Integer employeeId;
    private String  employeeName;
    private String  date;        // "YYYY-MM-DD"
    private String  status;      // present | absent | half-day | leave
    private String  checkIn;     // "HH:mm"
    private String  checkOut;    // "HH:mm"
    private String  note;
}