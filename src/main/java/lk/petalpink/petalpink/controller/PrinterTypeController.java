package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.PrinterTypeDTO;
import lk.petalpink.petalpink.service.PrinterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/printer-types")
@CrossOrigin(origins = "http://localhost:5173")
public class PrinterTypeController {
    @Autowired
    private PrinterTypeService service;

    @PostMapping
    public String create(@RequestBody PrinterTypeDTO dto) { return service.create(dto); }

    @GetMapping
    public List<PrinterTypeDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody PrinterTypeDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}