package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.service.DeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class DeliveryOrderController {

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @GetMapping("/delivery-orders")
    public List<DeliveryOrderDTO> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return deliveryOrderService.getByDateRange(startDate, endDate);
    }
}