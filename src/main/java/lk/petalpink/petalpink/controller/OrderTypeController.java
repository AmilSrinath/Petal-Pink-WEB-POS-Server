package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.OrderTypeDTO;
import lk.petalpink.petalpink.service.OrderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-types") @CrossOrigin(origins = "http://localhost:5173")
public class OrderTypeController {
    @Autowired
    private OrderTypeService service;

    @PostMapping
    public String create(@RequestBody OrderTypeDTO dto) { return service.create(dto); }

    @GetMapping
    public List<OrderTypeDTO> getAll() { return service.getAll(); }

    @PutMapping
    public String update(@RequestBody OrderTypeDTO dto) { return service.update(dto); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) { return service.delete(id); }
}