package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.EmployeeAttendanceDTO;
import lk.petalpink.petalpink.service.EmployeeAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-attendance")
@CrossOrigin(origins = "*")
public class EmployeeAttendanceController {

    @Autowired
    private EmployeeAttendanceService attendanceService;

    // GET /api/employee-attendance?date=2026-06-26
    @GetMapping
    public ResponseEntity<List<EmployeeAttendanceDTO>> getAttendanceByDate(
            @RequestParam String date) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/employee-attendance/employee/{employeeId}
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeAttendanceDTO>> getAttendanceByEmployee(
            @PathVariable Integer employeeId) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByEmployee(employeeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/employee-attendance  — bulk save (array of records)
    @PostMapping
    public ResponseEntity<?> saveAttendance(
            @RequestBody List<EmployeeAttendanceDTO> attendanceList) {
        try {
            attendanceService.saveAttendanceList(attendanceList);
            return ResponseEntity.ok("Attendance saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save attendance: " + e.getMessage());
        }
    }

    // DELETE /api/employee-attendance/{attendanceId}
    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Integer attendanceId) {
        try {
            attendanceService.deleteAttendance(attendanceId);
            return ResponseEntity.ok("Attendance record deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete: " + e.getMessage());
        }
    }
}