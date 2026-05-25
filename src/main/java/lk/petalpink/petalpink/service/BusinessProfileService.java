package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.BusinessProfileDTO;
import lk.petalpink.petalpink.repository.BusinessProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessProfileService {

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    // ── FETCH ALL ─────────────────────────────────────────────────────────────
    public List<BusinessProfileDTO> getAllBusinessProfiles() {
        return businessProfileRepository.findAll();
    }

    // ── FETCH BY ID ───────────────────────────────────────────────────────────
    public BusinessProfileDTO getBusinessProfileById(Integer id) {
        return businessProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business profile not found with id: " + id));
    }

    // ── SAVE ──────────────────────────────────────────────────────────────────
    public int saveBusinessProfile(BusinessProfileDTO dto) {
        if (dto.getBussinessProfileName() == null || dto.getBussinessProfileName().isBlank()) {
            throw new IllegalArgumentException("Business profile name must not be empty");
        }
        return businessProfileRepository.save(dto);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public void updateBusinessProfile(Integer id, BusinessProfileDTO dto) {
        // Ensure profile exists before updating
        businessProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business profile not found with id: " + id));

        dto.setBussinessProfileId(id);
        int rows = businessProfileRepository.update(dto);
        if (rows == 0) {
            throw new RuntimeException("Update failed for business profile id: " + id);
        }
    }

    // ── SOFT DELETE ───────────────────────────────────────────────────────────
    public void deleteBusinessProfile(Integer id) {
        int rows = businessProfileRepository.softDelete(id);
        if (rows == 0) {
            throw new RuntimeException("Business profile not found with id: " + id);
        }
    }
}