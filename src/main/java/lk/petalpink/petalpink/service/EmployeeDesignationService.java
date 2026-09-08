package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.EmployeeDesignationDTO;
import lk.petalpink.petalpink.repository.EmployeeDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeDesignationService {

    @Autowired
    private EmployeeDesignationRepository employeeDesignationRepository;

    public List<EmployeeDesignationDTO> getAllDesignations() {
        return employeeDesignationRepository.findAll();
    }

    public EmployeeDesignationDTO getDesignationById(int designationId) {
        return employeeDesignationRepository.findById(designationId);
    }

    public String createDesignation(EmployeeDesignationDTO dto) {
        int rows = employeeDesignationRepository.save(dto);
        return rows > 0 ? "Designation created successfully" : "Failed to create designation";
    }

    public String updateDesignation(EmployeeDesignationDTO dto) {
        int rows = employeeDesignationRepository.update(dto);
        return rows > 0 ? "Designation updated successfully" : "Designation not found or update failed";
    }

    public String deleteDesignation(int designationId) {
        int rows = employeeDesignationRepository.softDelete(designationId);
        return rows > 0 ? "Designation deleted successfully" : "Designation not found";
    }
}