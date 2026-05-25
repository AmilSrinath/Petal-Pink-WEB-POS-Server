package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.BusinessProfileDTO;
import lk.petalpink.petalpink.service.BusinessProfileService;
import lk.petalpink.petalpink.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:5173")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private BusinessProfileService businessProfileService;

    @GetMapping("/delivery-fee")
    public double getDeliveryFee() {
        return configService.getDeliveryFee();
    }

    @GetMapping("/add-cost-per-kg")
    public double getAddCostPerKg() {
        return configService.getAddCostPer1Kg();
    }

    @GetMapping("/payment-tier")
    public int getPaymentTier(@RequestParam double orderAmount) {
        return configService.getPaymentTierRate(orderAmount);
    }

    @GetMapping("/all")
    public Map<String, Object> getAllConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("deliveryFee", configService.getDeliveryFee());
        config.put("addCostPer1Kg", configService.getAddCostPer1Kg());
        config.put("paymentTiers", Map.of(
                "0to5000", configService.getTier0To5000(),
                "5001to10000", configService.getTier5001To10000(),
                "10001to20000", configService.getTier10001To20000(),
                "20001to50000", configService.getTier20001To50000()
        ));
        return config;
    }

    @GetMapping("/business-profiles")
    public List<BusinessProfileDTO> getBusinessProfiles() {
        return businessProfileService.getAllBusinessProfiles();
    }

    @GetMapping("/is-print")
    public int getIsPrint() {
        return configService.getIsPrint();
    }

    @GetMapping("/auto-generate-id")
    public int getAutoGenerateId() {
        return configService.getAutoGenerateId();
    }

    @GetMapping("/courier-bags/config")
    public ResponseEntity<?> getCourierBagsConfig() {
        return ResponseEntity.ok(Map.of(
                "isShowCourierBags", configService.getIsShowCourierBags()
        ));
    }
}