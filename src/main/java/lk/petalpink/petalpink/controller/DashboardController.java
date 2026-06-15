package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.DashboardDTO;
import lk.petalpink.petalpink.dto.ItemSaleCountDTO;
import lk.petalpink.petalpink.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * GET /api/dashboard/summary
     *
     * Returns today's KPIs + item-wise sale counts in one call:
     *
     *  {
     *    "todayOrdersCount":       42,
     *    "todayRevenue":           125300.00,
     *    "pendingDeliveriesCount": 18,
     *    "activeInquiriesCount":   7,
     *    "todayItemSaleCounts": [
     *      { "itemId": 3, "itemName": "Rose Bouquet", "totalQuantitySold": 15, "totalItemRevenue": 22500.00 },
     *      ...
     *    ]
     *  }
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardDTO> getSummary() {
        DashboardDTO summary = dashboardService.getSummary();
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/today-item-sales
     *
     * Lightweight standalone endpoint — returns only the item-wise sale counts
     * for today without fetching the other KPIs. Useful for independent refresh.
     *
     * Response example:
     *  [
     *    { "itemId": 3, "itemName": "Rose Bouquet", "totalQuantitySold": 15, "totalItemRevenue": 22500.00 },
     *    { "itemId": 7, "itemName": "Lily Bunch",   "totalQuantitySold": 9,  "totalItemRevenue": 10800.00 }
     *  ]
     */
    @GetMapping("/today-item-sales")
    public ResponseEntity<List<ItemSaleCountDTO>> getTodayItemSales() {
        List<ItemSaleCountDTO> items = dashboardService.getTodayItemSaleCounts();
        return ResponseEntity.ok(items);
    }
}