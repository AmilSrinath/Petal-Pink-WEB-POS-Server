package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.PrinterTypeDTO;
import lk.petalpink.petalpink.repository.PrinterTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrinterTypeService {
    @Autowired private PrinterTypeRepository repo;

    public String create(PrinterTypeDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<PrinterTypeDTO> getAll() { return repo.findAll(); }
    public String update(PrinterTypeDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}