package lk.petalpink.petalpink.service;

import lk.petalpink.petalpink.dto.website.WebsiteCustomerDTO;
import lk.petalpink.petalpink.repository.WebCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebCustomerService {

    @Autowired
    private WebCustomerRepository webCustomerRepository;

    public List<WebsiteCustomerDTO> getAllCustomers() {
        return webCustomerRepository.getAllCustomers();
    }

    public long getCustomerCount() {
        return webCustomerRepository.getCustomerCount();
    }
}