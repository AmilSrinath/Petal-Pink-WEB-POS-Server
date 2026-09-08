package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.EmployeeTitleDTO;
import lk.petalpink.petalpink.repository.EmployeeTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTitleService {

    @Autowired
    private EmployeeTitleRepository employeeTitleRepository;

    public List<EmployeeTitleDTO> getAllTitles() {
        return employeeTitleRepository.findAll();
    }

    public EmployeeTitleDTO getTitleById(int titleId) {
        return employeeTitleRepository.findById(titleId);
    }

    public String createTitle(EmployeeTitleDTO dto) {
        int rows = employeeTitleRepository.save(dto);
        return rows > 0 ? "Title created successfully" : "Failed to create title";
    }

    public String updateTitle(EmployeeTitleDTO dto) {
        int rows = employeeTitleRepository.update(dto);
        return rows > 0 ? "Title updated successfully" : "Title not found or update failed";
    }

    public String deleteTitle(int titleId) {
        int rows = employeeTitleRepository.softDelete(titleId);
        return rows > 0 ? "Title deleted successfully" : "Title not found";
    }
}