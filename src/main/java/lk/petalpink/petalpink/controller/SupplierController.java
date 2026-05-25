package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.SupplierDTO;
import lk.petalpink.petalpink.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:5173")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public String createSupplier(@RequestBody SupplierDTO dto) {
        return supplierService.createSupplier(dto);
    }

    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping
    public String updateSupplier(@RequestBody SupplierDTO dto) {
        return supplierService.updateSupplier(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable int id) {
        return supplierService.deleteSupplier(id);
    }
}