package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.ColorDTO;
import lk.petalpink.petalpink.dto.website.SizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

/**
 * Master (global, reusable) clothing size & color catalog.
 *
 * Sizes and colors are each saved once here and can then be picked from again
 * on any future product — the admin form fetches these via
 * WebProductVariantController and lets the user pick from what's already
 * saved (or type a new one, which is added to the catalog automatically).
 */
@Repository
public class WebProductVariantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ─── Master sizes ──────────────────────────────────────────────────────

    public List<SizeDTO> getAllSizes() {
        return jdbcTemplate.query(
                """
                SELECT size_id, size_name FROM web_petal_pink_size_tb
                WHERE status = 1 ORDER BY size_name
                """,
                (rs, rowNum) -> new SizeDTO(rs.getInt("size_id"), rs.getString("size_name")));
    }

    /** Finds an existing size by name (case-insensitive) or creates it, returning its id. */
    public Integer getOrCreateSizeId(String sizeName) {
        if (sizeName == null || sizeName.isBlank()) return null;
        String trimmed = sizeName.trim();

        List<Integer> existing = jdbcTemplate.queryForList(
                "SELECT size_id FROM web_petal_pink_size_tb WHERE LOWER(size_name) = LOWER(?)",
                Integer.class, trimmed);
        if (!existing.isEmpty()) return existing.get(0);

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO web_petal_pink_size_tb (size_name, status, create_date) VALUES (?, 1, NOW())",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, trimmed);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }

    // ─── Master colors ─────────────────────────────────────────────────────

    public List<ColorDTO> getAllColors() {
        return jdbcTemplate.query(
                """
                SELECT color_id, color_name, color_code FROM web_petal_pink_color_tb
                WHERE status = 1 ORDER BY color_name
                """,
                (rs, rowNum) -> new ColorDTO(rs.getInt("color_id"), rs.getString("color_name"), rs.getString("color_code")));
    }

    /**
     * Finds an existing color by name (case-insensitive) or creates it, returning its id.
     * If the color already exists and a new colorCode is supplied, the stored code is refreshed.
     */
    public Integer getOrCreateColorId(String colorName, String colorCode) {
        if (colorName == null || colorName.isBlank()) return null;
        String trimmed = colorName.trim();

        List<Integer> existing = jdbcTemplate.queryForList(
                "SELECT color_id FROM web_petal_pink_color_tb WHERE LOWER(color_name) = LOWER(?)",
                Integer.class, trimmed);
        if (!existing.isEmpty()) {
            Integer colorId = existing.get(0);
            if (colorCode != null && !colorCode.isBlank()) {
                jdbcTemplate.update(
                        "UPDATE web_petal_pink_color_tb SET color_code = ? WHERE color_id = ?",
                        colorCode, colorId);
            }
            return colorId;
        }

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO web_petal_pink_color_tb (color_name, color_code, status, create_date) VALUES (?, ?, 1, NOW())",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, trimmed);
            ps.setString(2, colorCode);
            return ps;
        }, kh);
        return Objects.requireNonNull(kh.getKey()).intValue();
    }
}
