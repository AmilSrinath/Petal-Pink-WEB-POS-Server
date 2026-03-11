package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.StatusTypeDTO;
import lk.petalpink.petalpink.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<StatusTypeDTO> getStatusTypesByRegId(Integer regId) {
        return statusRepository.getStatusTypesByRegId(regId);
    }
}