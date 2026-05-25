package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ConfigTableLocationDTO;
import lk.petalpink.petalpink.service.ConfigTableLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config-table-locations")
@CrossOrigin(origins = "http://localhost:5173")
public class ConfigTableLocationController {

    @Autowired
    private ConfigTableLocationService configTableLocationService;

    @PostMapping
    public String createConfigTableLocation(@RequestBody ConfigTableLocationDTO dto) {
        return configTableLocationService.createConfigTableLocation(dto);
    }

    @GetMapping
    public List<ConfigTableLocationDTO> getAllConfigTableLocations() {
        return configTableLocationService.getAllConfigTableLocations();
    }

    @PutMapping
    public String updateConfigTableLocation(@RequestBody ConfigTableLocationDTO dto) {
        return configTableLocationService.updateConfigTableLocation(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteConfigTableLocation(@PathVariable int id) {
        return configTableLocationService.deleteConfigTableLocation(id);
    }
}