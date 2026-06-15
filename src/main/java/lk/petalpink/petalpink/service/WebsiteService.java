package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WebsiteService {

    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private WebsiteGcpStorageService gcpStorageService;

    @Autowired
    private JavaMailSender mailSender;

    // ══════════════════════════════════════════════════════════════════════════
    //  CONFIGURATION
    // ══════════════════════════════════════════════════════════════════════════

    public int saveConfig(WebsiteConfigDTO dto) {
        if (dto.getConfigName() == null || dto.getConfigValue() == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("config_name, config_value, and user_id are required");
        }
        return websiteRepository.saveConfig(dto);
    }

    public List<WebsiteConfigDTO> getAllConfigs() {
        return websiteRepository.getAllConfigs();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BUSINESS PROFILE
    // ══════════════════════════════════════════════════════════════════════════

    public int createBusinessProfile(WebsiteBusinessProfileDTO dto) {
        if (dto.getBusinessName() == null || dto.getBusinessName().isBlank()) {
            throw new IllegalArgumentException("Business name is required");
        }
        return websiteRepository.createBusinessProfile(dto);
    }

    public List<WebsiteBusinessProfileDTO> getAllBusinessProfiles() {
        return websiteRepository.getAllBusinessProfiles();
    }

    public void updateBusinessProfile(Integer businessId, WebsiteBusinessProfileDTO dto) {
        int rows = websiteRepository.updateBusinessProfile(businessId, dto);
        if (rows == 0) throw new RuntimeException("Business Profile not found or inactive");
    }

    public void deleteBusinessProfile(Integer businessId) {
        int rows = websiteRepository.deleteBusinessProfile(businessId);
        if (rows == 0) throw new RuntimeException("Business Profile not found");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BANNERS
    // ══════════════════════════════════════════════════════════════════════════

    public int saveBanner(String title, String subtitle, Integer userId, MultipartFile image) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "banners/" + System.currentTimeMillis() + "-image_url.jpg");
        }
        if (title == null || subtitle == null || imageUrl == null || userId == null) {
            throw new IllegalArgumentException("title, subtitle, image_url, and user_id are required");
        }
        return websiteRepository.saveBanner(title, subtitle, imageUrl, userId);
    }

    public void updateBanner(Integer id, String title, String subtitle, Integer userId,
                             String existingImageUrl, MultipartFile image) {
        String imageUrl = existingImageUrl;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "banners/" + System.currentTimeMillis() + "-image_url.jpg");
        }
        int rows = websiteRepository.updateBanner(id, title, subtitle, imageUrl, userId);
        if (rows == 0) throw new RuntimeException("Banner not found or already inactive.");
    }

    public void deleteBanner(Integer id) {
        int rows = websiteRepository.deleteBanner(id);
        if (rows == 0) throw new RuntimeException("Banner not found or already inactive.");
    }

    public List<WebsiteBannerDTO> getAllBanners() {
        List<WebsiteBannerDTO> banners = websiteRepository.getAllBanners();
        if (banners.isEmpty()) throw new RuntimeException("No banners found.");
        return banners;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  COMMENTS
    // ══════════════════════════════════════════════════════════════════════════

    public int addComment(String content, String clientName, Integer userId, MultipartFile clientImg) {
        if (content == null || clientName == null || userId == null) {
            throw new IllegalArgumentException("content, clientName, and user_id are required");
        }
        String imgUrl = null;
        if (clientImg != null && !clientImg.isEmpty()) {
            imgUrl = gcpStorageService.uploadFile(clientImg, "comments/" + System.currentTimeMillis() + "-clientImg.jpg");
        }
        return websiteRepository.addComment(content, imgUrl, clientName, userId);
    }

    public void updateComment(Integer id, String content, String clientName, Integer userId, MultipartFile clientImg) {
        if (content == null || clientName == null || userId == null) {
            throw new IllegalArgumentException("content, clientName, and user_id are required");
        }
        String existingImg = websiteRepository.getCommentImage(id);
        String imgUrl = existingImg;
        if (clientImg != null && !clientImg.isEmpty()) {
            imgUrl = gcpStorageService.uploadFile(clientImg, "comments/" + System.currentTimeMillis() + "-clientImg.jpg");
        }
        int rows = websiteRepository.updateComment(id, content, imgUrl, clientName, userId);
        if (rows == 0) throw new RuntimeException("Comment not found.");
    }

    public void deleteComment(Integer id) {
        int rows = websiteRepository.deleteComment(id);
        if (rows == 0) throw new RuntimeException("Comment not found.");
    }

    public List<WebsiteCommentDTO> getAllComments() {
        List<WebsiteCommentDTO> comments = websiteRepository.getAllComments();
        if (comments.isEmpty()) throw new RuntimeException("No comments found.");
        return comments;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CUSTOMERS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteCustomerDTO> getAllCustomers() {
        return websiteRepository.getAllCustomers();
    }

    public long getCustomerCount() {
        return websiteRepository.getCustomerCount();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ORDERS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteOrderDTO> getAllOrders() {
        return websiteRepository.getAllOrders();
    }

    public WebsiteOrderDetailsResponseDTO getOrderDetails(String orderId) {
        WebsiteOrderDTO order = websiteRepository.getOrderWithCustomer(orderId);
        if (order == null) throw new RuntimeException("Order not found.");
        List<WebsiteOrderItemDTO> items = websiteRepository.getOrderItems(orderId);
        return new WebsiteOrderDetailsResponseDTO(order, items);
    }

    public List<WebsiteOrderItemDTO> getDetailsByOrderId(String orderId) {
        websiteRepository.markOrderInactive(orderId);
        List<WebsiteOrderItemDTO> items = websiteRepository.getOrderItemsSimple(orderId);
        if (items.isEmpty()) throw new RuntimeException("Order details not found.");
        return items;
    }

    public List<WebsiteNewOrderDTO> getNewOrderDetails() {
        List<WebsiteNewOrderDTO> orders = websiteRepository.getNewOrders();
        if (orders.isEmpty()) throw new RuntimeException("No new orders found.");
        for (WebsiteNewOrderDTO order : orders) {
            List<WebsiteOrderItemDTO> items = websiteRepository.getOrderItemsSimple(order.getOrderId());
            if (items.isEmpty()) throw new RuntimeException("Order details not found for order: " + order.getOrderId());
            order.setItems(items);
        }
        return orders;
    }

    public void markOrdersAsReceived() {
        websiteRepository.markOrdersAsReceived();
    }

    public void updateOrderStatus(String orderId, String status) {
        int rows = websiteRepository.updateOrderStatus(orderId, status);
        if (rows == 0) throw new RuntimeException("Order not found or status unchanged.");
    }

    public void updateTrackingNumber(String orderId, String trackingNumber) {
        websiteRepository.updateTrackingNumber(orderId, trackingNumber);
    }

    public String saveOrder(WebsiteSaveOrderRequestDTO dto) {
        return websiteRepository.saveOrder(dto);
    }

    public double getTodaySales() {
        return websiteRepository.getTodaySales();
    }

    public List<Double> getYearSales() {
        return websiteRepository.getYearSales();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PRODUCTS
    // ══════════════════════════════════════════════════════════════════════════

    public List<WebsiteProductDTO> getAllProducts() {
        List<WebsiteProductDTO> products = websiteRepository.getAllProducts();
        if (products.isEmpty()) throw new RuntimeException("No data found.");
        return products;
    }

    public WebsiteProductDTO getProductById(Integer productId) {
        WebsiteProductDTO product = websiteRepository.getProductById(productId);
        if (product == null) throw new RuntimeException("Product not found.");
        return product;
    }

    public WebsiteProductDTO getProductByName(String productName) {
        WebsiteProductDTO product = websiteRepository.getProductByName(productName);
        if (product == null) throw new RuntimeException("Product not found.");
        return product;
    }

    public void saveProduct(WebsiteProductSaveRequestDTO req) {
        String img1 = uploadIfPresent(req.getImageUrl(), "product/" + System.currentTimeMillis() + "-image_url.jpg");
        String img2 = uploadIfPresent(req.getImageUrl2(), "product/" + System.currentTimeMillis() + "-image_url_2.jpg");
        String img3 = uploadIfPresent(req.getImageUrl3(), "product/" + System.currentTimeMillis() + "-image_url_3.jpg");
        websiteRepository.saveProduct(req, img1, img2, img3);
    }

    public void updateProduct(WebsiteProductUpdateRequestDTO req) {
        String img1 = uploadIfPresent(req.getImageUrl(), "product/" + System.currentTimeMillis() + "-image_url.jpg");
        String img2 = uploadIfPresent(req.getImageUrl2(), "product/" + System.currentTimeMillis() + "-image_url_2.jpg");
        String img3 = uploadIfPresent(req.getImageUrl3(), "product/" + System.currentTimeMillis() + "-image_url_3.jpg");
        int rows = websiteRepository.updateProduct(req, img1, img2, img3);
        if (rows == 0) throw new RuntimeException("Product not found.");
    }

    public void deleteProduct(Integer productId) {
        if (productId == null) throw new IllegalArgumentException("Product ID is required.");
        int rows = websiteRepository.deleteProduct(productId);
        if (rows == 0) throw new RuntimeException("Product not found.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  WEBSITE USERS
    // ══════════════════════════════════════════════════════════════════════════

    public void saveUser(String employeeId, String email, String password, String name,
                         String nic, String role, Integer status, Integer visible, MultipartFile image) {
        if (employeeId == null || email == null || password == null || name == null || nic == null || role == null) {
            throw new IllegalArgumentException("All fields are required.");
        }
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = gcpStorageService.uploadFile(image, "user/" + System.currentTimeMillis() + "-" + email + "-profile.jpg");
        }
        websiteRepository.saveUser(employeeId, email, password, name, nic, role, status, visible, imageUrl);
    }

    public List<WebsiteUserDTO> getAllWebsiteUsers() {
        return websiteRepository.getAllWebsiteUsers();
    }

    public WebsiteUserDTO getUserByEmail(String email) {
        WebsiteUserDTO user = websiteRepository.getUserByEmail(email);
        if (user == null) throw new RuntimeException("User not found.");
        return user;
    }

    public void updateUser(String userId, WebsiteUserUpdateDTO dto) {
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        if (dto.getEmail() == null || dto.getRole() == null) {
            throw new IllegalArgumentException("Please fill all fields to update the user.");
        }
        int rows = websiteRepository.updateUser(userId, dto);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public void deleteUser(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID is required.");
        int rows = websiteRepository.deleteUser(userId);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public void forgotPassword(String email) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        String resetCode = String.valueOf((int)(Math.random() * 900000) + 100000);
        int rows = websiteRepository.setResetCode(email, resetCode);
        if (rows == 0) throw new RuntimeException("User not found.");
        sendResetCodeEmail(email, resetCode);
    }

    public void verifyResetCode(String email, String code) {
        if (email == null || code == null) throw new IllegalArgumentException("Email and code are required.");
        websiteRepository.verifyResetCode(email, code); // throws if invalid/expired
    }

    public void resetPassword(String email, String password, String code) {
        if (email == null || password == null || code == null) {
            throw new IllegalArgumentException("Email, password, and reset code are required.");
        }
        websiteRepository.verifyResetCode(email, code);
        websiteRepository.resetPassword(email, password);
    }

    public void updatePassword(String email, String password) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        if (password == null) throw new IllegalArgumentException("Password is required.");
        int rows = websiteRepository.updatePassword(email, password);
        if (rows == 0) throw new RuntimeException("User not found.");
    }

    public String updateProfilePicture(String email, MultipartFile image) {
        if (email == null) throw new IllegalArgumentException("Email is required.");
        if (image == null || image.isEmpty()) throw new IllegalArgumentException("Image is required.");
        String imageUrl = gcpStorageService.uploadFile(image, "user/" + System.currentTimeMillis() + "-" + email + "-profile.jpg");
        int rows = websiteRepository.updateProfilePicture(email, imageUrl);
        if (rows == 0) throw new RuntimeException("User not found.");
        return imageUrl;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String uploadIfPresent(MultipartFile file, String path) {
        if (file != null && !file.isEmpty()) {
            return gcpStorageService.uploadFile(file, path);
        }
        return null;
    }

    private void sendResetCodeEmail(String email, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password Reset Code");
        msg.setText("Do not share this email with anyone. Your password reset code is: " + code);
        mailSender.send(msg);
    }
}
