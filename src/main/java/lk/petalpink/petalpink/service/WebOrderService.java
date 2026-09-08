package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CreateOrderRequestDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.dto.website.*;
import lk.petalpink.petalpink.repository.ItemRepository;
import lk.petalpink.petalpink.repository.WebOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WebOrderService {

    // System/website user, used to attribute the auto-created Sales order and
    // its stock movements — mirrors the hardcoded userId used by the Sales
    // page itself for its own order-create payload.
    private static final int WEBSITE_SYSTEM_USER_ID = 1;

    @Autowired
    private WebOrderRepository webOrderRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CreateOrderService createOrderService;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Save a new web order and its line items, then send a confirmation SMS.
     * Format: NPP-YYYYMMDD{seq}
     * Examples: NPP-202606271, NPP-202606272, NPP-20260627999
     *
     * Right after the order is saved into Website Orders, it is also pushed
     * into the Sales page as a normal order (same create-order process used
     * by the Sales page itself: customer upsert, delivery order, order,
     * order details, FIFO stock deduction, Not-Paid payment record). This
     * runs in its own transaction (see CreateOrderService#createOrder) so
     * that if it fails for any reason, the customer's Website Order is still
     * saved successfully — the failure is only logged for follow-up.
     */
    @Transactional
    public String placeOrder(WebsiteSaveOrderRequestDTO req) {

        // Date prefix: NPP-YYYYMMDD
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix   = "NPP-" + datePart;

        // Get next sequence number for today from DB (1, 2, 3 ...)
        int seq = webOrderRepository.getNextDailySequence(prefix);

        // Final ref: NPP-202606271 / NPP-202606272 / NPP-20260627999
        String orderRef = prefix + seq;

        // 1. Insert order header
        Integer webOrderId = webOrderRepository.saveOrder(req, orderRef);

        // 2. Insert detail rows (product snapshot)
        if (req.getCartItems() != null && !req.getCartItems().isEmpty()) {
            webOrderRepository.saveOrderDetails(webOrderId, req.getCartItems());
        }

        // 2b. Push the same order into the Sales page pipeline
        try {
            Integer deliveryOrderId = pushToSales(req, orderRef);
            webOrderRepository.linkDeliveryOrder(webOrderId, deliveryOrderId);
        } catch (Exception e) {
            // Never let a Sales-side issue (e.g. insufficient stock) block the
            // customer's checkout — the Website Order above is already saved.
            System.err.println("Failed to push website order " + orderRef + " to Sales page: " + e.getMessage());
        }

        // 3. Send confirmation SMS — order already committed, so failure is non-fatal
        System.out.println("Near SMS");
        smsService.sendOrderConfirmation(req.getPhone1(), orderRef);

        return orderRef;
    }

    /**
     * Builds the same request shape the Sales page itself sends to
     * POST /api/orders/create, and runs it through the normal order-creation
     * process (CreateOrderService#createOrder).
     */
    private Integer pushToSales(WebsiteSaveOrderRequestDTO req, String orderRef) {
        CreateOrderRequestDTO salesReq = new CreateOrderRequestDTO();

        // customer
        salesReq.setCustomerName(buildFullName(req.getFirstName(), req.getLastName()));
        salesReq.setPhoneOne(req.getPhone1());
        salesReq.setPhoneTwo(req.getPhone2());
        salesReq.setAddress(buildAddress(req));
        salesReq.setUserId(WEBSITE_SYSTEM_USER_ID);
        salesReq.setCustomerNumber(null);

        // delivery order
        salesReq.setWebsiteOrderId(orderRef);
        salesReq.setCodAmount(req.getTotal() != null ? BigDecimal.valueOf(req.getTotal()) : BigDecimal.ZERO);
        salesReq.setWeight(req.getTotalWeight() != null ? String.valueOf(req.getTotalWeight()) : null);
        salesReq.setRemark("Website Order " + orderRef);
        salesReq.setOrderType("Website");
        salesReq.setIsFreeDelivery(0);
        salesReq.setIsExchange(0);

        // order
        salesReq.setBillNo("");
        salesReq.setSubTotalPrice(req.getSubTotal());
        salesReq.setTotalDiscountPrice(0.0);
        salesReq.setDeliveryFee(req.getDelivery());
        salesReq.setTotalOrderPrice(req.getTotal());
        salesReq.setPaidAmount(0.0); // matches normal flow: payment record is created as Not Paid
        salesReq.setPaymentTypeId(resolvePaymentTypeId(req.getPaymentMethod()));

        // courier bag is intentionally left unset here — it's chosen and
        // deducted later, when the order is prepared for Despatch (see
        // DeliveryOrderService), same as every other order.
        salesReq.setCourierBagId(null);
        salesReq.setCourierBagName(null);

        // order details
        salesReq.setItems(buildOrderDetailItems(req.getCartItems()));

        return createOrderService.createOrder(salesReq);
    }

    private List<OrderDetailItemDTO> buildOrderDetailItems(List<WebsiteCartItemDTO> cartItems) {
        if (cartItems == null) return new ArrayList<>();

        return cartItems.stream().map(c -> {
            OrderDetailItemDTO item = new OrderDetailItemDTO();

            // Match the website product to a POS item by name so stock can be
            // deducted correctly. If no match is found, the line is still
            // added to the order/bill (itemId stays null) but is skipped for
            // stock deduction — see CreateOrderService.
            item.setItemId(itemRepository.findItemIdByName(c.getProductName()));
            item.setItemName(c.getProductName());
            item.setItemBarCode(null);
            item.setUnitTypeId(null);
            item.setPrinterTypeId(null);
            item.setQuantity(c.getQuantity());
            item.setPerItemPrice(c.getPrice());

            double qty = c.getQuantity() != null ? c.getQuantity() : 1;
            double discount = c.getDiscount() != null ? c.getDiscount() : 0.0;
            double price = c.getPrice() != null ? c.getPrice() : 0.0;

            item.setTotalDiscountPrice(discount);
            item.setTotalItemPrice(c.getSubTotal() != null ? c.getSubTotal() : (price * qty - discount));
            item.setRemark(null);

            return item;
        }).collect(Collectors.toList());
    }

    private String buildFullName(String firstName, String lastName) {
        String first = firstName != null ? firstName.trim() : "";
        String last  = lastName  != null ? lastName.trim()  : "";
        String full  = (first + " " + last).trim();
        return full.isEmpty() ? "Website Customer" : full;
    }

    private String buildAddress(WebsiteSaveOrderRequestDTO req) {
        return Stream.of(req.getAddress1(), req.getAddress2(), req.getCity(), req.getProvince(), req.getCountry())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(", "));
    }

    /** Website only supports Cash on Delivery / Card today; falls back to Cash. */
    private Integer resolvePaymentTypeId(String paymentMethod) {
        if (paymentMethod != null && paymentMethod.toLowerCase().contains("card")) {
            return 2; // Card
        }
        return 1; // Cash (incl. Cash on Delivery)
    }

    /** All orders - header only */
    public List<WebsiteOrderDTO> getAllOrders() {
        return webOrderRepository.getAllOrders();
    }

    /** Full detail for one order */
    public WebsiteOrderDetailsResponseDTO getOrderDetails(String orderRef) {
        WebsiteOrderDTO order = webOrderRepository.getOrderByRef(orderRef);
        if (order == null) return null;

        List<WebsiteOrderItemDTO> items = webOrderRepository.getOrderItems(orderRef);

        WebsiteOrderDetailsResponseDTO response = new WebsiteOrderDetailsResponseDTO();
        response.setOrder(order);
        response.setItems(items);
        return response;
    }

    // ─────────────────────────────────────────────────
    //  DASHBOARD — sales figures
    // ─────────────────────────────────────────────────

    /** Total value of orders placed today. */
    public double getTodaySales() {
        return webOrderRepository.getTodaySales();
    }

    /** Monthly totals (index 0 = Jan ... index 11 = Dec) for the current year. */
    public List<Double> getYearSales() {
        int currentYear = LocalDate.now().getYear();
        return webOrderRepository.getMonthlySales(currentYear);
    }

    // ─────────────────────────────────────────────────
    //  UPDATE — status / tracking number
    // ─────────────────────────────────────────────────

    public void updateOrderStatus(String orderRef, String orderStatus) {
        if (orderStatus == null || orderStatus.isBlank()) {
            throw new IllegalArgumentException("order_status is required.");
        }
        int rows = webOrderRepository.updateOrderStatus(orderRef, orderStatus);
        if (rows == 0) throw new RuntimeException("Order not found: " + orderRef);
    }

    public void updateTrackingNumber(String orderRef, String trackingNumber) {
        int rows = webOrderRepository.updateTrackingNumber(orderRef, trackingNumber);
        if (rows == 0) throw new RuntimeException("Order not found: " + orderRef);
    }
}
