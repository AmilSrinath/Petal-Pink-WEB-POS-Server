package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.CategoryDTO;
import lk.petalpink.petalpink.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public String createCategory(@RequestBody CategoryDTO dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping
    public String updateCategory(@RequestBody CategoryDTO dto) {
        return categoryService.updateCategory(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable int id) {
        return categoryService.deleteCategory(id);
    }
}