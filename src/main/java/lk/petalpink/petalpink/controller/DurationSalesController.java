package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.DurationSalesDTO;
import lk.petalpink.petalpink.service.DurationSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class DurationSalesController {

    @Autowired
    private DurationSalesService service;

    // GET /api/reports/duration-sales?dateFrom=2024-01-01&dateTo=2024-01-31
    @GetMapping("/duration-sales")
    public DurationSalesDTO getDurationSales(
            @RequestParam String dateFrom,
            @RequestParam String dateTo) {
        return service.getDurationSales(dateFrom, dateTo);
    }
}