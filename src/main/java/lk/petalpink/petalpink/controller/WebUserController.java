package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteUserDTO;
import lk.petalpink.petalpink.dto.website.WebsiteUserUpdateDTO;
import lk.petalpink.petalpink.service.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/user")
@CrossOrigin(origins = "*")
public class WebUserController {

    @Autowired
    private WebUserService webUserService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> saveUser(
            @RequestParam("employeeId") String employeeId,
            @RequestParam("email")      String email,
            @RequestParam("password")   String password,
            @RequestParam("name")       String name,
            @RequestParam("nic")        String nic,
            @RequestParam("role")       String role,
            @RequestParam(value = "status",  defaultValue = "1") Integer status,
            @RequestParam(value = "visible", defaultValue = "1") Integer visible,
            @RequestParam(value = "image",   required = false)   MultipartFile image) {
        webUserService.saveUser(employeeId, email, password, name, nic, role, status, visible, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User saved successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<List<WebsiteUserDTO>> getAllUsers() {
        return ResponseEntity.ok(webUserService.getAllWebsiteUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<WebsiteUserDTO> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(webUserService.getUserByEmail(email));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody WebsiteUserUpdateDTO dto) {
        webUserService.updateUser(userId, dto);
        return ResponseEntity.ok(Map.of("message", "User updated successfully."));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("userId") String userId) {
        webUserService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        webUserService.forgotPassword(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Reset code sent successfully."));
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<Map<String, String>> verifyResetCode(@RequestBody Map<String, String> body) {
        webUserService.verifyResetCode(body.get("email"), body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Code verified successfully. Proceed to reset password."));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        webUserService.resetPassword(body.get("email"), body.get("password"), body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }

    @PutMapping("/update-password/{email}")
    public ResponseEntity<Map<String, String>> updatePassword(
            @PathVariable("email") String email,
            @RequestBody Map<String, String> body) {
        webUserService.updatePassword(email, body.get("password"));
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }

    @PutMapping(value = "/profile-picture/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateProfilePicture(
            @PathVariable("email") String email,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = webUserService.updateProfilePicture(email, image);
        return ResponseEntity.ok(Map.of("message", "Profile picture updated successfully.", "imageUrl", imageUrl));
    }
}