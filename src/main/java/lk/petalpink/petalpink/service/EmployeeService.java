package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.EmployeeDTO;
import lk.petalpink.petalpink.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeDTO getEmployeeById(int employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public String createEmployee(EmployeeDTO dto) {
        int rows = employeeRepository.save(dto);
        return rows > 0 ? "Employee created successfully" : "Failed to create employee";
    }

    public String updateEmployee(EmployeeDTO dto) {
        int rows = employeeRepository.update(dto);
        return rows > 0 ? "Employee updated successfully" : "Employee not found or update failed";
    }

    public String deleteEmployee(int employeeId) {
        int rows = employeeRepository.softDelete(employeeId);
        return rows > 0 ? "Employee deleted successfully" : "Employee not found";
    }
}