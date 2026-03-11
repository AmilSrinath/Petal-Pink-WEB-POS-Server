package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.CourierCompanyDTO;
import lk.petalpink.petalpink.service.CourierCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courier-companies")
@CrossOrigin(origins = "http://localhost:5173")
public class CourierCompanyController {
    @Autowired
    private CourierCompanyService service;

    @PostMapping
    public String create(@RequestBody CourierCompanyDTO dto) { return service.create(dto); }

    @GetMapping
    public List<CourierCompanyDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody CourierCompanyDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}