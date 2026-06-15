package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.DashboardDTO;
import lk.petalpink.petalpink.dto.ItemSaleCountDTO;
import lk.petalpink.petalpink.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    /**
     * Returns all dashboard KPIs for today, including item-wise sale counts.
     */
    public DashboardDTO getSummary() {
        return dashboardRepository.getSummary();
    }

    /**
     * Standalone method — returns only the item-wise sale counts for today.
     * Useful if you want a dedicated lightweight endpoint in the future.
     */
    public List<ItemSaleCountDTO> getTodayItemSaleCounts() {
        return dashboardRepository.getTodayItemSaleCounts();
    }
}