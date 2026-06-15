package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website")
@CrossOrigin(origins = "*")
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;

    // ══════════════════════════════════════════════════════════════════════════
    //  CONFIGURATION
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/config/save */
    @PostMapping("/config/save")
    public ResponseEntity<Map<String, Object>> saveConfig(@RequestBody WebsiteConfigDTO dto) {
        int configId = websiteService.saveConfig(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Configuration saved successfully", "config_id", configId));
    }

    /** POST /api/website/config/all */
    @PostMapping("/config/all")
    public ResponseEntity<Map<String, Object>> getAllConfig() {
        List<WebsiteConfigDTO> configs = websiteService.getAllConfigs();
        return ResponseEntity.ok(Map.of("configs", configs));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BUSINESS PROFILE (website tables)
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/business-profile/create */
    @PostMapping("/business-profile/create")
    public ResponseEntity<Map<String, Object>> createBusinessProfile(@RequestBody WebsiteBusinessProfileDTO dto) {
        int id = websiteService.createBusinessProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Business Profile created successfully", "business_id", id));
    }

    /** GET /api/website/business-profile/all */
    @GetMapping("/business-profile/all")
    public ResponseEntity<Map<String, Object>> getAllBusinessProfiles() {
        List<WebsiteBusinessProfileDTO> profiles = websiteService.getAllBusinessProfiles();
        return ResponseEntity.ok(Map.of("configs", profiles));
    }

    /** PUT /api/website/business-profile/{businessId} */
    @PutMapping("/business-profile/{businessId}")
    public ResponseEntity<Map<String, String>> updateBusinessProfile(
            @PathVariable("businessId") Integer businessId,
            @RequestBody WebsiteBusinessProfileDTO dto) {
        websiteService.updateBusinessProfile(businessId, dto);
        return ResponseEntity.ok(Map.of("message", "Business Profile updated successfully"));
    }

    /** DELETE /api/website/business-profile/{businessId} */
    @DeleteMapping("/business-profile/{businessId}")
    public ResponseEntity<Map<String, String>> deleteBusinessProfile(@PathVariable("businessId") Integer businessId) {
        websiteService.deleteBusinessProfile(businessId);
        return ResponseEntity.ok(Map.of("message", "Business Profile deleted successfully"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BANNERS
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/banner/save  (multipart) */
    @PostMapping(value = "/banner/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> saveBanner(
            @RequestParam("title")    String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("user_id")  Integer userId,
            @RequestParam(value = "image_url", required = false) MultipartFile image) {
        int bannerId = websiteService.saveBanner(title, subtitle, userId, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Banner saved successfully.", "banner_id", bannerId));
    }

    /** PUT /api/website/banner/{id}  (multipart) */
    @PutMapping(value = "/banner/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateBanner(
            @PathVariable("id") Integer id,
            @RequestParam("title")    String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("user_id")  Integer userId,
            @RequestParam(value = "image_url", required = false) String existingImageUrl,
            @RequestParam(value = "image_file", required = false) MultipartFile image) {
        websiteService.updateBanner(id, title, subtitle, userId, existingImageUrl, image);
        return ResponseEntity.ok(Map.of("message", "Banner updated successfully."));
    }

    /** DELETE /api/website/banner/{id} */
    @DeleteMapping("/banner/{id}")
    public ResponseEntity<Map<String, String>> deleteBanner(@PathVariable("id") Integer id) {
        websiteService.deleteBanner(id);
        return ResponseEntity.ok(Map.of("message", "Banner deleted successfully."));
    }

    /** GET /api/website/banner/all */
    @GetMapping("/banner/all")
    public ResponseEntity<Map<String, Object>> getAllBanners() {
        List<WebsiteBannerDTO> banners = websiteService.getAllBanners();
        return ResponseEntity.ok(Map.of("banners", banners));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  COMMENTS
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/comment/add  (multipart) */
    @PostMapping(value = "/comment/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> addComment(
            @RequestParam("content")    String content,
            @RequestParam("clientName") String clientName,
            @RequestParam("user_id")    Integer userId,
            @RequestParam(value = "clientImg", required = false) MultipartFile clientImg) {
        int commentId = websiteService.addComment(content, clientName, userId, clientImg);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Comment added successfully.", "comment_id", commentId));
    }

    /** PUT /api/website/comment/{id}  (multipart) */
    @PutMapping(value = "/comment/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateComment(
            @PathVariable("id") Integer id,
            @RequestParam("content")    String content,
            @RequestParam("clientName") String clientName,
            @RequestParam("user_id")    Integer userId,
            @RequestParam(value = "clientImg", required = false) MultipartFile clientImg) {
        websiteService.updateComment(id, content, clientName, userId, clientImg);
        return ResponseEntity.ok(Map.of("message", "Comment updated successfully."));
    }

    /** DELETE /api/website/comment/{id} */
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable("id") Integer id) {
        websiteService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message", "Comment deleted successfully."));
    }

    /** GET /api/website/comment/all */
    @GetMapping("/comment/all")
    public ResponseEntity<Map<String, Object>> getAllComments() {
        List<WebsiteCommentDTO> comments = websiteService.getAllComments();
        return ResponseEntity.ok(Map.of("comments", comments));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CUSTOMERS
    // ══════════════════════════════════════════════════════════════════════════

    /** GET /api/website/customer/all */
    @GetMapping("/customer/all")
    public ResponseEntity<List<WebsiteCustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(websiteService.getAllCustomers());
    }

    /** GET /api/website/customer/count */
    @GetMapping("/customer/count")
    public ResponseEntity<Map<String, Object>> getCustomerCount() {
        long count = websiteService.getCustomerCount();
        return ResponseEntity.ok(Map.of("totalCustomers", count));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ORDERS
    // ══════════════════════════════════════════════════════════════════════════

    /** GET /api/website/order/all */
    @GetMapping("/order/all")
    public ResponseEntity<List<WebsiteOrderDTO>> getAllOrders() {
        return ResponseEntity.ok(websiteService.getAllOrders());
    }

    /** GET /api/website/order/details/{orderId} */
    @GetMapping("/order/details/{orderId}")
    public ResponseEntity<WebsiteOrderDetailsResponseDTO> getOrderDetails(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(websiteService.getOrderDetails(orderId));
    }

    /** GET /api/website/order/details-by-id/{orderId} */
    @GetMapping("/order/details-by-id/{orderId}")
    public ResponseEntity<List<WebsiteOrderItemDTO>> getDetailsByOrderId(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(websiteService.getDetailsByOrderId(orderId));
    }

    /** GET /api/website/order/new-orders */
    @GetMapping("/order/new-orders")
    public ResponseEntity<List<WebsiteNewOrderDTO>> getNewOrderDetails() {
        return ResponseEntity.ok(websiteService.getNewOrderDetails());
    }

    /** PUT /api/website/order/mark-received */
    @PutMapping("/order/mark-received")
    public ResponseEntity<Map<String, String>> markOrdersReceived() {
        websiteService.markOrdersAsReceived();
        return ResponseEntity.ok(Map.of("message", "Order data updated successfully."));
    }

    /** PUT /api/website/order/status/{orderId} */
    @PutMapping("/order/status/{orderId}")
    public ResponseEntity<Map<String, String>> updateOrderStatus(
            @PathVariable("orderId") String orderId,
            @RequestBody Map<String, String> body) {
        String status = body.get("order_status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Order status is required."));
        }
        websiteService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(Map.of("message", "Order status updated successfully."));
    }

    /** PUT /api/website/order/tracking/{orderId} */
    @PutMapping("/order/tracking/{orderId}")
    public ResponseEntity<Map<String, String>> updateTrackingNumber(
            @PathVariable("orderId") String orderId,
            @RequestBody Map<String, String> body) {
        websiteService.updateTrackingNumber(body.get("orderId"), body.get("tracking_number"));
        return ResponseEntity.ok(Map.of("message", "Update Successful!"));
    }

    /** PUT /api/website/order/save */
    @PutMapping("/order/save")
    public ResponseEntity<Map<String, Object>> saveOrder(@RequestBody WebsiteSaveOrderRequestDTO dto) {
        String newOrderId = websiteService.saveOrder(dto);
        return ResponseEntity.ok(Map.of("message", "Order saved successfully!", "orderId", newOrderId));
    }

    /** GET /api/website/order/today-sales */
    @GetMapping("/order/today-sales")
    public ResponseEntity<Map<String, Object>> getTodaySales() {
        double sales = websiteService.getTodaySales();
        return ResponseEntity.ok(Map.of("todaySales", sales));
    }

    /** GET /api/website/order/year-sales */
    @GetMapping("/order/year-sales")
    public ResponseEntity<Map<String, Object>> getYearSales() {
        List<Double> monthlySales = websiteService.getYearSales();
        return ResponseEntity.ok(Map.of("monthlySales", monthlySales));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PRODUCTS (website-facing)
    // ══════════════════════════════════════════════════════════════════════════

    /** GET /api/website/product/all */
    @GetMapping("/product/all")
    public ResponseEntity<List<WebsiteProductDTO>> getAllProducts() {
        return ResponseEntity.ok(websiteService.getAllProducts());
    }

    /** GET /api/website/product/{productId} */
    @GetMapping("/product/{productId}")
    public ResponseEntity<WebsiteProductDTO> getProductById(@PathVariable("productId") Integer productId) {
        return ResponseEntity.ok(websiteService.getProductById(productId));
    }

    /** GET /api/website/product/by-name/{productName} */
    @GetMapping("/product/by-name/{productName}")
    public ResponseEntity<WebsiteProductDTO> getProductByName(@PathVariable("productName") String productName) {
        return ResponseEntity.ok(websiteService.getProductByName(productName));
    }

    /** POST /api/website/product/save  (multipart) */
    @PostMapping(value = "/product/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> saveProduct(
            @RequestParam("product_name")  String productName,
            @RequestParam("unit_type")     String unitType,
            @RequestParam("product_price") Double productPrice,
            @RequestParam(value = "quantity",    defaultValue = "0") Integer quantity,
            @RequestParam(value = "discount",    defaultValue = "0") Double discount,
            @RequestParam(value = "weight",      required = false)   Double weight,
            @RequestParam(value = "amount",      defaultValue = "0") Double amount,
            @RequestParam(value = "description", defaultValue = "")  String description,
            @RequestParam(value = "keyPoints",   defaultValue = "")  String keyPoints,
            @RequestParam(value = "faq",         defaultValue = "")  String faq,
            @RequestParam(value = "howToUse",    defaultValue = "")  String howToUse,
            @RequestParam(value = "user_id",     defaultValue = "U001") String userId,
            @RequestParam(value = "business_name", required = false) String businessName,
            @RequestParam(value = "image_url",   required = false)   MultipartFile imageUrl,
            @RequestParam(value = "image_url_2", required = false)   MultipartFile imageUrl2,
            @RequestParam(value = "image_url_3", required = false)   MultipartFile imageUrl3) {

        WebsiteProductSaveRequestDTO req = new WebsiteProductSaveRequestDTO(
                productName, unitType, productPrice, quantity, discount, weight, amount,
                description, keyPoints, faq, howToUse, userId, businessName,
                imageUrl, imageUrl2, imageUrl3);
        websiteService.saveProduct(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product saved successfully."));
    }

    /** PUT /api/website/product/update  (multipart) */
    @PutMapping(value = "/product/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateProduct(
            @RequestParam("product_id")    Integer productId,
            @RequestParam("product_name")  String productName,
            @RequestParam("unit_type")     String unitType,
            @RequestParam("product_price") Double productPrice,
            @RequestParam(value = "quantity",    defaultValue = "0") Integer quantity,
            @RequestParam(value = "discount",    defaultValue = "0") Double discount,
            @RequestParam(value = "weight",      required = false)   Double weight,
            @RequestParam(value = "amount",      defaultValue = "0") Double amount,
            @RequestParam(value = "description", defaultValue = "")  String description,
            @RequestParam(value = "keyPoints",   defaultValue = "")  String keyPoints,
            @RequestParam(value = "faq",         defaultValue = "")  String faq,
            @RequestParam(value = "howToUse",    defaultValue = "")  String howToUse,
            @RequestParam(value = "image_url",   required = false)   MultipartFile imageUrl,
            @RequestParam(value = "image_url_2", required = false)   MultipartFile imageUrl2,
            @RequestParam(value = "image_url_3", required = false)   MultipartFile imageUrl3) {

        WebsiteProductUpdateRequestDTO req = new WebsiteProductUpdateRequestDTO(
                productId, productName, unitType, productPrice, quantity, discount, weight, amount,
                description, keyPoints, faq, howToUse, imageUrl, imageUrl2, imageUrl3);
        websiteService.updateProduct(req);
        return ResponseEntity.ok(Map.of("message", "Product updated successfully."));
    }

    /** DELETE /api/website/product/{productId} */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("productId") Integer productId) {
        websiteService.deleteProduct(productId);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully."));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  WEBSITE USERS
    // ══════════════════════════════════════════════════════════════════════════

    /** POST /api/website/user/save  (multipart) */
    @PostMapping(value = "/user/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> saveUser(
            @RequestParam("employeeId") String employeeId,
            @RequestParam("email")      String email,
            @RequestParam("password")   String password,
            @RequestParam("name")       String name,
            @RequestParam("nic")        String nic,
            @RequestParam("role")       String role,
            @RequestParam(value = "status",  defaultValue = "1") Integer status,
            @RequestParam(value = "visible", defaultValue = "1") Integer visible,
            @RequestParam(value = "image",   required = false)   MultipartFile image) {
        websiteService.saveUser(employeeId, email, password, name, nic, role, status, visible, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User saved successfully."));
    }

    /** GET /api/website/user/all */
    @GetMapping("/user/all")
    public ResponseEntity<List<WebsiteUserDTO>> getAllUsers() {
        return ResponseEntity.ok(websiteService.getAllWebsiteUsers());
    }

    /** GET /api/website/user/{email} */
    @GetMapping("/user/{email}")
    public ResponseEntity<WebsiteUserDTO> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(websiteService.getUserByEmail(email));
    }

    /** PUT /api/website/user/{userId} */
    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody WebsiteUserUpdateDTO dto) {
        websiteService.updateUser(userId, dto);
        return ResponseEntity.ok(Map.of("message", "User updated successfully."));
    }

    /** DELETE /api/website/user/{userId} */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("userId") String userId) {
        websiteService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

    /** POST /api/website/user/forgot-password */
    @PostMapping("/user/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        websiteService.forgotPassword(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Reset code sent successfully."));
    }

    /** POST /api/website/user/verify-reset-code */
    @PostMapping("/user/verify-reset-code")
    public ResponseEntity<Map<String, String>> verifyResetCode(@RequestBody Map<String, String> body) {
        websiteService.verifyResetCode(body.get("email"), body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Code verified successfully. Proceed to reset password."));
    }

    /** PUT /api/website/user/reset-password */
    @PutMapping("/user/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        websiteService.resetPassword(body.get("email"), body.get("password"), body.get("code"));
        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }

    /** PUT /api/website/user/update-password/{email} */
    @PutMapping("/user/update-password/{email}")
    public ResponseEntity<Map<String, String>> updatePassword(
            @PathVariable("email") String email,
            @RequestBody Map<String, String> body) {
        websiteService.updatePassword(email, body.get("password"));
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }

    /** PUT /api/website/user/profile-picture/{email}  (multipart) */
    @PutMapping(value = "/user/profile-picture/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateProfilePicture(
            @PathVariable("email") String email,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = websiteService.updateProfilePicture(email, image);
        return ResponseEntity.ok(Map.of("message", "Profile picture updated successfully.", "imageUrl", imageUrl));
    }
}
