package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.UserDTO;
import lk.petalpink.petalpink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserDTO getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDTO checkPassword(String username, String rawPassword) {
        UserDTO user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        user.setPassword(null); // clear password before returning
        return user;
    }
}