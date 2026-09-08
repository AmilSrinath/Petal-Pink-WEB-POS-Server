package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.EmployeeAttendanceReportDTO;
import lk.petalpink.petalpink.service.EmployeeAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class EmployeeAttendanceReportController {

    @Autowired
    private EmployeeAttendanceService attendanceService;

    // GET /api/reports/employee-attendance?dateFrom=2026-07-01&dateTo=2026-07-31
    @GetMapping("/employee-attendance")
    public ResponseEntity<EmployeeAttendanceReportDTO> getEmployeeAttendanceReport(
            @RequestParam String dateFrom,
            @RequestParam String dateTo) {
        try {
            return ResponseEntity.ok(attendanceService.getEmployeeAttendanceReport(dateFrom, dateTo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}