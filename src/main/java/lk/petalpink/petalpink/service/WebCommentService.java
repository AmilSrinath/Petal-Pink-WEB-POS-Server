package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteCommentDTO;
import lk.petalpink.petalpink.repository.WebCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class WebCommentService {

    @Autowired
    private WebCommentRepository webCommentRepository;

    @Autowired
    private WebsiteGcpStorageService gcpStorageService;

    public int addComment(String content, String clientName, Integer userId, MultipartFile clientImg) {
        if (content == null || clientName == null || userId == null) {
            throw new IllegalArgumentException("content, clientName, and user_id are required");
        }
        String imgUrl = null;
        if (clientImg != null && !clientImg.isEmpty()) {
            imgUrl = gcpStorageService.uploadFile(clientImg, "comments/" + System.currentTimeMillis() + "-clientImg.jpg");
        }
        return webCommentRepository.addComment(content, imgUrl, clientName, userId);
    }

    public void updateComment(Integer id, String content, String clientName, Integer userId, MultipartFile clientImg) {
        if (content == null || clientName == null || userId == null) {
            throw new IllegalArgumentException("content, clientName, and user_id are required");
        }
        String existingImg = webCommentRepository.getCommentImage(id);
        String imgUrl = existingImg;
        if (clientImg != null && !clientImg.isEmpty()) {
            imgUrl = gcpStorageService.uploadFile(clientImg, "comments/" + System.currentTimeMillis() + "-clientImg.jpg");
        }
        int rows = webCommentRepository.updateComment(id, content, imgUrl, clientName, userId);
        if (rows == 0) throw new RuntimeException("Comment not found.");
    }

    public void deleteComment(Integer id) {
        int rows = webCommentRepository.deleteComment(id);
        if (rows == 0) throw new RuntimeException("Comment not found.");
    }

    public List<WebsiteCommentDTO> getAllComments() {
        List<WebsiteCommentDTO> comments = webCommentRepository.getAllComments();
        if (comments.isEmpty()) throw new RuntimeException("No comments found.");
        return comments;
    }
}