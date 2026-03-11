package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.PaymentTypeDTO;
import lk.petalpink.petalpink.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-types")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentTypeController {
    @Autowired
    private PaymentTypeService service;

    @PostMapping
    public String create(@RequestBody PaymentTypeDTO dto) { return service.create(dto); }

    @GetMapping
    public List<PaymentTypeDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody PaymentTypeDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}