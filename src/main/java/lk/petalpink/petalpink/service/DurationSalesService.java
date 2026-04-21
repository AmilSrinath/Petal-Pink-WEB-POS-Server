package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.DurationSalesDTO;
import lk.petalpink.petalpink.repository.DurationSalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DurationSalesService {

    @Autowired
    private DurationSalesRepository repo;

    public DurationSalesDTO getDurationSales(String dateFrom, String dateTo) {
        return repo.getDurationSales(dateFrom, dateTo);
    }
}