package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.UserAccountDTO;
import lk.petalpink.petalpink.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserAccountDTO> getAllUserAccounts() {
        return userAccountRepository.findAll();
    }

    public UserAccountDTO getUserAccountById(int userId) {
        return userAccountRepository.findById(userId);
    }

    public String createUserAccount(UserAccountDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (userAccountRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        normalizeForeignKeys(dto);
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        int rows = userAccountRepository.save(dto);
        return rows > 0 ? "User account created successfully" : "Failed to create user account";
    }

    public String updateUserAccount(UserAccountDTO dto) {
        if (dto.getUserId() == null) {
            throw new RuntimeException("userId is required for update");
        }

        normalizeForeignKeys(dto);
        int rows;
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
            rows = userAccountRepository.updateWithPassword(dto);
        } else {
            rows = userAccountRepository.update(dto);
        }
        return rows > 0 ? "User account updated successfully" : "User account not found or update failed";
    }

    public String deleteUserAccount(int userId) {
        int rows = userAccountRepository.softDelete(userId);
        return rows > 0 ? "User account deleted successfully" : "User account not found";
    }

    // the frontend "Select Employee / Select Role" placeholder sends 0 which is not a real
    // foreign key, so treat it as "unset" to avoid a foreign key constraint violation
    private void normalizeForeignKeys(UserAccountDTO dto) {
        if (dto.getEmployeeId() != null && dto.getEmployeeId() == 0) {
            dto.setEmployeeId(null);
        }
        if (dto.getRoleId() != null && dto.getRoleId() == 0) {
            dto.setRoleId(null);
        }
    }
}