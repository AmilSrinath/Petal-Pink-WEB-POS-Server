package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ConfigTableDetailsDTO;
import lk.petalpink.petalpink.repository.ConfigTableDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigTableDetailsService {

    @Autowired
    private ConfigTableDetailsRepository configTableDetailsRepository;

    public String createConfigTableDetails(ConfigTableDetailsDTO dto) {
        int rows = configTableDetailsRepository.save(dto);
        return rows > 0 ? "Config table details created successfully" : "Failed to create config table details";
    }

    public List<ConfigTableDetailsDTO> getAllConfigTableDetails() {
        return configTableDetailsRepository.findAll();
    }

    public List<ConfigTableDetailsDTO> getDetailsByConfigTableId(int configTableId) {
        return configTableDetailsRepository.findByConfigTableId(configTableId);
    }

    public String updateConfigTableDetails(ConfigTableDetailsDTO dto) {
        int rows = configTableDetailsRepository.update(dto);
        return rows > 0 ? "Config table details updated successfully" : "Config table details not found or update failed";
    }

    public String deleteConfigTableDetails(int tableId) {
        int rows = configTableDetailsRepository.softDelete(tableId);
        return rows > 0 ? "Config table details deleted successfully" : "Config table details not found";
    }
}