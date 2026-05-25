package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.IngredientDTO;
import lk.petalpink.petalpink.dto.ItemTemplateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemTemplateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ─── SAVE ────────────────────────────────────────────────────────────────

    public int saveTemplate(ItemTemplateDTO dto) {
        String sql = "INSERT INTO pos_inv_item_store_tamplate_tb " +
                "(item_id, sub_item_id, tamplate_name, qty, unit, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int totalRows = 0;
        for (IngredientDTO ingredient : dto.getIngredients()) {
            totalRows += jdbcTemplate.update(sql,
                    dto.getItemId(),
                    ingredient.getSubItemId(),
                    dto.getTemplateName(),
                    ingredient.getQuantity(),
                    ingredient.getUnitType(),
                    1,
                    1
            );
        }
        return totalRows;
    }

    // ─── GET BY ITEM ID ──────────────────────────────────────────────────────

    public ItemTemplateDTO getTemplateByItemId(Integer itemId) {
        // 1. Fetch template rows joined with sub-item names
        String sql = """
                SELECT
                    t.item_id,
                    m.item_name,
                    t.tamplate_name,
                    t.sub_item_id,
                    s.item_name  AS sub_item_name,
                    t.qty,
                    t.unit
                FROM pos_inv_item_store_tamplate_tb t
                JOIN pos_main_item_tb m ON m.item_id = t.item_id
                JOIN pos_main_item_tb s ON s.item_id = t.sub_item_id
                WHERE t.item_id = ?
                  AND t.visible = 1
                ORDER BY t.tamplate_id
                """;

        List<IngredientDTO> ingredients = new java.util.ArrayList<>();
        final ItemTemplateDTO[] result = {null};

        jdbcTemplate.query(sql, rs -> {
            // Build header once from first row
            if (result[0] == null) {
                result[0] = new ItemTemplateDTO();
                result[0].setItemId(rs.getInt("item_id"));
                result[0].setItemName(rs.getString("item_name"));
                result[0].setTemplateName(rs.getString("tamplate_name"));
                result[0].setIngredients(ingredients);
            }
            // Build ingredient list
            IngredientDTO ing = new IngredientDTO();
            ing.setSubItemId(rs.getInt("sub_item_id"));
            ing.setSubItemName(rs.getString("sub_item_name"));
            ing.setQuantity(rs.getDouble("qty"));
            ing.setUnitType(rs.getString("unit"));
            ingredients.add(ing);
        }, itemId);

        return result[0]; // null if no template found
    }

    // ─── UPDATE BY ITEM ID ───────────────────────────────────────────────────

    /**
     * Strategy: soft-delete existing rows for this item_id, then insert fresh rows.
     * This avoids complex diff logic and keeps history implicit via visible flag.
     */
    public int updateTemplate(ItemTemplateDTO dto) {
        // 1. Soft-delete existing rows
        String deleteSql = "UPDATE pos_inv_item_store_tamplate_tb " +
                "SET visible = 0 " +
                "WHERE item_id = ? AND visible = 1";
        jdbcTemplate.update(deleteSql, dto.getItemId());

        // 2. Insert fresh ingredient rows
        String insertSql = "INSERT INTO pos_inv_item_store_tamplate_tb " +
                "(item_id, sub_item_id, tamplate_name, qty, unit, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int totalRows = 0;
        for (IngredientDTO ingredient : dto.getIngredients()) {
            totalRows += jdbcTemplate.update(insertSql,
                    dto.getItemId(),
                    ingredient.getSubItemId(),
                    dto.getTemplateName(),
                    ingredient.getQuantity(),
                    ingredient.getUnitType(),
                    1,
                    1
            );
        }
        return totalRows;
    }

    public int disableTemplate(Integer itemId) {
        String sql = "UPDATE pos_inv_item_store_tamplate_tb " +
                "SET visible = 0 " +
                "WHERE item_id = ? AND visible = 1";
        return jdbcTemplate.update(sql, itemId);
    }
}