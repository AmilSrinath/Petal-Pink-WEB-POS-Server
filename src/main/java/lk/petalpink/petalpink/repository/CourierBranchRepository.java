package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.CourierBranchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CourierBranchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(CourierBranchDTO dto) {
        String sql = "INSERT INTO pos_courier_branch_tb (branch_name, branch_contact, created_date, edited_date, company_id, status, user_id) VALUES (?, ?, ?, ?, ?, 1, ?)";
        return jdbcTemplate.update(sql,
                dto.getBranchName(), dto.getBranchContact(),
                LocalDateTime.now(), LocalDateTime.now(),
                dto.getCompanyId(), dto.getUserId()
        );
    }

    public List<CourierBranchDTO> findAll() {
        String sql = """
            SELECT b.*, c.company_name AS company
            FROM pos_courier_branch_tb b
            JOIN pos_courier_company_tb c ON b.company_id = c.company_id
            WHERE b.status != 0
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CourierBranchDTO.class));
    }

    public List<CourierBranchDTO> findByCompanyId(int companyId) {
        String sql = """
            SELECT b.*, c.company_name AS company
            FROM pos_courier_branch_tb b
            JOIN pos_company_tb c ON b.company_id = c.company_id
            WHERE b.company_id = ? AND b.status != 0
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CourierBranchDTO.class), companyId);
    }

    public int update(CourierBranchDTO dto) {
        String sql = "UPDATE pos_courier_branch_tb SET branch_name=?, branch_contact=?, edited_date=?, company_id=?, status=?, user_id=? WHERE branch_id=?";
        return jdbcTemplate.update(sql,
                dto.getBranchName(), dto.getBranchContact(),
                LocalDateTime.now(),
                dto.getCompanyId(),           // still use companyId for FK
                dto.getStatus(), dto.getUserId(), dto.getBranchId()
        );
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_courier_branch_tb SET status = 0 WHERE branch_id = ?", id);
    }
}