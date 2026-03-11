package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.dto.ItemDTO;
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

    public List<ItemDTO> getOrderItemsByDeliveryId(Integer deliveryId) {
        return deliveryOrderRepository.getOrderItemsByDeliveryId(deliveryId);
    }

    public void updateDeliveryStatus(Integer deliveryId, Integer statusId) {
        deliveryOrderRepository.updateDeliveryStatus(deliveryId, statusId);
    }

    public String getRemarkByDeliveryId(Integer deliveryId) {
        return deliveryOrderRepository.getRemarkByDeliveryId(deliveryId);
    }

    public void updateRemark(Integer deliveryId, String remark) {
        deliveryOrderRepository.updateRemark(deliveryId, remark);
    }
}