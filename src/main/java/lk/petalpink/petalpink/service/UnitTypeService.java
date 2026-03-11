package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.UnitTypeDTO;
import lk.petalpink.petalpink.repository.UnitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitTypeService {
    @Autowired
    private UnitTypeRepository repo;

    public String create(UnitTypeDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<UnitTypeDTO> getAll() { return repo.findAll(); }
    public String update(UnitTypeDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}