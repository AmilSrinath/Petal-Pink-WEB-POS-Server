package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.SubItemCategoryDTO;
import lk.petalpink.petalpink.service.SubItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-item-category")
@CrossOrigin(origins = "http://localhost:5173")
public class SubItemCategoryController {

    @Autowired
    private SubItemCategoryService subItemCategoryService;

    // GET ALL
    @GetMapping
    public List<SubItemCategoryDTO> getAll() {
        return subItemCategoryService.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public SubItemCategoryDTO getById(@PathVariable int id) {
        return subItemCategoryService.getById(id);
    }

    // CREATE
    @PostMapping
    public String save(@RequestBody SubItemCategoryDTO dto) {
        return subItemCategoryService.save(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public String update(@PathVariable int id, @RequestBody SubItemCategoryDTO dto) {
        return subItemCategoryService.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        return subItemCategoryService.delete(id);
    }
}