package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.InquiryDTO;
import lk.petalpink.petalpink.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = "http://localhost:5173")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @GetMapping("/inquiries")
    public List<InquiryDTO> getAllInquiries() {
        return inquiryService.getAllInquiriesWithCustomer();
    }

    @GetMapping("/inquiries/by-date")
    public List<InquiryDTO> getInquiriesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return inquiryService.getInquiriesByDateRange(startDate, endDate);
    }

    @PutMapping("/inquiries/{inquiryId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable int inquiryId,
            @RequestParam int statusId) {
        boolean updated = inquiryService.updateInquiryStatus(inquiryId, statusId);
        if (updated) {
            return ResponseEntity.ok("Status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found");
        }
    }

    @PostMapping("/inquiries")
    public ResponseEntity<String> saveInquiry(@RequestBody InquiryDTO dto) {
        boolean saved = inquiryService.saveInquiry(dto);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Inquiry saved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save inquiry");
        }
    }
}