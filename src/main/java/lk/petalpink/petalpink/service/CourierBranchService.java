package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.CourierBranchDTO;
import lk.petalpink.petalpink.repository.CourierBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierBranchService {
    @Autowired
    private CourierBranchRepository repo;

    public String create(CourierBranchDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<CourierBranchDTO> getAll() { return repo.findAll(); }
    public List<CourierBranchDTO> getByCompany(int companyId) { return repo.findByCompanyId(companyId); }
    public String update(CourierBranchDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}