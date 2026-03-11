package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.StatusTypeDTO;
import lk.petalpink.petalpink.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "http://localhost:5173")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/types/{regId}")
    public List<StatusTypeDTO> getStatusTypesByRegId(@PathVariable Integer regId) {
        return statusService.getStatusTypesByRegId(regId);
    }
}