package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteBusinessProfileDTO;
import lk.petalpink.petalpink.repository.WebBusinessProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebBusinessProfileService {

    @Autowired
    private WebBusinessProfileRepository webBusinessProfileRepository;

    public int createBusinessProfile(WebsiteBusinessProfileDTO dto) {
        if (dto.getBusinessName() == null || dto.getBusinessName().isBlank()) {
            throw new IllegalArgumentException("Business name is required");
        }
        return webBusinessProfileRepository.createBusinessProfile(dto);
    }

    public List<WebsiteBusinessProfileDTO> getAllBusinessProfiles() {
        return webBusinessProfileRepository.getAllBusinessProfiles();
    }

    public void updateBusinessProfile(Integer businessId, WebsiteBusinessProfileDTO dto) {
        int rows = webBusinessProfileRepository.updateBusinessProfile(businessId, dto);
        if (rows == 0) throw new RuntimeException("Business Profile not found or inactive");
    }

    public void deleteBusinessProfile(Integer businessId) {
        int rows = webBusinessProfileRepository.deleteBusinessProfile(businessId);
        if (rows == 0) throw new RuntimeException("Business Profile not found");
    }
}