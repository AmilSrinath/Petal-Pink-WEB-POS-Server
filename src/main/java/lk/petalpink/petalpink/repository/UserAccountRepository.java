package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.UserAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserAccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BASE =
            "SELECT u.user_id, u.employee_id, u.role_id, u.username, u.status, " +
                    "       e.employee_name AS employee_name, " +
                    "       r.role           AS role_name " +
                    "FROM   pos_main_user_tb u " +
                    "LEFT JOIN pos_emp_employee_management_tb e ON e.employee_id = u.employee_id " +
                    "LEFT JOIN pos_main_user_role_tb r          ON r.role_id = u.role_id ";

    private final RowMapper<UserAccountDTO> rowMapper = new RowMapper<>() {
        @Override
        public UserAccountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserAccountDTO dto = new UserAccountDTO();
            dto.setUserId(rs.getInt("user_id"));
            dto.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
            dto.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
            dto.setUsername(rs.getString("username"));
            dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
            dto.setEmployeeName(rs.getString("employee_name"));
            dto.setRoleName(rs.getString("role_name"));
            return dto;
        }
    };

    public List<UserAccountDTO> findAll() {
        String sql = SELECT_BASE + "WHERE u.visible = 1 OR u.visible IS NULL ORDER BY u.user_id DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public UserAccountDTO findById(int userId) {
        String sql = SELECT_BASE + "WHERE u.user_id = ?";
        List<UserAccountDTO> results = jdbcTemplate.query(sql, rowMapper, userId);
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM pos_main_user_tb WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    // dto.getPassword() must already be the BCrypt-hashed password
    public int save(UserAccountDTO dto) {
        String sql = "INSERT INTO pos_main_user_tb " +
                "(employee_id, role_id, username, password, status, visible) " +
                "VALUES (?, ?, ?, ?, ?, 1)";
        return jdbcTemplate.update(sql,
                dto.getEmployeeId(), dto.getRoleId(), dto.getUsername(),
                dto.getPassword(), dto.getStatus());
    }

    // updates everything except the password
    public int update(UserAccountDTO dto) {
        String sql = "UPDATE pos_main_user_tb SET employee_id = ?, role_id = ?, username = ?, status = ? " +
                "WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                dto.getEmployeeId(), dto.getRoleId(), dto.getUsername(),
                dto.getStatus(), dto.getUserId());
    }

    // updates everything including the (already hashed) password
    public int updateWithPassword(UserAccountDTO dto) {
        String sql = "UPDATE pos_main_user_tb SET employee_id = ?, role_id = ?, username = ?, status = ?, password = ? " +
                "WHERE user_id = ?";
        return jdbcTemplate.update(sql,
                dto.getEmployeeId(), dto.getRoleId(), dto.getUsername(),
                dto.getStatus(), dto.getPassword(), dto.getUserId());
    }

    public int softDelete(int userId) {
        String sql = "UPDATE pos_main_user_tb SET visible = 0 WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}