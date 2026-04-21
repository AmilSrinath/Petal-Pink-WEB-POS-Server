package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.ModuleAccessDTO;
import lk.petalpink.petalpink.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module")
@CrossOrigin(origins = "http://localhost:5173")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // Returns all modules — has_access: true/false per user
    @GetMapping("/access/{userId}")
    public List<ModuleAccessDTO> getModuleAccess(@PathVariable int userId) {
        return moduleService.getModuleAccessByUser(userId);
    }

    // Returns only modules the user has access to
    @GetMapping("/accessible/{userId}")
    public List<ModuleAccessDTO> getAccessibleModules(@PathVariable int userId) {
        return moduleService.getAccessibleModulesByUser(userId);
    }
}