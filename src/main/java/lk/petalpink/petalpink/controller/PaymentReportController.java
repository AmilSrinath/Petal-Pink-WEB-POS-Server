package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.PaymentReportDTO;
import lk.petalpink.petalpink.service.PaymentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payment-report")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentReportController {

    @Autowired
    private PaymentReportService service;

    @GetMapping
    public List<PaymentReportDTO> getPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getPaymentReport(from, to);
    }

    @PutMapping("/status-by-order")
    public String updateStatusByOrderId(
            @RequestParam Integer orderId,
            @RequestParam Integer statusId) {

        boolean updated = service.updatePaymentStatusByOrderId(orderId, statusId);

        if (updated) {
            return "Payment status updated successfully";
        } else {
            return "Order not found";
        }
    }
}