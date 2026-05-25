package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.BatchMergeRequestDTO;
import lk.petalpink.petalpink.dto.BatchProfileDTO;
import lk.petalpink.petalpink.service.BatchMergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
@CrossOrigin(origins = "http://localhost:5173")
public class BatchMergeController {

    @Autowired
    private BatchMergeService batchMergeService;

    @PostMapping("/merge")
    public ResponseEntity<String> mergeBatches(@RequestBody BatchMergeRequestDTO request) {
        String result = batchMergeService.mergeBatches(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<BatchProfileDTO>> getBatchesByItem(@PathVariable int itemId) {
        return ResponseEntity.ok(batchMergeService.getActiveBatchesByItem(itemId));
    }
}