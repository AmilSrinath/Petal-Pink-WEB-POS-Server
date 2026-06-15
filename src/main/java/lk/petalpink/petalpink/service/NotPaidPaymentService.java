package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.NotPaidPaymentDTO;
import lk.petalpink.petalpink.repository.NotPaidPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotPaidPaymentService {

    @Autowired
    private NotPaidPaymentRepository repository;

    public List<NotPaidPaymentDTO> getNotPaidPayments(LocalDate from, LocalDate to) {
        return repository.findNotPaidByDateRange(from, to);
    }
}