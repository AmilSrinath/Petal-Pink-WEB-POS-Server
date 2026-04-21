package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.SubItemCategoryDTO;
import lk.petalpink.petalpink.repository.SubItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubItemCategoryService {

    @Autowired
    private SubItemCategoryRepository subItemCategoryRepository;

    public List<SubItemCategoryDTO> getAll() {
        return subItemCategoryRepository.getAll();
    }

    public SubItemCategoryDTO getById(int id) {
        return subItemCategoryRepository.getById(id);
    }

    public String save(SubItemCategoryDTO dto) {
        int rows = subItemCategoryRepository.save(dto);
        return rows > 0 ? "Saved successfully" : "Save failed";
    }

    public String update(int id, SubItemCategoryDTO dto) {
        int rows = subItemCategoryRepository.update(id, dto);
        return rows > 0 ? "Updated successfully" : "Update failed or ID not found";
    }

    public String delete(int id) {
        int rows = subItemCategoryRepository.delete(id);
        return rows > 0 ? "Deleted successfully" : "Delete failed or ID not found";
    }
}