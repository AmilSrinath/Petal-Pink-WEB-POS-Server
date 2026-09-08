package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.ColorDTO;
import lk.petalpink.petalpink.dto.website.SizeDTO;
import lk.petalpink.petalpink.repository.WebProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/website/variant")
@CrossOrigin(origins = "*")
public class WebProductVariantController {

    @Autowired
    private WebProductVariantRepository webProductVariantRepository;

    // Master sizes already saved — the product form lets the admin pick from these
    // (or type a new one, which gets added to the catalog automatically on save).
    @GetMapping("/sizes")
    public ResponseEntity<List<SizeDTO>> getAllSizes() {
        return ResponseEntity.ok(webProductVariantRepository.getAllSizes());
    }

    // Master colors already saved — the product form lets the admin pick from these
    // for any size (or type a new one, which gets added to the catalog automatically on save).
    @GetMapping("/colors")
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        return ResponseEntity.ok(webProductVariantRepository.getAllColors());
    }
}
