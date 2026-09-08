package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.UserRoleDTO;
import lk.petalpink.petalpink.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<UserRoleDTO> getAllRoles() {
        return userRoleRepository.findAll();
    }

    public UserRoleDTO getRoleById(int roleId) {
        return userRoleRepository.findById(roleId);
    }

    public String createRole(UserRoleDTO dto) {
        int rows = userRoleRepository.save(dto);
        return rows > 0 ? "Role created successfully" : "Failed to create role";
    }

    public String updateRole(UserRoleDTO dto) {
        int rows = userRoleRepository.update(dto);
        return rows > 0 ? "Role updated successfully" : "Role not found or update failed";
    }

    public String deleteRole(int roleId) {
        int rows = userRoleRepository.softDelete(roleId);
        return rows > 0 ? "Role deleted successfully" : "Role not found";
    }
}