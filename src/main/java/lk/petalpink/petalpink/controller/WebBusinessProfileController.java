package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteBusinessProfileDTO;
import lk.petalpink.petalpink.service.WebBusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/business-profile")
@CrossOrigin(origins = "*")
public class WebBusinessProfileController {

    @Autowired
    private WebBusinessProfileService webBusinessProfileService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createBusinessProfile(@RequestBody WebsiteBusinessProfileDTO dto) {
        int id = webBusinessProfileService.createBusinessProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Business Profile created successfully", "business_id", id));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBusinessProfiles() {
        List<WebsiteBusinessProfileDTO> profiles = webBusinessProfileService.getAllBusinessProfiles();
        return ResponseEntity.ok(Map.of("configs", profiles));
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<Map<String, String>> updateBusinessProfile(
            @PathVariable("businessId") Integer businessId,
            @RequestBody WebsiteBusinessProfileDTO dto) {
        webBusinessProfileService.updateBusinessProfile(businessId, dto);
        return ResponseEntity.ok(Map.of("message", "Business Profile updated successfully"));
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<Map<String, String>> deleteBusinessProfile(@PathVariable("businessId") Integer businessId) {
        webBusinessProfileService.deleteBusinessProfile(businessId);
        return ResponseEntity.ok(Map.of("message", "Business Profile deleted successfully"));
    }
}