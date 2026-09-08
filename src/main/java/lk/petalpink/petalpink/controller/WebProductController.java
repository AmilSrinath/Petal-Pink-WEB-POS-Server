package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.service.WebProductService;
import lk.petalpink.petalpink.service.WebsiteGcpStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/product")
@CrossOrigin(origins = "*")
public class WebProductController {

    @Autowired
    private WebProductService webProductService;

    @Autowired
    private WebsiteGcpStorageService gcpStorageService;

    @GetMapping("/all")
    public ResponseEntity<List<WebsiteProductDTO>> getAllProducts() {
        return ResponseEntity.ok(webProductService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<WebsiteProductDTO> getProductById(@PathVariable("productId") Integer productId) {
        return ResponseEntity.ok(webProductService.getProductById(productId));
    }

    @GetMapping("/by-name/{productName}")
    public ResponseEntity<WebsiteProductDTO> getProductByName(@PathVariable("productName") String productName) {
        return ResponseEntity.ok(webProductService.getProductByName(productName));
    }

    // ✅ NEW: Signed upload URL ලබාගන්න endpoint එක
    @PostMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getUploadUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType) {
        WebsiteGcpStorageService.SignedUrlResult result =
                gcpStorageService.generateSignedUploadUrl(fileName, contentType);
        return ResponseEntity.ok(Map.of(
                "uploadUrl", result.uploadUrl(),
                "publicUrl", result.publicUrl()
        ));
    }

    // ✅ දැන් JSON body එකක් විතරයි, MultipartFile නෑ
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveProduct(@RequestBody WebsiteProductSaveRequestDTO req) {
        webProductService.saveProduct(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product saved successfully."));
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateProduct(@RequestBody WebsiteProductUpdateRequestDTO req) {
        webProductService.updateProduct(req);
        return ResponseEntity.ok(Map.of("message", "Product updated successfully."));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("productId") Integer productId) {
        webProductService.deleteProduct(productId);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully."));
    }
}