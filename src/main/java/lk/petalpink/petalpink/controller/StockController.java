package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.StockDTO;
import lk.petalpink.petalpink.dto.StockDetailsDTO;
import lk.petalpink.petalpink.dto.StockInitDTO;
import lk.petalpink.petalpink.dto.StockTransactionDTO;
import lk.petalpink.petalpink.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:5173")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * POST /api/stocks/add
     * Add stock — GRN receive, return, adjustment in
     */
//    @PostMapping("/add")
//    public String addStock(@RequestBody StockTransactionDTO dto) {
//        return stockService.addStock(dto);
//    }

    /**
     * POST /api/stocks/reduce
     * Reduce stock — sale, damage write-off, adjustment out
     */
//    @PostMapping("/reduce")
//    public String reduceStock(@RequestBody StockTransactionDTO dto) {
//        return stockService.reduceStock(dto);
//    }

    /**
     * GET /api/stocks
     * Current stock levels (master table)
     */
    @GetMapping
    public List<StockDTO> getAllMasterStocks() {
        return stockService.getAllMasterStocks();
    }

    /**
     * GET /api/stocks/{stockId}/details
     * Full transaction history for one stock item
     */
    @GetMapping("/{stockId}/details")
    public List<StockDetailsDTO> getDetailsByStockId(@PathVariable int stockId) {
        return stockService.getDetailsByStockId(stockId);
    }

    /**
     * GET /api/stocks/details
     * All transaction history
     */
    @GetMapping("/details")
    public List<StockDetailsDTO> getAllDetails() {
        return stockService.getAllDetails();
    }

    @PostMapping("/init")
    public void initStock(@RequestBody StockInitDTO dto) {
        stockService.initializeStock(dto);
    }
}