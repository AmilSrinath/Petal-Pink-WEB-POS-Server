package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.NotPaidPaymentDTO;
import lk.petalpink.petalpink.service.NotPaidPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class NotPaidPaymentController {

    @Autowired
    private NotPaidPaymentService service;

    /**
     * GET /api/payments/not-paid?startDate=2026-05-01&endDate=2026-05-31
     *
     * Returns all Not Paid payments (payment_status = 0)
     * within the given date range.
     */
    @GetMapping("/not-paid")
    public List<NotPaidPaymentDTO> getNotPaidPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return service.getNotPaidPayments(startDate, endDate);
    }
}