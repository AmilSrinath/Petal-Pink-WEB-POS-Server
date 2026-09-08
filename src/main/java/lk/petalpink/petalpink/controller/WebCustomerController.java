package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteCustomerDTO;
import lk.petalpink.petalpink.service.WebCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/customer")
@CrossOrigin(origins = "*")
public class WebCustomerController {

    @Autowired
    private WebCustomerService webCustomerService;

    @GetMapping("/all")
    public ResponseEntity<List<WebsiteCustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(webCustomerService.getAllCustomers());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCustomerCount() {
        long count = webCustomerService.getCustomerCount();
        return ResponseEntity.ok(Map.of("totalCustomers", count));
    }
}