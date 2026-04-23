package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.SubCategoryDTO;
import lk.petalpink.petalpink.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public String createSubCategory(SubCategoryDTO dto) {
        int rows = subCategoryRepository.save(dto);
        return rows > 0 ? "Sub category created successfully" : "Failed to create sub category";
    }

    public List<SubCategoryDTO> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    public List<SubCategoryDTO> getSubCategoriesByMainCategory(int mainItemCategoryId) {
        return subCategoryRepository.findByMainCategory(mainItemCategoryId);
    }

    public String updateSubCategory(SubCategoryDTO dto) {
        int rows = subCategoryRepository.update(dto);
        return rows > 0 ? "Sub category updated successfully" : "Sub category not found or update failed";
    }

    public String deleteSubCategory(int subItemCategoryId) {
        int rows = subCategoryRepository.softDelete(subItemCategoryId);
        return rows > 0 ? "Sub category deleted successfully" : "Sub category not found";
    }
}