package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ConfigTableLocationDTO;
import lk.petalpink.petalpink.repository.ConfigTableLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigTableLocationService {

    @Autowired
    private ConfigTableLocationRepository configTableLocationRepository;

    public String createConfigTableLocation(ConfigTableLocationDTO dto) {
        int rows = configTableLocationRepository.save(dto);
        return rows > 0 ? "Config table location created successfully" : "Failed to create config table location";
    }

    public List<ConfigTableLocationDTO> getAllConfigTableLocations() {
        return configTableLocationRepository.findAll();
    }

    public String updateConfigTableLocation(ConfigTableLocationDTO dto) {
        int rows = configTableLocationRepository.update(dto);
        return rows > 0 ? "Config table location updated successfully" : "Config table location not found or update failed";
    }

    public String deleteConfigTableLocation(int configTableLocationId) {
        int rows = configTableLocationRepository.softDelete(configTableLocationId);
        return rows > 0 ? "Config table location deleted successfully" : "Config table location not found";
    }
}