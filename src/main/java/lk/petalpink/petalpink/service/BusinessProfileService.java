package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.BusinessProfileDTO;
import lk.petalpink.petalpink.repository.BusinessProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessProfileService {

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    public List<BusinessProfileDTO> getAllBusinessProfiles() {
        return businessProfileRepository.findAll();
    }
}