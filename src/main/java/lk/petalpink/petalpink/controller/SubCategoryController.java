package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.SubCategoryDTO;
import lk.petalpink.petalpink.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-categories")
@CrossOrigin(origins = "http://localhost:5173")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping
    public String createSubCategory(@RequestBody SubCategoryDTO dto) {
        return subCategoryService.createSubCategory(dto);
    }

    @GetMapping
    public List<SubCategoryDTO> getAllSubCategories() {
        return subCategoryService.getAllSubCategories();
    }

    @GetMapping("/by-main/{mainItemCategoryId}")
    public List<SubCategoryDTO> getByMainCategory(@PathVariable int mainItemCategoryId) {
        return subCategoryService.getSubCategoriesByMainCategory(mainItemCategoryId);
    }

    @PutMapping
    public String updateSubCategory(@RequestBody SubCategoryDTO dto) {
        return subCategoryService.updateSubCategory(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteSubCategory(@PathVariable int id) {
        return subCategoryService.deleteSubCategory(id);
    }
}