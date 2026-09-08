package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebMainCategoryDTO;
import lk.petalpink.petalpink.dto.website.WebSubCategoryDTO;
import lk.petalpink.petalpink.repository.WebCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebCategoryService {

    @Autowired
    private WebCategoryRepository webCategoryRepository;

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    public int saveMainCategory(String mainCategoryName, Integer userId) {
        if (mainCategoryName == null || mainCategoryName.isBlank()) {
            throw new IllegalArgumentException("Main category name is required.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }
        return webCategoryRepository.saveMainCategory(mainCategoryName, userId);
    }

    public List<WebMainCategoryDTO> getAllMainCategories() {
        // Empty list instead of throwing, so the admin panel can render an
        // empty state rather than surfacing a 500 error.
        return webCategoryRepository.getAllMainCategories();
    }

    public WebMainCategoryDTO getMainCategoryById(Integer mainCategoryId) {
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        WebMainCategoryDTO category = webCategoryRepository.getMainCategoryById(mainCategoryId);
        if (category == null) throw new RuntimeException("Main category not found.");
        return category;
    }

    public void updateMainCategory(Integer mainCategoryId, String mainCategoryName, Integer userId) {
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        if (mainCategoryName == null || mainCategoryName.isBlank()) {
            throw new IllegalArgumentException("Main category name is required.");
        }
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        int rows = webCategoryRepository.updateMainCategory(mainCategoryId, mainCategoryName, userId);
        if (rows == 0) throw new RuntimeException("Main category not found or already inactive.");
    }

    public void deleteMainCategory(Integer mainCategoryId) {
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        int rows = webCategoryRepository.deleteMainCategory(mainCategoryId);
        if (rows == 0) throw new RuntimeException("Main category not found.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SUB CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    public int saveSubCategory(String subCategoryName, Integer mainCategoryId, Integer userId) {
        if (subCategoryName == null || subCategoryName.isBlank()) {
            throw new IllegalArgumentException("Sub category name is required.");
        }
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        return webCategoryRepository.saveSubCategory(subCategoryName, mainCategoryId, userId);
    }

    public List<WebSubCategoryDTO> getAllSubCategories() {
        return webCategoryRepository.getAllSubCategories();
    }

    public List<WebSubCategoryDTO> getSubCategoriesByMainCategoryId(Integer mainCategoryId) {
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        return webCategoryRepository.getSubCategoriesByMainCategoryId(mainCategoryId);
    }

    public WebSubCategoryDTO getSubCategoryById(Integer subCategoryId) {
        if (subCategoryId == null) throw new IllegalArgumentException("Sub category ID is required.");
        WebSubCategoryDTO category = webCategoryRepository.getSubCategoryById(subCategoryId);
        if (category == null) throw new RuntimeException("Sub category not found.");
        return category;
    }

    public void updateSubCategory(Integer subCategoryId, String subCategoryName, Integer mainCategoryId, Integer userId) {
        if (subCategoryId == null) throw new IllegalArgumentException("Sub category ID is required.");
        if (subCategoryName == null || subCategoryName.isBlank()) {
            throw new IllegalArgumentException("Sub category name is required.");
        }
        if (mainCategoryId == null) throw new IllegalArgumentException("Main category ID is required.");
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        int rows = webCategoryRepository.updateSubCategory(subCategoryId, subCategoryName, mainCategoryId, userId);
        if (rows == 0) throw new RuntimeException("Sub category not found or already inactive.");
    }

    public void deleteSubCategory(Integer subCategoryId) {
        if (subCategoryId == null) throw new IllegalArgumentException("Sub category ID is required.");
        int rows = webCategoryRepository.deleteSubCategory(subCategoryId);
        if (rows == 0) throw new RuntimeException("Sub category not found.");
    }
}