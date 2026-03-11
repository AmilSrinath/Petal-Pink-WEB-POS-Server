package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CourierCompanyDTO;
import lk.petalpink.petalpink.repository.CourierCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierCompanyService {
    @Autowired
    private CourierCompanyRepository repo;

    public String create(CourierCompanyDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<CourierCompanyDTO> getAll() { return repo.findAll(); }
    public String update(CourierCompanyDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}