package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.PaymentReportDTO;
import lk.petalpink.petalpink.repository.PaymentReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentReportService {

    @Autowired
    private PaymentReportRepository repo;

    public List<PaymentReportDTO> getPaymentReport(LocalDate from, LocalDate to) {
        return repo.findByDateRange(from, to);
    }

    public boolean updatePaymentStatusByOrderId(Integer orderId, Integer statusId) {
        return repo.updatePaymentStatusByOrderId(orderId, statusId) > 0;
    }
}