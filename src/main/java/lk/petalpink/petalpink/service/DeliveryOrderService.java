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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String generateAndAssignTracking(Integer deliveryId) {
        String trackingCode = deliveryOrderRepository.getNextTrackingCodeFromSequence();
        deliveryOrderRepository.assignTrackingCode(deliveryId, trackingCode);
        printLabel(trackingCode); // ← print immediately after tracking is saved
        return trackingCode;
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
                            "TEXT 93,10,\"TSS24.BF2\",0,2,2,\"" + "Petal Pink" + "\"\n" +
                            "BARCODE 85,70,\"128\",80,2,0,2,3,\"" + trackingId + "\"\n" +
                            "PRINT 1\n";
            DocPrintJob job = printer.createPrintJob();
            Doc doc = new SimpleDoc(tspl.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);
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
}