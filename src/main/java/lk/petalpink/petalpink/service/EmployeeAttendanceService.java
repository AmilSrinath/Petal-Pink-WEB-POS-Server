package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.EmployeeAttendanceDTO;
import lk.petalpink.petalpink.dto.EmployeeAttendanceReportDTO;
import lk.petalpink.petalpink.repository.EmployeeAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeAttendanceService {

    @Autowired
    private EmployeeAttendanceRepository attendanceRepository;

    // Get all attendance records for a specific date
    public List<EmployeeAttendanceDTO> getAttendanceByDate(String date) {
        return attendanceRepository.getAttendanceByDate(date);
    }

    // Get all attendance records for a specific employee
    public List<EmployeeAttendanceDTO> getAttendanceByEmployee(Integer employeeId) {
        return attendanceRepository.getAttendanceByEmployee(employeeId);
    }

    // Save (upsert) a list of attendance records — called from frontend bulk save
    public void saveAttendanceList(List<EmployeeAttendanceDTO> list) {
        for (EmployeeAttendanceDTO dto : list) {
            // If absent or leave, clear check-in / check-out
            if ("absent".equals(dto.getStatus()) || "leave".equals(dto.getStatus())) {
                dto.setCheckIn(null);
                dto.setCheckOut(null);
            }
            attendanceRepository.upsertAttendance(dto);
        }
    }

    // Delete a single attendance record by ID
    public void deleteAttendance(Integer attendanceId) {
        attendanceRepository.deleteAttendance(attendanceId);
    }

    // Get the employee attendance report for a date range (per-employee breakdown + totals)
    public EmployeeAttendanceReportDTO getEmployeeAttendanceReport(String dateFrom, String dateTo) {
        return attendanceRepository.getEmployeeAttendanceReport(dateFrom, dateTo);
    }
}