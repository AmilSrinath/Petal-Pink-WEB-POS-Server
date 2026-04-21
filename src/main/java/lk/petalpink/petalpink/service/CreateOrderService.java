package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.UpdateOrderRequestDTO;
import lk.petalpink.petalpink.repository.CreateOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService {

    @Autowired
    private CreateOrderRepository createOrderRepository;

    @Transactional
    public Integer createOrder(CreateOrderRequestDTO req) {
        // step 1 - upsert customer
        Integer customerId = createOrderRepository.upsertCustomer(req);

        // step 2 - create delivery order (order_code empty, status=1 pending)
        Integer deliveryOrderId = createOrderRepository.createDeliveryOrder(req, customerId);

        // step 3 - create order
        Integer orderId = createOrderRepository.createOrder(req, customerId, deliveryOrderId);

        // step 4 - create order details
        createOrderRepository.createOrderDetails(req.getItems(), orderId, req.getUserId());

        // step 5 - create payment record (status_id = 9, Not Paid)
        createOrderRepository.createPayment(orderId, customerId, req);

        return deliveryOrderId;
    }

    public void updateOrderCode(Integer deliveryId, String orderCode) {
        createOrderRepository.updateOrderCode(deliveryId, orderCode);
    }

    public void updateOrder(UpdateOrderRequestDTO req) {
        createOrderRepository.updateOrder(req);
    }
}