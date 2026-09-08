package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.EmployeeSalaryDTO;
import lk.petalpink.petalpink.repository.EmployeeSalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeSalaryService {

    @Autowired
    private EmployeeSalaryRepository salaryRepository;

    // Get all salary records for a specific month (YYYY-MM)
    public List<EmployeeSalaryDTO> getSalaryByMonth(String month) {
        return salaryRepository.getSalaryByMonth(month);
    }

    // Get all salary records for a specific employee
    public List<EmployeeSalaryDTO> getSalaryByEmployee(Integer employeeId) {
        return salaryRepository.getSalaryByEmployee(employeeId);
    }

    // Save (upsert) a list of salary records — called from frontend bulk save
    public void saveSalaryList(List<EmployeeSalaryDTO> list) {
        for (EmployeeSalaryDTO dto : list) {
            // Ensure net salary is always recalculated server-side
            double net = dto.getBasicSalary()
                    + dto.getAllowances()
                    + dto.getOvertime()
                    - dto.getDeductions();
            dto.setNetSalary(net);
            salaryRepository.upsertSalary(dto);
        }
    }

    // Delete a single salary record by ID
    public void deleteSalary(Integer salaryId) {
        salaryRepository.deleteSalary(salaryId);
    }
}