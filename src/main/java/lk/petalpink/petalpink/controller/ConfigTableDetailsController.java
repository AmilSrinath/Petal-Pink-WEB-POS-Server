package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ConfigTableDetailsDTO;
import lk.petalpink.petalpink.service.ConfigTableDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config-table-details")
@CrossOrigin(origins = "http://localhost:5173")
public class ConfigTableDetailsController {

    @Autowired
    private ConfigTableDetailsService configTableDetailsService;

    @PostMapping
    public String createConfigTableDetails(@RequestBody ConfigTableDetailsDTO dto) {
        return configTableDetailsService.createConfigTableDetails(dto);
    }

    @GetMapping
    public List<ConfigTableDetailsDTO> getAllConfigTableDetails() {
        return configTableDetailsService.getAllConfigTableDetails();
    }

    @GetMapping("/by-config/{configTableId}")
    public List<ConfigTableDetailsDTO> getDetailsByConfigTableId(@PathVariable int configTableId) {
        return configTableDetailsService.getDetailsByConfigTableId(configTableId);
    }

    @PutMapping
    public String updateConfigTableDetails(@RequestBody ConfigTableDetailsDTO dto) {
        return configTableDetailsService.updateConfigTableDetails(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteConfigTableDetails(@PathVariable int id) {
        return configTableDetailsService.deleteConfigTableDetails(id);
    }
}