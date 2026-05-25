package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.SubTableLocationDTO;
import lk.petalpink.petalpink.service.SubTableLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-table-locations")
@CrossOrigin(origins = "http://localhost:5173")
public class SubTableLocationController {

    @Autowired
    private SubTableLocationService subTableLocationService;

    @PostMapping
    public String createSubTableLocation(@RequestBody SubTableLocationDTO dto) {
        return subTableLocationService.createSubTableLocation(dto);
    }

    @GetMapping
    public List<SubTableLocationDTO> getAllSubTableLocations() {
        return subTableLocationService.getAllSubTableLocations();
    }

    @GetMapping("/by-main/{mainId}")
    public List<SubTableLocationDTO> getSubLocationsByMainId(@PathVariable int mainId) {
        return subTableLocationService.getSubLocationsByMainId(mainId);
    }

    @PutMapping
    public String updateSubTableLocation(@RequestBody SubTableLocationDTO dto) {
        return subTableLocationService.updateSubTableLocation(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteSubTableLocation(@PathVariable int id) {
        return subTableLocationService.deleteSubTableLocation(id);
    }
}