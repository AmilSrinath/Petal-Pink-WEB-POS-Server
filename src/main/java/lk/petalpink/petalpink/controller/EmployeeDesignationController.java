package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.EmployeeDesignationDTO;
import lk.petalpink.petalpink.service.EmployeeDesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-designations")
@CrossOrigin(origins = "*")
public class EmployeeDesignationController {

    @Autowired
    private EmployeeDesignationService employeeDesignationService;

    @GetMapping
    public List<EmployeeDesignationDTO> getAllDesignations() {
        return employeeDesignationService.getAllDesignations();
    }

    @GetMapping("/{id}")
    public EmployeeDesignationDTO getDesignationById(@PathVariable int id) {
        return employeeDesignationService.getDesignationById(id);
    }

    @PostMapping
    public String createDesignation(@RequestBody EmployeeDesignationDTO dto) {
        return employeeDesignationService.createDesignation(dto);
    }

    @PutMapping
    public String updateDesignation(@RequestBody EmployeeDesignationDTO dto) {
        return employeeDesignationService.updateDesignation(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteDesignation(@PathVariable int id) {
        return employeeDesignationService.deleteDesignation(id);
    }
}