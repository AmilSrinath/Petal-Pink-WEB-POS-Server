package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.InquiryDTO;
import lk.petalpink.petalpink.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    public List<InquiryDTO> getAllInquiriesWithCustomer() {
        return inquiryRepository.getAllInquiriesWithCustomer();
    }

    public List<InquiryDTO> getInquiriesByDateRange(String startDate, String endDate) {
        return inquiryRepository.getInquiriesByDateRange(startDate, endDate);
    }

    public boolean updateInquiryStatus(int inquiryId, int statusId) {
        return inquiryRepository.updateInquiryStatus(inquiryId, statusId) > 0;
    }

    public boolean saveInquiry(InquiryDTO dto) {
        return inquiryRepository.saveInquiry(dto) > 0;
    }
}