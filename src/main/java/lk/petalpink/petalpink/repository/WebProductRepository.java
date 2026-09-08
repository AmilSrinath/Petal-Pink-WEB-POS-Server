package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class WebProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WebProductVariantRepository webProductVariantRepository;

    private static final RowMapper<WebsiteProductDTO> PRODUCT_ROW_MAPPER = (rs, rowNum) -> {
        WebsiteProductDTO dto = new WebsiteProductDTO();
        dto.setProductId(rs.getInt("product_id"));
        dto.setProduct_name(rs.getString("product_name"));
        dto.setUnit_type(rs.getString("unit_type"));
        dto.setProduct_price(rs.getDouble("product_price"));
        dto.setDiscount(rs.getDouble("discount"));
        dto.setStatus(rs.getInt("status"));
        dto.setVisible(rs.getInt("visible"));
        dto.setCreateDate(rs.getTimestamp("create_date"));
        dto.setEditDate(rs.getTimestamp("edit_date"));
        dto.setImageUrl(rs.getString("image_url"));
        dto.setImage_url_2(rs.getString("image_url_2"));
        dto.setImage_url_3(rs.getString("image_url_3"));
        dto.setUserId(rs.getString("user_id"));
        dto.setWeight(rs.getDouble("weight"));
        dto.setAmount(rs.getDouble("amount"));
        dto.setDescription(rs.getString("description"));
        dto.setKeyPoints(rs.getString("keyPoints"));
        dto.setFaq(rs.getString("faq"));
        dto.setHowToUse(rs.getString("howToUse"));
        dto.setIsDeliveryFree(rs.getInt("is_delivery_free"));

        int mainCatId = rs.getInt("main_category_id");
        dto.setMainCategoryId(rs.wasNull() ? null : mainCatId);
        dto.setMainCategoryName(rs.getString("main_cat_name"));

        int subCatId = rs.getInt("sub_category_id");
        dto.setSubCategoryId(rs.wasNull() ? null : subCatId);
        dto.setSubCategoryName(rs.getString("sub_cat_name"));

        return dto;
    };

    public List<WebsiteProductDTO> getAllProducts() {
        List<WebsiteProductDTO> products = jdbcTemplate.query(
                """
                SELECT p.*, mc.main_category_name AS main_cat_name, sc.sub_category_name AS sub_cat_name
                FROM web_petal_pink_product_tb p
                LEFT JOIN web_petal_pink_main_category_tb mc ON p.main_category_id = mc.main_category_id
                LEFT JOIN web_petal_pink_sub_category_tb  sc ON p.sub_category_id  = sc.sub_category_id
                WHERE p.status = 1
                """,
                PRODUCT_ROW_MAPPER);
        attachVariants(products);
        return products;
    }

    public WebsiteProductDTO getProductById(Integer productId) {
        List<WebsiteProductDTO> result = jdbcTemplate.query(
                """
                SELECT p.*, mc.main_category_name AS main_cat_name, sc.sub_category_name AS sub_cat_name
                FROM web_petal_pink_product_tb p
                LEFT JOIN web_petal_pink_main_category_tb mc ON p.main_category_id = mc.main_category_id
                LEFT JOIN web_petal_pink_sub_category_tb  sc ON p.sub_category_id  = sc.sub_category_id
                WHERE p.product_id = ?
                """,
                PRODUCT_ROW_MAPPER, productId);
        if (result.isEmpty()) return null;
        attachVariants(result);
        return result.get(0);
    }

    public WebsiteProductDTO getProductByName(String productName) {
        List<WebsiteProductDTO> result = jdbcTemplate.query(
                """
                SELECT p.*, mc.main_category_name AS main_cat_name, sc.sub_category_name AS sub_cat_name
                FROM web_petal_pink_product_tb p
                LEFT JOIN web_petal_pink_main_category_tb mc ON p.main_category_id = mc.main_category_id
                LEFT JOIN web_petal_pink_sub_category_tb  sc ON p.sub_category_id  = sc.sub_category_id
                WHERE p.product_name = ?
                """,
                PRODUCT_ROW_MAPPER, productName);
        if (result.isEmpty()) return null;
        attachVariants(result);
        return result.get(0);
    }

    // ─── Variant (colors + sizes) helpers ─────────────────────────────────────

    // Attaches colors/sizes to every product in the list (only Clothing products will have any)
    private void attachVariants(List<WebsiteProductDTO> products) {
        for (WebsiteProductDTO p : products) {
            p.setColors(getColorsForProduct(p.getProductId()));
            p.setSizes(getSizesForProduct(p.getProductId()));
            p.setSizeGroups(getSizeGroupsForProduct(p.getProductId()));
        }
    }

    // Colors are stored against the master web_petal_pink_color_tb catalog; this product's
    // own row in web_petal_pink_product_color_tb just links to it and owns that product's images.
    private List<ProductColorDTO> getColorsForProduct(Integer productId) {
        // product_color_id is only needed to look up this row's images below; ProductColorDTO
        // itself exposes the master color_id, so we track the two ids side by side here.
        List<Object[]> rows = jdbcTemplate.query(
                """
                SELECT pc.product_color_id, pc.product_id, c.color_id, c.color_name, c.color_code
                FROM web_petal_pink_product_color_tb pc
                JOIN web_petal_pink_color_tb c ON c.color_id = pc.color_id
                WHERE pc.product_id = ? AND pc.status = 1
                ORDER BY pc.sort_order, pc.product_color_id
                """,
                (rs, rowNum) -> new Object[]{
                        rs.getInt("product_color_id"),
                        new ProductColorDTO(
                                rs.getInt("color_id"),
                                rs.getInt("product_id"),
                                rs.getString("color_name"),
                                rs.getString("color_code"),
                                new ArrayList<>())
                },
                productId);

        List<ProductColorDTO> colors = new ArrayList<>();
        for (Object[] row : rows) {
            int productColorId = (int) row[0];
            ProductColorDTO color = (ProductColorDTO) row[1];
            List<String> images = jdbcTemplate.queryForList(
                    """
                    SELECT image_url FROM web_petal_pink_product_color_image_tb
                    WHERE product_color_id = ? ORDER BY sort_order, image_id
                    """,
                    String.class, productColorId);
            color.setImages(images);
            colors.add(color);
        }
        return colors;
    }

    // Sizes are stored against the master web_petal_pink_size_tb catalog; this product's own
    // row in web_petal_pink_product_size_tb just links to it.
    private List<ProductSizeDTO> getSizesForProduct(Integer productId) {
        return jdbcTemplate.query(
                """
                SELECT s.size_id, ps.product_id, s.size_name
                FROM web_petal_pink_product_size_tb ps
                JOIN web_petal_pink_size_tb s ON s.size_id = ps.size_id
                WHERE ps.product_id = ? AND ps.status = 1
                ORDER BY ps.sort_order, s.size_id
                """,
                (rs, rowNum) -> new ProductSizeDTO(
                        rs.getInt("size_id"),
                        rs.getInt("product_id"),
                        rs.getString("size_name")),
                productId);
    }

    // Reconstructs the exact per-size color pairings (web_petal_pink_product_details_tb) so
    // the edit form can be pre-filled with the same "size -> its colors" grouping the admin
    // originally chose, rather than a flattened cross-join.
    private List<ProductSizeGroupDTO> getSizeGroupsForProduct(Integer productId) {
        List<ProductSizeDTO> sizes = getSizesForProduct(productId);
        if (sizes.isEmpty()) return new ArrayList<>();

        // color_id -> fully-populated ProductColorDTO (with its images), built once per product
        Map<Integer, ProductColorDTO> colorsById = new LinkedHashMap<>();
        for (ProductColorDTO color : getColorsForProduct(productId)) {
            colorsById.put(color.getColorId(), color);
        }

        // size_id -> ordered list of color_ids paired with it in product_details_tb.
        // NOTE: this lambda must have a block body ("-> { ... }") with no return value —
        // an expression body here (e.g. "-> list.add(x)") is ambiguous between
        // JdbcTemplate's RowCallbackHandler and ResultSetExtractor overloads and fails to compile.
        Map<Integer, List<Integer>> colorIdsBySize = new LinkedHashMap<>();
        jdbcTemplate.query(
                """
                SELECT pd.size_id, pd.color_id
                FROM web_petal_pink_product_details_tb pd
                WHERE pd.product_id = ? AND pd.status = 1
                ORDER BY pd.size_id, pd.detail_id
                """,
                (RowCallbackHandler) rs -> {
                    colorIdsBySize
                            .computeIfAbsent(rs.getInt("size_id"), k -> new ArrayList<>())
                            .add(rs.getInt("color_id"));
                },
                productId);

        List<ProductSizeGroupDTO> groups = new ArrayList<>();
        for (ProductSizeDTO size : sizes) {
            List<ProductColorDTO> groupColors = new ArrayList<>();
            for (Integer colorId : colorIdsBySize.getOrDefault(size.getSizeId(), new ArrayList<>())) {
                ProductColorDTO color = colorsById.get(colorId);
                if (color != null) groupColors.add(color);
            }
            groups.add(new ProductSizeGroupDTO(size.getSizeId(), size.getSizeName(), groupColors));
        }
        return groups;
    }

    // Removes all existing color/size links (cascades to color images) plus this product's
    // size × color combination matrix, without touching the master catalogs.
    private void deleteVariants(Integer productId) {
        jdbcTemplate.update("DELETE FROM web_petal_pink_product_details_tb WHERE product_id = ?", productId);
        jdbcTemplate.update("DELETE FROM web_petal_pink_product_color_tb WHERE product_id = ?", productId);
        jdbcTemplate.update("DELETE FROM web_petal_pink_product_size_tb WHERE product_id = ?", productId);
    }

    // Links the product to its sizes and colors via the master catalogs, using the EXACT
    // per-size color pairings the admin chose (no cross-joining sizes with colors from a
    // different size group). The same color reused across multiple sizes only gets one
    // web_petal_pink_product_color_tb row (and one set of images) for this product.
    private void insertVariants(Integer productId, List<ProductSizeGroupRequestDTO> sizeGroups) {
        if (sizeGroups == null) return;

        Map<Integer, Integer> productColorIdByColorId = new LinkedHashMap<>(); // colorId -> product_color_id
        int colorOrder = 0;
        int sizeOrder = 0;

        for (ProductSizeGroupRequestDTO group : sizeGroups) {
            if (group.getSizeName() == null || group.getSizeName().isBlank()) continue;

            Integer sizeId = webProductVariantRepository.getOrCreateSizeId(group.getSizeName());

            jdbcTemplate.update(
                    """
                    INSERT IGNORE INTO web_petal_pink_product_size_tb
                        (product_id, size_id, sort_order, status, create_date)
                    VALUES (?, ?, ?, 1, NOW())
                    """,
                    productId, sizeId, sizeOrder++);

            if (group.getColors() == null) continue;

            for (ProductColorRequestDTO color : group.getColors()) {
                if (color.getColorName() == null || color.getColorName().isBlank()) continue;

                Integer colorId = webProductVariantRepository.getOrCreateColorId(color.getColorName(), color.getColorCode());

                Integer productColorId = productColorIdByColorId.get(colorId);
                if (productColorId == null) {
                    final int sortOrder = colorOrder++;
                    KeyHolder kh = new GeneratedKeyHolder();
                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(
                                """
                                INSERT INTO web_petal_pink_product_color_tb
                                    (product_id, color_id, sort_order, status, create_date, edit_date)
                                VALUES (?, ?, ?, 1, NOW(), NOW())
                                """,
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setInt(1, productId);
                        ps.setInt(2, colorId);
                        ps.setInt(3, sortOrder);
                        return ps;
                    }, kh);
                    productColorId = Objects.requireNonNull(kh.getKey()).intValue();
                    productColorIdByColorId.put(colorId, productColorId);

                    if (color.getImages() != null) {
                        int imgOrder = 0;
                        for (String imageUrl : color.getImages()) {
                            if (imageUrl == null || imageUrl.isBlank()) continue;
                            jdbcTemplate.update(
                                    """
                                    INSERT INTO web_petal_pink_product_color_image_tb
                                        (product_color_id, image_url, sort_order, create_date)
                                    VALUES (?, ?, ?, NOW())
                                    """,
                                    productColorId, imageUrl, imgOrder++);
                        }
                    }
                }

                // Exact size↔color pairing for this product — only what this size group asked for.
                jdbcTemplate.update(
                        """
                        INSERT IGNORE INTO web_petal_pink_product_details_tb
                            (product_id, size_id, color_id, status, create_date)
                        VALUES (?, ?, ?, 1, NOW())
                        """,
                        productId, sizeId, colorId);
            }
        }
    }

    public void saveProduct(WebsiteProductSaveRequestDTO req, String img1, String img2, String img3) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    INSERT INTO web_petal_pink_product_tb
                        (product_name, unit_type, product_price, discount, status, visible,
                         create_date, edit_date, image_url, image_url_2, image_url_3,
                         user_id, weight, amount, description, keyPoints, faq, howToUse,
                         main_category_id, sub_category_id)
                    VALUES (?,?,?,?,1,1,NOW(),NOW(),?,?,?,1,?,?,?,?,?,?,?,?)
                    """,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, req.getProductName());
            ps.setString(2, req.getUnitType() != null ? req.getUnitType() : "ml");
            ps.setDouble(3, req.getProductPrice() != null ? req.getProductPrice() : 0);
            ps.setDouble(4, req.getDiscount() != null ? req.getDiscount() : 0);
            ps.setString(5, img1);
            ps.setString(6, img2);
            ps.setString(7, img3);
            if (req.getWeight() != null) ps.setDouble(8, req.getWeight()); else ps.setNull(8, java.sql.Types.DOUBLE);
            ps.setDouble(9, req.getAmount() != null ? req.getAmount() : 0);
            ps.setString(10, req.getDescription() != null ? req.getDescription() : "");
            ps.setString(11, req.getKeyPoints() != null ? req.getKeyPoints() : "");
            ps.setString(12, req.getFaq() != null ? req.getFaq() : "");
            ps.setString(13, req.getHowToUse() != null ? req.getHowToUse() : "");
            if (req.getMainCategoryId() != null) ps.setInt(14, req.getMainCategoryId()); else ps.setNull(14, java.sql.Types.INTEGER);
            if (req.getSubCategoryId() != null) ps.setInt(15, req.getSubCategoryId()); else ps.setNull(15, java.sql.Types.INTEGER);
            return ps;
        }, kh);

        int productId = Objects.requireNonNull(kh.getKey()).intValue();
        insertVariants(productId, req.getSizeGroups());
    }

    public int updateProduct(WebsiteProductUpdateRequestDTO req, String img1, String img2, String img3) {
        StringBuilder sql = new StringBuilder(
                """
                UPDATE web_petal_pink_product_tb SET
                    product_name=?, unit_type=?, product_price=?, discount=?,
                    weight=?, amount=?, description=?, keyPoints=?, faq=?, howToUse=?, sub_category_id=?, main_category_id=?
                """);
        List<Object> params = new ArrayList<>();
        params.add(req.getProductName());
        params.add(req.getUnitType() != null ? req.getUnitType() : "");
        params.add(req.getProductPrice() != null ? req.getProductPrice() : 0);
        params.add(req.getDiscount() != null ? req.getDiscount() : 0);
        params.add(req.getWeight() != null ? req.getWeight() : 0);
        params.add(req.getAmount() != null ? req.getAmount() : 0);
        params.add(req.getDescription() != null ? req.getDescription() : "");
        params.add(req.getKeyPoints() != null ? req.getKeyPoints() : "");
        params.add(req.getFaq() != null ? req.getFaq() : "");
        params.add(req.getHowToUse() != null ? req.getHowToUse() : "");
        params.add(req.getSubCategoryId() != null ? req.getSubCategoryId() : "");
        params.add(req.getMainCategoryId() != null ? req.getMainCategoryId() : "");

        if (img1 != null) { sql.append(", image_url=?");   params.add(img1); }
        if (img2 != null) { sql.append(", image_url_2=?"); params.add(img2); }
        if (img3 != null) { sql.append(", image_url_3=?"); params.add(img3); }

        sql.append(" WHERE product_id=?");
        params.add(req.getProductId());

        int rows = jdbcTemplate.update(sql.toString(), params.toArray());

        // Only touch variants if the frontend actually sent variant data for this update
        // (sizeGroups == null means "no change requested", an empty list means "clear all")
        if (rows > 0 && req.getSizeGroups() != null) {
            deleteVariants(req.getProductId());
            insertVariants(req.getProductId(), req.getSizeGroups());
        }

        return rows;
    }

    public int deleteProduct(Integer productId) {
        return jdbcTemplate.update(
                "UPDATE web_petal_pink_product_tb SET status = 0 WHERE product_id = ?", productId);
    }
}