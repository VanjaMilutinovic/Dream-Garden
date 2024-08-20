package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.Service;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.ServiceRepository;
import dreamgarden.request.CreateServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing Service-related operations.
 * Provides endpoints to interact with Service entities.
 * 
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/getAll")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/getByCompanyId")
    public ResponseEntity<?> getServiceById(@RequestParam Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + companyId);
        } 
        List<Service> services = serviceRepository.findByCompanyId(company.get());
        if (services.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No services found for companyId " + companyId);
        }
        return ResponseEntity.ok(services);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createService(@RequestBody CreateServiceRequest request) {
        Optional<Company> company = companyRepository.findById(request.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + request.getCompanyId());
        }
        List<Service> serviceByCompanyIdAndServiceName = serviceRepository.findByCompanyIdAndServiceName(company.get(), request.getServiceName());
        if (!serviceByCompanyIdAndServiceName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Company must have unique service names. Conflicts with: " + serviceByCompanyIdAndServiceName);
        }
        Service service = new Service();
        service.setPrice(request.getPrice());
        service.setServiceName(request.getServiceName());
        service.setServiceDescription(request.getServiceDescription());
        service.setCompanyId(company.get());
        service = serviceRepository.saveAndFlush(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }
    
    @PostMapping("/delete")
    public ResponseEntity<String> deleteService(@RequestParam Integer serviceId) {
        if (serviceRepository.existsById(serviceId)) {
            serviceRepository.deleteById(serviceId);
            return ResponseEntity.ok("Service deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found for ID " + serviceId);
        }
    }

}
