package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.OrderTypeDTO;
import lk.petalpink.petalpink.repository.OrderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTypeService {
    @Autowired
    private OrderTypeRepository repo;

    public String create(OrderTypeDTO dto) { return repo.save(dto) > 0 ? "Created" : "Failed"; }
    public List<OrderTypeDTO> getAll() { return repo.findAll(); }
    public String update(OrderTypeDTO dto) { return repo.update(dto) > 0 ? "Updated" : "Not found or failed"; }
    public String delete(int id) { return repo.softDelete(id) > 0 ? "Deleted" : "Not found"; }
}