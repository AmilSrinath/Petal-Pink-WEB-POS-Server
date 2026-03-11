package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.UnitTypeDTO;
import lk.petalpink.petalpink.service.UnitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit-types")
@CrossOrigin(origins = "http://localhost:5173")
public class UnitTypeController {
    @Autowired private UnitTypeService service;

    @PostMapping
    public String create(@RequestBody UnitTypeDTO dto) { return service.create(dto); }

    @GetMapping
    public List<UnitTypeDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody UnitTypeDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}