package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.ReasonDTO;
import lk.petalpink.petalpink.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonService {
    @Autowired
    private ReasonRepository repo;

    public String create(ReasonDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<ReasonDTO> getAll() { return repo.findAll(); }
    public String update(ReasonDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}