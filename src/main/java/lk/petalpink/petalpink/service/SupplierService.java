package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.SupplierDTO;
import lk.petalpink.petalpink.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public String createSupplier(SupplierDTO dto) {
        int rows = supplierRepository.save(dto);
        return rows > 0 ? "Supplier created successfully" : "Failed to create supplier";
    }

    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public String updateSupplier(SupplierDTO dto) {
        int rows = supplierRepository.update(dto);
        return rows > 0 ? "Supplier updated successfully" : "Supplier not found or update failed";
    }

    public String deleteSupplier(int supplierId) {
        int rows = supplierRepository.softDelete(supplierId);
        return rows > 0 ? "Supplier deleted successfully" : "Supplier not found";
    }
}