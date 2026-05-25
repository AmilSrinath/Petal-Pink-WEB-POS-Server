package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.MainTableLocationDTO;
import lk.petalpink.petalpink.repository.MainTableLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainTableLocationService {

    @Autowired
    private MainTableLocationRepository mainTableLocationRepository;

    public String createMainTableLocation(MainTableLocationDTO dto) {
        int rows = mainTableLocationRepository.save(dto);
        return rows > 0 ? "Main table location created successfully" : "Failed to create main table location";
    }

    public List<MainTableLocationDTO> getAllMainTableLocations() {
        return mainTableLocationRepository.findAll();
    }

    public String updateMainTableLocation(MainTableLocationDTO dto) {
        int rows = mainTableLocationRepository.update(dto);
        return rows > 0 ? "Main table location updated successfully" : "Main table location not found or update failed";
    }

    public String deleteMainTableLocation(int mainTableLocationId) {
        int rows = mainTableLocationRepository.softDelete(mainTableLocationId);
        return rows > 0 ? "Main table location deleted successfully" : "Main table location not found";
    }
}