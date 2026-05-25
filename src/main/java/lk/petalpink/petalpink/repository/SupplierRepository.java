package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.SupplierDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SupplierRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(SupplierDTO dto) {
        String sql = "INSERT INTO pos_inv_supplier_tb " +
                "(salesman_name, company_name, brand_name, telephone, phone, addree, gmail, status, user_id, visible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.getSalesmanName(), dto.getCompanyName(), dto.getBrandName(),
                dto.getTelephone(), dto.getPhone(), dto.getAddree(), dto.getGmail(),
                dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<SupplierDTO> findAll() {
        String sql = "SELECT * FROM pos_inv_supplier_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SupplierDTO.class));
    }

    public int update(SupplierDTO dto) {
        String sql = "UPDATE pos_inv_supplier_tb " +
                "SET salesman_name=?, company_name=?, brand_name=?, telephone=?, phone=?, addree=?, gmail=?, status=?, user_id=?, visible=? " +
                "WHERE supplier_id=?";
        return jdbcTemplate.update(sql,
                dto.getSalesmanName(), dto.getCompanyName(), dto.getBrandName(),
                dto.getTelephone(), dto.getPhone(), dto.getAddree(), dto.getGmail(),
                dto.getStatus(), dto.getUserId(), dto.getVisible(),
                dto.getSupplierId());
    }

    public int softDelete(int supplierId) {
        String sql = "UPDATE pos_inv_supplier_tb SET status = 0 WHERE supplier_id = ?";
        return jdbcTemplate.update(sql, supplierId);
    }
}