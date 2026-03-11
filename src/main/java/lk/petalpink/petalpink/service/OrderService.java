package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.OrderDTO;
import lk.petalpink.petalpink.repository.ItemRepository;
import lk.petalpink.petalpink.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDTO> getOrdersWithCustomers() {
        return orderRepository.getOrdersWithCustomers();
    }
}
