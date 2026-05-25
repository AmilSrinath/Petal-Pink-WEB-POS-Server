package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.SubTableLocationDTO;
import lk.petalpink.petalpink.repository.SubTableLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTableLocationService {

    @Autowired
    private SubTableLocationRepository subTableLocationRepository;

    public String createSubTableLocation(SubTableLocationDTO dto) {
        int rows = subTableLocationRepository.save(dto);
        return rows > 0 ? "Sub table location created successfully" : "Failed to create sub table location";
    }

    public List<SubTableLocationDTO> getAllSubTableLocations() {
        return subTableLocationRepository.findAll();
    }

    public List<SubTableLocationDTO> getSubLocationsByMainId(int mainTableLocationId) {
        return subTableLocationRepository.findByMainLocationId(mainTableLocationId);
    }

    public String updateSubTableLocation(SubTableLocationDTO dto) {
        int rows = subTableLocationRepository.update(dto);
        return rows > 0 ? "Sub table location updated successfully" : "Sub table location not found or update failed";
    }

    public String deleteSubTableLocation(int subTableLocationId) {
        int rows = subTableLocationRepository.softDelete(subTableLocationId);
        return rows > 0 ? "Sub table location deleted successfully" : "Sub table location not found";
    }
}