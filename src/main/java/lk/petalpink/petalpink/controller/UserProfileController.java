package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.UpdateProfileDTO;
import lk.petalpink.petalpink.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Integer userId,
            @RequestBody UpdateProfileDTO dto) {
        try {
            userProfileService.updateProfile(userId, dto);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}