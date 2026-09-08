package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.EmployeeSalaryDTO;
import lk.petalpink.petalpink.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-salary")
@CrossOrigin(origins = "*")
public class EmployeeSalaryController {

    @Autowired
    private EmployeeSalaryService salaryService;

    // GET /api/employee-salary?month=2026-06
    @GetMapping
    public ResponseEntity<List<EmployeeSalaryDTO>> getSalaryByMonth(
            @RequestParam String month) {
        try {
            return ResponseEntity.ok(salaryService.getSalaryByMonth(month));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/employee-salary/employee/{employeeId}
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeSalaryDTO>> getSalaryByEmployee(
            @PathVariable Integer employeeId) {
        try {
            return ResponseEntity.ok(salaryService.getSalaryByEmployee(employeeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/employee-salary  — bulk save (array of records)
    @PostMapping
    public ResponseEntity<?> saveSalary(
            @RequestBody List<EmployeeSalaryDTO> salaryList) {
        try {
            salaryService.saveSalaryList(salaryList);
            return ResponseEntity.ok("Salary records saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save salary: " + e.getMessage());
        }
    }

    // DELETE /api/employee-salary/{salaryId}
    @DeleteMapping("/{salaryId}")
    public ResponseEntity<?> deleteSalary(@PathVariable Integer salaryId) {
        try {
            salaryService.deleteSalary(salaryId);
            return ResponseEntity.ok("Salary record deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete: " + e.getMessage());
        }
    }
}