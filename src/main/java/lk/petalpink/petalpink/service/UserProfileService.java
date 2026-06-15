package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.UpdateProfileDTO;
import lk.petalpink.petalpink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void updateProfile(Integer userId, UpdateProfileDTO dto) {
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            userRepository.updateUsername(userId, dto.getUsername());
        }
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            String hashed = passwordEncoder.encode(dto.getNewPassword());
            userRepository.updatePassword(userId, hashed);
        }
    }
}