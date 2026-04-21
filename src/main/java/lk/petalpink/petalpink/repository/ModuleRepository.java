package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.ModuleAccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModuleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get all modules with access flag for a specific user
    public List<ModuleAccessDTO> getModuleAccessByUser(int userId) {
        String sql =
                "SELECT " +
                        "m.module_id, " +
                        "m.module_name, " +
                        "CASE WHEN mw.module_wise_id IS NOT NULL THEN true ELSE false END AS has_access " +
                        "FROM pos_module_tb m " +
                        "LEFT JOIN pos_module_wise_tb mw " +
                        "ON m.module_id = mw.module_id " +
                        "AND mw.user_id = ? " +
                        "AND mw.status = 1 " +
                        "WHERE m.status = 1 " +
                        "ORDER BY m.module_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ModuleAccessDTO dto = new ModuleAccessDTO();
            dto.setModuleId(rs.getInt("module_id"));
            dto.setModuleName(rs.getString("module_name"));
            dto.setHasAccess(rs.getBoolean("has_access"));
            return dto;
        }, userId);
    }

    // Get only accessible modules for a specific user
    public List<ModuleAccessDTO> getAccessibleModulesByUser(int userId) {
        String sql =
                "SELECT " +
                        "m.module_id, " +
                        "m.module_name, " +
                        "true AS has_access " +
                        "FROM pos_module_tb m " +
                        "INNER JOIN pos_module_wise_tb mw " +
                        "ON m.module_id = mw.module_id " +
                        "WHERE mw.user_id = ? " +
                        "AND mw.status = 1 " +
                        "AND m.status = 1 " +
                        "ORDER BY m.module_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ModuleAccessDTO dto = new ModuleAccessDTO();
            dto.setModuleId(rs.getInt("module_id"));
            dto.setModuleName(rs.getString("module_name"));
            dto.setHasAccess(true);
            return dto;
        }, userId);
    }
}