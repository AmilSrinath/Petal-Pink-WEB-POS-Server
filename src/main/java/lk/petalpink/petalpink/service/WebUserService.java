package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteUserDTO;
import lk.petalpink.petalpink.dto.website.WebsiteUserUpdateDTO;
import lk.petalpink.petalpink.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WebUserService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private WebsiteGcpStorageService gcpStorageService;

    @Autowired
    private JavaMailSender mailSender;

    public void saveUser(String employeeId, String email, String password, String name,
                         String nic, String role, Integer status, Integer visible, MultipartFile image) {
        if (employeeId == null || email == null || password == null || name == null || nic == null || role == null) {
            throw new IllegalArgumentException("All fields are required.");
        }
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "user/" + System.currentTimeMillis() + "-" + email + "-profile.jpg");
        }
        webUserRepository.saveUser(employeeId, email, password, name, nic, role, status, visible, imageUrl);
    }

    public List<WebsiteUserDTO> getAllWebsiteUsers() {
        return webUserRepository.getAllWebsiteUsers();
    }

    public WebsiteUserDTO getUserByEmail(String email) {
        WebsiteUserDTO user = webUserRepository.getUserByEmail(email);
        if (user == null) throw new RuntimeException("User not found.");
        return user;
    }

    public void updateUser(String userId, WebsiteUserUpdateDTO dto) {
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        if (dto.getEmail() == null || dto.getRole() == null) {
            throw new IllegalArgumentException("Please fill all fields to update the user.");
        }
        int rows = webUserRepository.updateUser(userId, dto);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public void deleteUser(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        int rows = webUserRepository.deleteUser(userId);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public void forgotPassword(String email) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        String resetCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        int rows = webUserRepository.setResetCode(email, resetCode);
        if (rows == 0) throw new RuntimeException("User not found.");
        sendResetCodeEmail(email, resetCode);
    }

    public void verifyResetCode(String email, String code) {
        if (email == null || code == null) throw new IllegalArgumentException("Email and code are required.");
        webUserRepository.verifyResetCode(email, code);
    }

    public void resetPassword(String email, String password, String code) {
        if (email == null || password == null || code == null) {
            throw new IllegalArgumentException("Email, password, and reset code are required.");
        }
        webUserRepository.verifyResetCode(email, code);
        webUserRepository.resetPassword(email, password);
    }

    public void updatePassword(String email, String password) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        if (password == null) throw new IllegalArgumentException("Password is required.");
        int rows = webUserRepository.updatePassword(email, password);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public String updateProfilePicture(String email, MultipartFile image) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("Image is required.");
        String imageUrl = gcpStorageService.uploadFile(image, "user/" + System.currentTimeMillis() + "-" + email + "-profile.jpg");
        int rows = webUserRepository.updateProfilePicture(email, imageUrl);
        if (rows == 0) throw new RuntimeException("User not found.");
        return imageUrl;
    }

    private void sendResetCodeEmail(String email, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password Reset Code");
        msg.setText("Do not share this email with anyone. Your password reset code is: " + code);
        mailSender.send(msg);
    }
}