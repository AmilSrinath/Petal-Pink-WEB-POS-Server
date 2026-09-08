package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteCommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class WebCommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addComment(String content, String clientImg, String clientName, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO petal_pink_comment_tb (content, clientImg, clientName, user_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, content);
            ps.setString(2, clientImg);
            ps.setString(3, clientName);
            ps.setInt(4, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public String getCommentImage(Integer id) {
        List<String> result = jdbcTemplate.queryForList(
                "SELECT clientImg FROM petal_pink_comment_tb WHERE comment_id = ?", String.class, id);
        if (result.isEmpty()) throw new RuntimeException("Comment not found.");
        return result.get(0);
    }

    public int updateComment(Integer id, String content, String clientImg, String clientName, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_comment_tb SET content = ?, clientImg = ?, clientName = ?, user_id = ? WHERE comment_id = ?",
                content, clientImg, clientName, userId, id);
    }

    public int deleteComment(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM petal_pink_comment_tb WHERE comment_id = ?", id);
    }

    public List<WebsiteCommentDTO> getAllComments() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_comment_tb",
                new BeanPropertyRowMapper<>(WebsiteCommentDTO.class));
    }
}