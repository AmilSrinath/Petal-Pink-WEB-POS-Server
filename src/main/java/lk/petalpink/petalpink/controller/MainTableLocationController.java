package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.MainTableLocationDTO;
import lk.petalpink.petalpink.service.MainTableLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main-table-locations")
@CrossOrigin(origins = "http://localhost:5173")
public class MainTableLocationController {

    @Autowired
    private MainTableLocationService mainTableLocationService;

    @PostMapping
    public String createMainTableLocation(@RequestBody MainTableLocationDTO dto) {
        return mainTableLocationService.createMainTableLocation(dto);
    }

    @GetMapping
    public List<MainTableLocationDTO> getAllMainTableLocations() {
        return mainTableLocationService.getAllMainTableLocations();
    }

    @PutMapping
    public String updateMainTableLocation(@RequestBody MainTableLocationDTO dto) {
        return mainTableLocationService.updateMainTableLocation(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteMainTableLocation(@PathVariable int id) {
        return mainTableLocationService.deleteMainTableLocation(id);
    }
}