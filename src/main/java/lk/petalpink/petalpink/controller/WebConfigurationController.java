package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteConfigDTO;
import lk.petalpink.petalpink.service.WebConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/config")
@CrossOrigin(origins = "*")
public class WebConfigurationController {

    @Autowired
    private WebConfigurationService webConfigurationService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveConfig(@RequestBody WebsiteConfigDTO dto) {
        int configId = webConfigurationService.saveConfig(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Configuration saved successfully", "config_id", configId));
    }

    @PostMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllConfig() {
        List<WebsiteConfigDTO> configs = webConfigurationService.getAllConfigs();
        return ResponseEntity.ok(Map.of("configs", configs));
    }
}