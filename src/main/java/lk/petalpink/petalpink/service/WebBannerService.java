package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteBannerDTO;
import lk.petalpink.petalpink.repository.WebBannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WebBannerService {

    @Autowired
    private WebBannerRepository webBannerRepository;

    @Autowired
    private WebsiteGcpStorageService gcpStorageService;

    public int saveBanner(String title, String subtitle, Integer userId, MultipartFile image) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "banners/" + System.currentTimeMillis() + "-image_url.jpg");
        }
        if (title == null || subtitle == null || imageUrl == null || userId == null) {
            throw new IllegalArgumentException("title, subtitle, image_url, and user_id are required");
        }
        return webBannerRepository.saveBanner(title, subtitle, imageUrl, userId);
    }

    public void updateBanner(Integer id, String title, String subtitle, Integer userId,
                             String existingImageUrl, MultipartFile image) {
        String imageUrl = existingImageUrl;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "banners/" + System.currentTimeMillis() + "-image_url.jpg");
        }
        int rows = webBannerRepository.updateBanner(id, title, subtitle, imageUrl, userId);
        if (rows == 0) throw new RuntimeException("Banner not found or already inactive.");
    }

    public void deleteBanner(Integer id) {
        int rows = webBannerRepository.deleteBanner(id);
        if (rows == 0) throw new RuntimeException("Banner not found or already inactive.");
    }

    public List<WebsiteBannerDTO> getAllBanners() {
        // Returning an empty list (instead of throwing) lets the admin panel
        // render an empty state on a fresh site instead of a 500 error.
        return webBannerRepository.getAllBanners();
    }
}