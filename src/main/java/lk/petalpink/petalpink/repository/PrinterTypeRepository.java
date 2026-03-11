package lk.petalpink.petalpink.repository;

import lk.petalpink.petalpink.dto.PrinterTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PrinterTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(PrinterTypeDTO dto) {
        String sql = "INSERT INTO pos_main_printer_type_tb (printer_type, status, user_id, visible) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getPrinterType(), dto.getStatus(), dto.getUserId(), dto.getVisible());
    }

    public List<PrinterTypeDTO> findAll() {
        String sql = "SELECT * FROM pos_main_printer_type_tb WHERE status != 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PrinterTypeDTO.class));
    }

    public int update(PrinterTypeDTO dto) {
        String sql = "UPDATE pos_main_printer_type_tb SET printer_type=?, status=?, user_id=?, visible=? WHERE printer_type_id=?";
        return jdbcTemplate.update(sql, dto.getPrinterType(), dto.getStatus(), dto.getUserId(), dto.getVisible(), dto.getPrinterTypeId());
    }

    public int softDelete(int id) {
        return jdbcTemplate.update("UPDATE pos_main_printer_type_tb SET status = 0 WHERE printer_type_id = ?", id);
    }
}