package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.DeliveryOrderDTO;
import lk.petalpink.petalpink.dto.ItemDTO;
import lk.petalpink.petalpink.dto.OrderDetailItemDTO;
import lk.petalpink.petalpink.repository.DeliveryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import java.util.List;

@Service
public class DeliveryOrderService {

    // pos_status_types.status_id for "Despatch" (see reg_id = 1 status group)
    private static final int STATUS_ID_DESPATCH = 4;

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private OrderStockService orderStockService;

    public List<DeliveryOrderDTO> getByDateRange(String startDate, String endDate) {
        return deliveryOrderRepository.getByDateRange(startDate, endDate);
    }

    public List<ItemDTO> getOrderItemsByDeliveryId(Integer deliveryId) {
        return deliveryOrderRepository.getOrderItemsByDeliveryId(deliveryId);
    }

    /**
     * Updates the delivery status. If the new status is "Despatch", the order's
     * courier bag is deducted from stock at this point (once only — guarded by
     * courier_bag_deducted). It is intentionally never added back to stock when
     * an order later moves to Cancel/Return, per business rule.
     */
    @Transactional
    public void updateDeliveryStatus(Integer deliveryId, Integer statusId, Integer userId) {
        deliveryOrderRepository.updateDeliveryStatus(deliveryId, statusId);

        if (statusId != null && statusId == STATUS_ID_DESPATCH) {
            deductCourierBagIfNeeded(deliveryId, userId);
        }
    }

    private void deductCourierBagIfNeeded(Integer deliveryId, Integer userId) {
        DeliveryOrderDTO info = deliveryOrderRepository.getCourierBagInfo(deliveryId);

        if (info == null || info.getCourierBagId() == null) {
            return; // no bag selected for this order — nothing to deduct
        }
        if (info.getCourierBagDeducted() != null && info.getCourierBagDeducted() == 1) {
            return; // already deducted (e.g. order was moved to Despatch before) — never deduct twice
        }

        Integer effectiveUserId = (userId != null) ? userId : info.getUserId();

        orderStockService.deductCourierBag(info.getCourierBagId(), info.getCourierBagName(), effectiveUserId);
        deliveryOrderRepository.markCourierBagDeducted(deliveryId);
    }

    public String getRemarkByDeliveryId(Integer deliveryId) {
        return deliveryOrderRepository.getRemarkByDeliveryId(deliveryId);
    }

    public void updateRemark(Integer deliveryId, String remark) {
        deliveryOrderRepository.updateRemark(deliveryId, remark);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String generateAndAssignTracking(Integer deliveryId, Integer courierBagId, String courierBagName) {
        String trackingCode = deliveryOrderRepository.getNextTrackingCodeFromSequence();
        deliveryOrderRepository.assignTrackingCode(deliveryId, trackingCode, courierBagId, courierBagName);
//        printLabel(trackingCode); // ← print immediately after tracking is saved
        return trackingCode;
    }

    /**
     * Moves a pending order to Wrapping and records the courier bag selected
     * for it. This is the point at which a courier bag should be chosen —
     * not at order creation — since only now is the order actually being
     * packed for a specific courier.
     */
    @Transactional
    public void moveToWrapping(Integer deliveryId, Integer courierBagId, String courierBagName) {
        deliveryOrderRepository.setWrappingCourierBag(deliveryId, courierBagId, courierBagName);
    }

    private void printLabel(String trackingId) {
        try {
            String printerName = "Xprinter XP-365B";
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService printer = null;
            for (PrintService service : services) {
                if (service.getName().equalsIgnoreCase(printerName)) {
                    printer = service;
                    break;
                }
            }
            if (printer == null) {
                System.out.println("Printer not found: " + printerName);
                return;
            }

            String tspl =
                    "SIZE 50 mm,25 mm\n" +
                            "GAP 2 mm,0\n" +
                            "CLS\n" +
                            "TEXT 93,10,\"TSS24.BF2\",0,2,2,\"Petal Pink\"\n" +
                            "BARCODE 85,70,\"128\",80,2,0,2,3,\"" + trackingId + "\"\n" +
                            "PRINT 1\n";

            // Use PrintRequestAttributeSet to suppress dialog
            PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
            attrs.add(new JobName("LabelPrint", null));

            DocPrintJob job = printer.createPrintJob();
            DocAttributeSet docAttrs = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(
                    tspl.getBytes(),
                    DocFlavor.BYTE_ARRAY.AUTOSENSE,
                    docAttrs
            );
            job.print(doc, attrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DeliveryOrderDTO getByDeliveryId(Integer deliveryId) {
        return deliveryOrderRepository.getByDeliveryId(deliveryId);
    }

    public List<OrderDetailItemDTO> getOrderDetailsByOrderId(Integer orderId) {
        return deliveryOrderRepository.getOrderDetailsByOrderId(orderId);
    }

    public DeliveryOrderDTO getByOrderCode(String orderCode) {
        return deliveryOrderRepository.getByOrderCode(orderCode);
    }

    public void updateOrderItemRemark(Integer orderDetailId, String remark) {
        deliveryOrderRepository.updateOrderItemRemark(orderDetailId, remark);
    }
}