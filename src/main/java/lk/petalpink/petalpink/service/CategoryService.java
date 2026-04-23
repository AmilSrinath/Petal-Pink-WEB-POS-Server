package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CategoryDTO;
import lk.petalpink.petalpink.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public String createCategory(CategoryDTO dto) {
        int rows = categoryRepository.save(dto);
        return rows > 0 ? "Category created successfully" : "Failed to create category";
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll();
    }

    public String updateCategory(CategoryDTO dto) {
        int rows = categoryRepository.update(dto);
        return rows > 0 ? "Category updated successfully" : "Category not found or update failed";
    }

    public String deleteCategory(int mainItemCategoryId) {
        int rows = categoryRepository.softDelete(mainItemCategoryId);
        return rows > 0 ? "Category deleted successfully" : "Category not found";
    }
}