package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ModuleAccessDTO;
import lk.petalpink.petalpink.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    // All modules with has_access true/false
    public List<ModuleAccessDTO> getModuleAccessByUser(int userId) {
        return moduleRepository.getModuleAccessByUser(userId);
    }

    // Only modules the user can access
    public List<ModuleAccessDTO> getAccessibleModulesByUser(int userId) {
        return moduleRepository.getAccessibleModulesByUser(userId);
    }
}