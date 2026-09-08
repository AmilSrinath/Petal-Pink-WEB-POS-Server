package lk.petalpink.petalpink.controller;

import lk.petalpink.petalpink.dto.website.WebsiteCommentDTO;
import lk.petalpink.petalpink.service.WebCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/website/comment")
@CrossOrigin(origins = "*")
public class WebCommentController {

    @Autowired
    private WebCommentService webCommentService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> addComment(
            @RequestParam("content")    String content,
            @RequestParam("clientName") String clientName,
            @RequestParam("user_id")    Integer userId,
            @RequestParam(value = "clientImg", required = false) MultipartFile clientImg) {
        int commentId = webCommentService.addComment(content, clientName, userId, clientImg);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Comment added successfully.", "comment_id", commentId));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateComment(
            @PathVariable("id") Integer id,
            @RequestParam("content")    String content,
            @RequestParam("clientName") String clientName,
            @RequestParam("user_id")    Integer userId,
            @RequestParam(value = "clientImg", required = false) MultipartFile clientImg) {
        webCommentService.updateComment(id, content, clientName, userId, clientImg);
        return ResponseEntity.ok(Map.of("message", "Comment updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable("id") Integer id) {
        webCommentService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message", "Comment deleted successfully."));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllComments() {
        List<WebsiteCommentDTO> comments = webCommentService.getAllComments();
        return ResponseEntity.ok(Map.of("comments", comments));
    }
}