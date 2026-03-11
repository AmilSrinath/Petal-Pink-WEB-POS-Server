package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.PaymentTypeDTO;
import lk.petalpink.petalpink.repository.PaymentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeService {
    @Autowired
    private PaymentTypeRepository repo;

    public String create(PaymentTypeDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<PaymentTypeDTO> getAll() { return repo.findAll(); }
    public String update(PaymentTypeDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}