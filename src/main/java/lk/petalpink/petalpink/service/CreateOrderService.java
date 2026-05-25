package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.dto.UpdateOrderRequestDTO;
import lk.petalpink.petalpink.repository.CreateOrderRepository;
import lk.petalpink.petalpink.service.OrderStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService {

    @Autowired
    private CreateOrderRepository createOrderRepository;

    @Autowired
    private OrderStockService orderStockService;

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

        // step 5 - deduct stock for every ordered item via FIFO ingredient template
        if (req.getItems() != null) {
            for (OrderDetailItemDTO item : req.getItems()) {
                orderStockService.deductStockForOrder(
                        item.getItemId(),
                        item.getQuantity() != null ? item.getQuantity().doubleValue() : 1.0,
                        orderId,
                        req.getUserId()
                );
            }
        }

        // step 5b - deduct 1 courier bag per order  ← NEW
        orderStockService.deductCourierBag(
                req.getCourierBagId(),
                req.getCourierBagName(),
                req.getUserId()
        );

        // step 6 - create payment record (status_id = 9, Not Paid)
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