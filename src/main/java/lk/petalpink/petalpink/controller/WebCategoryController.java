package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebMainCategoryDTO;
import lk.petalpink.petalpink.dto.website.WebSubCategoryDTO;
import lk.petalpink.petalpink.service.WebCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/category")
@CrossOrigin(origins = "*")
public class WebCategoryController {

    @Autowired
    private WebCategoryService webCategoryService;

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/category/main/save */
    @PostMapping("/main/save")
    public ResponseEntity<Map<String, Object>> saveMainCategory(@RequestBody Map<String, Object> body) {
        String mainCategoryName = (String) body.get("main_category_name");
        Integer userId = (Integer) body.get("user_id");
        int id = webCategoryService.saveMainCategory(mainCategoryName, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Main category saved successfully.", "main_category_id", id));
    }

    /** GET /api/website/category/main/all */
    @GetMapping("/main/all")
    public ResponseEntity<Map<String, Object>> getAllMainCategories() {
        List<WebMainCategoryDTO> categories = webCategoryService.getAllMainCategories();
        return ResponseEntity.ok(Map.of("mainCategories", categories));
    }

    /** GET /api/website/category/main/{mainCategoryId} */
    @GetMapping("/main/{mainCategoryId}")
    public ResponseEntity<WebMainCategoryDTO> getMainCategoryById(@PathVariable Integer mainCategoryId) {
        return ResponseEntity.ok(webCategoryService.getMainCategoryById(mainCategoryId));
    }

    /** PUT /api/website/category/main/{mainCategoryId} */
    @PutMapping("/main/{mainCategoryId}")
    public ResponseEntity<Map<String, String>> updateMainCategory(
            @PathVariable Integer mainCategoryId,
            @RequestBody Map<String, Object> body) {
        String mainCategoryName = (String) body.get("main_category_name");
        Integer userId = (Integer) body.get("user_id");
        webCategoryService.updateMainCategory(mainCategoryId, mainCategoryName, userId);
        return ResponseEntity.ok(Map.of("message", "Main category updated successfully."));
    }

    /** DELETE /api/website/category/main/{mainCategoryId} */
    @DeleteMapping("/main/{mainCategoryId}")
    public ResponseEntity<Map<String, String>> deleteMainCategory(@PathVariable Integer mainCategoryId) {
        webCategoryService.deleteMainCategory(mainCategoryId);
        return ResponseEntity.ok(Map.of("message", "Main category deleted successfully."));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SUB CATEGORY
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/category/sub/save */
    @PostMapping("/sub/save")
    public ResponseEntity<Map<String, Object>> saveSubCategory(@RequestBody Map<String, Object> body) {
        String subCategoryName = (String) body.get("sub_category_name");
        Integer mainCategoryId = (Integer) body.get("main_category_id");
        Integer userId = (Integer) body.get("user_id");
        int id = webCategoryService.saveSubCategory(subCategoryName, mainCategoryId, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Sub category saved successfully.", "sub_category_id", id));
    }

    /** GET /api/website/category/sub/all */
    @GetMapping("/sub/all")
    public ResponseEntity<Map<String, Object>> getAllSubCategories() {
        List<WebSubCategoryDTO> categories = webCategoryService.getAllSubCategories();
        return ResponseEntity.ok(Map.of("subCategories", categories));
    }

    /** GET /api/website/category/sub/by-main/{mainCategoryId} */
    @GetMapping("/sub/by-main/{mainCategoryId}")
    public ResponseEntity<Map<String, Object>> getSubCategoriesByMainCategoryId(@PathVariable Integer mainCategoryId) {
        List<WebSubCategoryDTO> categories = webCategoryService.getSubCategoriesByMainCategoryId(mainCategoryId);
        return ResponseEntity.ok(Map.of("subCategories", categories));
    }

    /** GET /api/website/category/sub/{subCategoryId} */
    @GetMapping("/sub/{subCategoryId}")
    public ResponseEntity<WebSubCategoryDTO> getSubCategoryById(@PathVariable Integer subCategoryId) {
        return ResponseEntity.ok(webCategoryService.getSubCategoryById(subCategoryId));
    }

    /** PUT /api/website/category/sub/{subCategoryId} */
    @PutMapping("/sub/{subCategoryId}")
    public ResponseEntity<Map<String, String>> updateSubCategory(
            @PathVariable Integer subCategoryId,
            @RequestBody Map<String, Object> body) {
        String subCategoryName = (String) body.get("sub_category_name");
        Integer mainCategoryId = (Integer) body.get("main_category_id");
        Integer userId = (Integer) body.get("user_id");
        webCategoryService.updateSubCategory(subCategoryId, subCategoryName, mainCategoryId, userId);
        return ResponseEntity.ok(Map.of("message", "Sub category updated successfully."));
    }

    /** DELETE /api/website/category/sub/{subCategoryId} */
    @DeleteMapping("/sub/{subCategoryId}")
    public ResponseEntity<Map<String, String>> deleteSubCategory(@PathVariable Integer subCategoryId) {
        webCategoryService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok(Map.of("message", "Sub category deleted successfully."));
    }
}