package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(ItemDTO dto) {
        String sql = "INSERT INTO pos_main_item_tb (item_bar_code, main_item_category_id, sub_item_category_id, " +
                "item_prefix, item_code_prefix, discount, item_name, unit_type, printer_type, cost_price, " +
                "unit_price, image_path, grn_status, selling_status, status, user_id, visible, weight) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getItemBarCode(), dto.getMainItemCategoryId(), dto.getSubItemCategoryId(),
                dto.getItemPrefix(), dto.getItemCodePrefix(), dto.getDiscount(), dto.getItemName(),
                dto.getUnitType(), dto.getPrinterType(), dto.getCostPrice(), dto.getUnitPrice(),
                dto.getImagePath(), dto.getGrnStatus(), dto.getSellingStatus(), dto.getStatus(),
                dto.getUserId(), dto.getVisible(), dto.getWeight());
    }

    public List<ItemDTO> findAll() {
        String sql = "SELECT * FROM pos_main_item_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ItemDTO.class));
    }

    public int update(ItemDTO dto) {
        String sql = "UPDATE pos_main_item_tb SET item_bar_code=?, main_item_category_id=?, sub_item_category_id=?, " +
                "item_prefix=?, item_code_prefix=?, discount=?, item_name=?, unit_type=?, printer_type=?, " +
                "cost_price=?, unit_price=?, image_path=?, grn_status=?, selling_status=?, status=?, " +
                "user_id=?, visible=?, weight=? WHERE item_id=?";
        return jdbcTemplate.update(sql,
                dto.getItemBarCode(), dto.getMainItemCategoryId(), dto.getSubItemCategoryId(),
                dto.getItemPrefix(), dto.getItemCodePrefix(), dto.getDiscount(), dto.getItemName(),
                dto.getUnitType(), dto.getPrinterType(), dto.getCostPrice(), dto.getUnitPrice(),
                dto.getImagePath(), dto.getGrnStatus(), dto.getSellingStatus(), dto.getStatus(),
                dto.getUserId(), dto.getVisible(), dto.getWeight(), dto.getItemId());
    }

    public int softDelete(int itemId) {
        String sql = "UPDATE pos_main_item_tb SET status = 0 WHERE item_id = ?";
        return jdbcTemplate.update(sql, itemId);
    }
}