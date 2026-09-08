package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.repository.WebProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebProductService {

    @Autowired
    private WebProductRepository webProductRepository;

    public List<WebsiteProductDTO> getAllProducts() {
        List<WebsiteProductDTO> products = webProductRepository.getAllProducts();
        if (products.isEmpty()) throw new RuntimeException("No data found.");
        return products;
    }

    public WebsiteProductDTO getProductById(Integer productId) {
        WebsiteProductDTO product = webProductRepository.getProductById(productId);
        if (product == null) throw new RuntimeException("Product not found.");
        return product;
    }

    public WebsiteProductDTO getProductByName(String productName) {
        WebsiteProductDTO product = webProductRepository.getProductByName(productName);
        if (product == null) throw new RuntimeException("Product not found.");
        return product;
    }

    // ✅ images දැනටමත් GCS වල තියෙනවා — URL strings විතරයි DB එකට යන්නේ
    public void saveProduct(WebsiteProductSaveRequestDTO req) {
        webProductRepository.saveProduct(req, req.getImageUrl(), req.getImageUrl2(), req.getImageUrl3());
    }

    public void updateProduct(WebsiteProductUpdateRequestDTO req) {
        int rows = webProductRepository.updateProduct(req, req.getImageUrl(), req.getImageUrl2(), req.getImageUrl3());
        if (rows == 0) throw new RuntimeException("Product not found.");
    }

    public void deleteProduct(Integer productId) {
        if (productId == null) throw new IllegalArgumentException("Product ID is required.");
        int rows = webProductRepository.deleteProduct(productId);
        if (rows == 0) throw new RuntimeException("Product not found.");
    }
}