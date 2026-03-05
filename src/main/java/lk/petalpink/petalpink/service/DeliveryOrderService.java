package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.repository.DeliveryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    public List<DeliveryOrderDTO> getByDateRange(String startDate, String endDate) {
        return deliveryOrderRepository.getByDateRange(startDate, endDate);
    }
}