package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.BusinessProfileDTO;
import lk.petalpink.petalpink.service.BusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-profiles")
@CrossOrigin(origins = "http://localhost:5173")
public class BusinessProfileController {

    @Autowired
    private BusinessProfileService businessProfileService;

    // GET /api/business-profiles
    @GetMapping
    public ResponseEntity<List<BusinessProfileDTO>> getAll() {
        return ResponseEntity.ok(businessProfileService.getAllBusinessProfiles());
    }

    // GET /api/business-profiles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BusinessProfileDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(businessProfileService.getBusinessProfileById(id));
    }

    // POST /api/business-profiles
    @PostMapping
    public ResponseEntity<String> save(@RequestBody BusinessProfileDTO dto) {
        int generatedId = businessProfileService.saveBusinessProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Business profile saved with id: " + generatedId);
    }

    // PUT /api/business-profiles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id,
                                         @RequestBody BusinessProfileDTO dto) {
        businessProfileService.updateBusinessProfile(id, dto);
        return ResponseEntity.ok("Business profile updated successfully");
    }

    // DELETE /api/business-profiles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        businessProfileService.deleteBusinessProfile(id);
        return ResponseEntity.ok("Business profile deleted successfully");
    }
}