package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteBannerDTO;
import lk.petalpink.petalpink.service.WebBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/banner")
@CrossOrigin(origins = "*")
public class WebBannerController {

    @Autowired
    private WebBannerService webBannerService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> saveBanner(
            @RequestParam("title")    String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("user_id")  Integer userId,
            @RequestParam(value = "image_url", required = false) MultipartFile image) {
        int bannerId = webBannerService.saveBanner(title, subtitle, userId, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Banner saved successfully.", "banner_id", bannerId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateBanner(
            @PathVariable("id") Integer id,
            @RequestParam("title")    String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("user_id")  Integer userId,
            @RequestParam(value = "image_url",  required = false) String existingImageUrl,
            @RequestParam(value = "image_file", required = false) MultipartFile image) {
        webBannerService.updateBanner(id, title, subtitle, userId, existingImageUrl, image);
        return ResponseEntity.ok(Map.of("message", "Banner updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBanner(@PathVariable("id") Integer id) {
        webBannerService.deleteBanner(id);
        return ResponseEntity.ok(Map.of("message", "Banner deleted successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBanners() {
        List<WebsiteBannerDTO> banners = webBannerService.getAllBanners();
        return ResponseEntity.ok(Map.of("banners", banners));
    }
}