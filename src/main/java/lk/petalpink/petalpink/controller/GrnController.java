package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.GrnDTO;
import lk.petalpink.petalpink.dto.GrnItemDetailDTO;
import lk.petalpink.petalpink.dto.GrnRequestDTO;
import lk.petalpink.petalpink.dto.GrnUpdateRequestDTO;
import lk.petalpink.petalpink.service.GrnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grn")
@CrossOrigin(origins = "*")
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

    @GetMapping("/{id}/items")
    public List<GrnItemDetailDTO> getGrnItems(@PathVariable int id) {
        return grnService.getGrnItems(id);
    }

    @GetMapping("/next-invoice-no")
    public String getNextInvoiceNo() {
        return grnService.getNextInvoiceNo();
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

    @PutMapping("/transaction")
    public String updateGrnTransaction(@RequestBody GrnUpdateRequestDTO request) {
        return grnService.updateGrnTransaction(request);
    }
}