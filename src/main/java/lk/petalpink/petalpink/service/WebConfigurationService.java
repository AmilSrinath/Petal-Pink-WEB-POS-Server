package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteConfigDTO;
import lk.petalpink.petalpink.repository.WebConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebConfigurationService {

    @Autowired
    private WebConfigurationRepository webConfigurationRepository;

    public int saveConfig(WebsiteConfigDTO dto) {
        if (dto.getConfigName() == null || dto.getConfigValue() == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("config_name, config_value, and user_id are required");
        }
        return webConfigurationRepository.saveConfig(dto);
    }

    public List<WebsiteConfigDTO> getAllConfigs() {
        return webConfigurationRepository.getAllConfigs();
    }
}