package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.GrnDTO;
import lk.petalpink.petalpink.dto.GrnRequestDTO;
import lk.petalpink.petalpink.service.GrnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
@CrossOrigin(origins = "http://localhost:5173")
public class GrnController {

    @Autowired
    private GrnService grnService;

    @PostMapping
    public String createGrn(@RequestBody GrnDTO dto) {
        return grnService.createGrn(dto);
    }

    @GetMapping
    public List<GrnDTO> getAllGrns() {
        return grnService.getAllGrns();
    }

    @PutMapping
    public String updateGrn(@RequestBody GrnDTO dto) {
        return grnService.updateGrn(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteGrn(@PathVariable int id) {
        return grnService.deleteGrn(id);
    }

    @PostMapping("/transaction")
    public String createGrnTransaction(@RequestBody GrnRequestDTO request) {
        return grnService.createGrnTransaction(request);
    }
}