package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.CourierBranchDTO;
import lk.petalpink.petalpink.service.CourierBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courier-branches") @CrossOrigin(origins = "http://localhost:5173")
public class CourierBranchController {
    @Autowired
    private CourierBranchService service;

    @PostMapping
    public String create(@RequestBody CourierBranchDTO dto) { return service.create(dto); }

    @GetMapping
    public List<CourierBranchDTO> getAll() { return service.getAll(); }

    @GetMapping("/by-company/{companyId}")
    public List<CourierBranchDTO> getByCompany(@PathVariable int companyId) { return service.getByCompany(companyId); }

    @PutMapping
    public String update(@RequestBody CourierBranchDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}