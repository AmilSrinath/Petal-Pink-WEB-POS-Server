package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.dto.UpdateOrderRequestDTO;
import lk.petalpink.petalpink.repository.CreateOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService {

    @Autowired
    private CreateOrderRepository createOrderRepository;

    @Autowired
    private OrderStockService orderStockService;

    // REQUIRES_NEW: this always runs as its own, independent transaction.
    // This matters because WebOrderService calls this method after already
    // saving a Website Order; if this order-creation step fails (e.g. stock
    // issue) it must roll back on its own without ever rolling back the
    // Website Order save that already happened.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer createOrder(CreateOrderRequestDTO req) {

        // step 1 - upsert customer
        Integer customerId = createOrderRepository.upsertCustomer(req);

        // step 2 - create delivery order (order_code empty, status=1 pending)
        Integer deliveryOrderId = createOrderRepository.createDeliveryOrder(req, customerId);

        // step 3 - create order
        Integer orderId = createOrderRepository.createOrder(req, customerId, deliveryOrderId);

        // step 4 - create order details
        createOrderRepository.createOrderDetails(req.getItems(), orderId, req.getUserId());

        // step 5 - deduct the ordered item itself from stock via FIFO
        //          (ingredient breakdown is handled separately in ProductionService)
        if (req.getItems() != null) {
            for (OrderDetailItemDTO item : req.getItems()) {
                if (item.getItemId() == null) {
                    // No matching POS item found for this line (e.g. a website
                    // product not yet linked to a POS item by name). The line
                    // is still recorded on the order/bill; it's just skipped
                    // for FIFO stock deduction.
                    continue;
                }

                double qty =
                        item.getQuantity() != null
                                ? item.getQuantity().doubleValue()
                                : 1.0;

                if (orderStockService.isBudgetPack(item.getItemId())) {
                    orderStockService.deductBudgetPackStock(
                            item.getItemId(),
                            qty,
                            req.getUserId()
                    );
                } else {
                    orderStockService.deductItemStockForOrder(
                            item.getItemId(),
                            item.getItemName(),
                            qty,
                            req.getUserId()
                    );
                }
            }
        }

        // NOTE: Courier bag stock is intentionally NOT deducted here anymore.
        // It is deducted later, once, when the order's delivery status is
        // changed to "Despatch" (status_id = 4) — see DeliveryOrderService.
        // This avoids charging a bag against stock for orders that never
        // actually ship (e.g. get Cancelled while still Pending/Wrapping).

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