package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<UserDTO> getAllUsers() {
        String sql = "SELECT user_id, employee_id, role_id, username, status, visible, token " +
                "FROM pos_main_user_tb WHERE visible = 1";

        return jdbcTemplate.query(sql, new RowMapper<UserDTO>() {
            @Override
            public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDTO dto = new UserDTO();
                dto.setUserId(rs.getInt("user_id"));
                dto.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
                dto.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                dto.setUsername(rs.getString("username"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
                dto.setToken(rs.getString("token"));
                return dto;
            }
        });
    }

    public UserDTO getUserById(Integer userId) {
        String sql = "SELECT user_id, employee_id, role_id, username, status, visible, token " +
                "FROM pos_main_user_tb WHERE user_id = ? AND visible = 1";

        return jdbcTemplate.queryForObject(sql, new RowMapper<UserDTO>() {
            @Override
            public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDTO dto = new UserDTO();
                dto.setUserId(rs.getInt("user_id"));
                dto.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
                dto.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                dto.setUsername(rs.getString("username"));
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
                dto.setToken(rs.getString("token"));
                return dto;
            }
        }, userId);
    }

    public UserDTO getUserByUsername(String username) {
        String sql = "SELECT user_id, employee_id, role_id, username, password, status, visible, token " +
                "FROM pos_main_user_tb WHERE username = ? AND visible = 1";

        return jdbcTemplate.queryForObject(sql, new RowMapper<UserDTO>() {
            @Override
            public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDTO dto = new UserDTO();
                dto.setUserId(rs.getInt("user_id"));
                dto.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
                dto.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                dto.setUsername(rs.getString("username"));
                dto.setPassword(rs.getString("password")); // needed for BCrypt check
                dto.setStatus(rs.getObject("status") != null ? rs.getInt("status") : null);
                dto.setVisible(rs.getObject("visible") != null ? rs.getInt("visible") : null);
                dto.setToken(rs.getString("token"));
                return dto;
            }
        }, username);
    }
}