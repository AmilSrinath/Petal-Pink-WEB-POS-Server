package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ReasonDTO;
import lk.petalpink.petalpink.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reasons")
@CrossOrigin(origins = "http://localhost:5173")
public class ReasonController {
    @Autowired private ReasonService service;

    @PostMapping
    public String create(@RequestBody ReasonDTO dto) { return service.create(dto); }

    @GetMapping
    public List<ReasonDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody ReasonDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}
