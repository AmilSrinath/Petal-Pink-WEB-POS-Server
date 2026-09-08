package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.UserRoleDTO;
import lk.petalpink.petalpink.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@CrossOrigin(origins = "*")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping
    public List<UserRoleDTO> getAllRoles() {
        return userRoleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public UserRoleDTO getRoleById(@PathVariable int id) {
        return userRoleService.getRoleById(id);
    }

    @PostMapping
    public String createRole(@RequestBody UserRoleDTO dto) {
        return userRoleService.createRole(dto);
    }

    @PutMapping
    public String updateRole(@RequestBody UserRoleDTO dto) {
        return userRoleService.updateRole(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable int id) {
        return userRoleService.deleteRole(id);
    }
}