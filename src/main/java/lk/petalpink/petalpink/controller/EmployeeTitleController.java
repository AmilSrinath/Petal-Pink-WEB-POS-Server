package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.EmployeeTitleDTO;
import lk.petalpink.petalpink.service.EmployeeTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-titles")
@CrossOrigin(origins = "*")
public class EmployeeTitleController {

    @Autowired
    private EmployeeTitleService employeeTitleService;

    @GetMapping
    public List<EmployeeTitleDTO> getAllTitles() {
        return employeeTitleService.getAllTitles();
    }

    @GetMapping("/{id}")
    public EmployeeTitleDTO getTitleById(@PathVariable int id) {
        return employeeTitleService.getTitleById(id);
    }

    @PostMapping
    public String createTitle(@RequestBody EmployeeTitleDTO dto) {
        return employeeTitleService.createTitle(dto);
    }

    @PutMapping
    public String updateTitle(@RequestBody EmployeeTitleDTO dto) {
        return employeeTitleService.updateTitle(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteTitle(@PathVariable int id) {
        return employeeTitleService.deleteTitle(id);
    }
}