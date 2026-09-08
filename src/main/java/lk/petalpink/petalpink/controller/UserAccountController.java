package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.UserAccountDTO;
import lk.petalpink.petalpink.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-accounts")
@CrossOrigin(origins = "*")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping
    public List<UserAccountDTO> getAllUserAccounts() {
        return userAccountService.getAllUserAccounts();
    }

    @GetMapping("/{id}")
    public UserAccountDTO getUserAccountById(@PathVariable int id) {
        return userAccountService.getUserAccountById(id);
    }

    @PostMapping
    public ResponseEntity<String> createUserAccount(@RequestBody UserAccountDTO dto) {
        try {
            return ResponseEntity.ok(userAccountService.createUserAccount(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUserAccount(@RequestBody UserAccountDTO dto) {
        try {
            return ResponseEntity.ok(userAccountService.updateUserAccount(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUserAccount(@PathVariable int id) {
        return userAccountService.deleteUserAccount(id);
    }
}