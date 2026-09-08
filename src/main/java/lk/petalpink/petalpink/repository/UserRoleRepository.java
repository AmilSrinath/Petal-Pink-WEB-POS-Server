package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.UserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<UserRoleDTO> rowMapper = new RowMapper<>() {
        @Override
        public UserRoleDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRoleDTO dto = new UserRoleDTO();
            dto.setRoleId(rs.getInt("role_id"));
            dto.setRoleName(rs.getString("role"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            return dto;
        }
    };

    public List<UserRoleDTO> findAll() {
        String sql = "SELECT role_id, role, status FROM pos_main_user_role_tb " +
                "WHERE visible = 1 OR visible IS NULL ORDER BY role_id DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public UserRoleDTO findById(int roleId) {
        String sql = "SELECT role_id, role, status FROM pos_main_user_role_tb WHERE role_id = ?";
        List<UserRoleDTO> results = jdbcTemplate.query(sql, rowMapper, roleId);
        return results.isEmpty() ? null : results.get(0);
    }

    public int save(UserRoleDTO dto) {
        String sql = "INSERT INTO pos_main_user_role_tb (role, status, visible) VALUES (?, ?, 1)";
        return jdbcTemplate.update(sql, dto.getRoleName(), dto.getStatus());
    }

    public int update(UserRoleDTO dto) {
        String sql = "UPDATE pos_main_user_role_tb SET role = ?, status = ? WHERE role_id = ?";
        return jdbcTemplate.update(sql, dto.getRoleName(), dto.getStatus(), dto.getRoleId());
    }

    public int softDelete(int roleId) {
        String sql = "UPDATE pos_main_user_role_tb SET visible = 0 WHERE role_id = ?";
        return jdbcTemplate.update(sql, roleId);
    }
}