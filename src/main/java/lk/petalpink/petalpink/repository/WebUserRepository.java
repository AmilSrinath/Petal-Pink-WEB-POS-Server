package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.website.WebsiteUserDTO;
import lk.petalpink.petalpink.dto.website.WebsiteUserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WebUserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUser(String employeeId, String email, String password, String name,
                         String nic, String role, Integer status, Integer visible, String imageUrl) {
        List<String> lastIdList = jdbcTemplate.queryForList(
                "SELECT user_id FROM petal_pink_user_tb ORDER BY user_id DESC LIMIT 1", String.class);
        String lastId = lastIdList.isEmpty() ? null : lastIdList.get(0);
        String newUserId = generateNextUserId(lastId);

        jdbcTemplate.update(
                "INSERT INTO petal_pink_user_tb (user_id, employee_id, email, password, role, status, visible, name, nic, image_url) VALUES (?,?,?,?,?,?,?,?,?,?)",
                newUserId, employeeId, email, password, role, status, visible, name, nic, imageUrl);
    }

    public List<WebsiteUserDTO> getAllWebsiteUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM petal_pink_user_tb WHERE visible = 1",
                new BeanPropertyRowMapper<>(WebsiteUserDTO.class));
    }

    public WebsiteUserDTO getUserByEmail(String email) {
        List<WebsiteUserDTO> result = jdbcTemplate.query(
                "SELECT * FROM petal_pink_user_tb WHERE email = ? AND visible = 1",
                new BeanPropertyRowMapper<>(WebsiteUserDTO.class), email);
        return result.isEmpty() ? null : result.get(0);
    }

    public int updateUser(String userId, WebsiteUserUpdateDTO dto) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET employee_id=?, email=?, role=?, status=?, visible=? WHERE user_id=?",
                dto.getEmployeeId(), dto.getEmail(), dto.getRole(),
                dto.getStatus() != null ? dto.getStatus() : 1,
                dto.getVisible() != null ? dto.getVisible() : 1,
                userId);
    }

    public int deleteUser(String userId) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET visible = 0 WHERE user_id = ?", userId);
    }

    public int setResetCode(String email, String code) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET reset_code = ?, reset_code_expiry = DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE email = ?",
                code, email);
    }

    public void verifyResetCode(String email, String code) {
        List<Object[]> rows = jdbcTemplate.query(
                "SELECT reset_code, reset_code_expiry FROM petal_pink_user_tb WHERE email = ?",
                (rs, rowNum) -> new Object[]{rs.getString("reset_code"), rs.getTimestamp("reset_code_expiry")},
                email);

        if (rows.isEmpty()) throw new RuntimeException("User not found.");
        String storedCode = (String) rows.get(0)[0];
        java.sql.Timestamp expiry = (java.sql.Timestamp) rows.get(0)[1];

        if (!code.equals(storedCode)) throw new RuntimeException("Invalid reset code.");
        if (expiry != null && expiry.before(new java.util.Date())) throw new RuntimeException("Reset code has expired.");
    }

    public void resetPassword(String email, String password) {
        jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET password = ?, reset_code = NULL, reset_code_expiry = NULL WHERE email = ?",
                password, email);
    }

    public int updatePassword(String email, String password) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET password = ? WHERE email = ?", password, email);
    }

    public int updateProfilePicture(String email, String imageUrl) {
        return jdbcTemplate.update(
                "UPDATE petal_pink_user_tb SET image_url = ? WHERE email = ?", imageUrl, email);
    }

    private String generateNextUserId(String lastId) {
        if (lastId == null || lastId.isBlank()) return "U001";
        int num = Integer.parseInt(lastId.replace("U", ""));
        return "U" + String.format("%03d", num + 1);
    }
}