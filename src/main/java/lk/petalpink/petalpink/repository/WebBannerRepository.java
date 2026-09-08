package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteBannerDTO;
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
public class WebBannerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveBanner(String title, String subtitle, String imageUrl, Integer userId) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO web_petal_pink_banners_tb (title, subtitle, image_url, created_date, status, user_id) VALUES (?, ?, ?, NOW(), 1, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, subtitle);
            ps.setString(3, imageUrl);
            ps.setInt(4, userId);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    public int updateBanner(Integer id, String title, String subtitle, String imageUrl, Integer userId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_banners_tb SET title = ?, subtitle = ?, image_url = ?, user_id = ? WHERE banner_id = ? AND status = 1",
                title, subtitle, imageUrl, userId, id);
    }

    public int deleteBanner(Integer id) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_banners_tb SET status = 0 WHERE banner_id = ? AND status = 1", id);
    }

    public List<WebsiteBannerDTO> getAllBanners() {
        return jdbcTemplate.query(
                "SELECT banner_id, title, subtitle, image_url, created_date, user_id FROM web_petal_pink_banners_tb WHERE status = 1 ORDER BY created_date DESC",
                new BeanPropertyRowMapper<>(WebsiteBannerDTO.class));
    }
}