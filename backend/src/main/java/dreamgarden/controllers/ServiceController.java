package dreamgarden.controllers;

import dreamgarden.entities.Service;
import dreamgarden.repositories.ServiceRepository;
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
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/getAll")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getServiceById(@RequestParam Integer serviceId) {
        Optional<Service> service = serviceRepository.findById(serviceId);
        if (service.isPresent()) {
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createService(@RequestBody Service service) {
        serviceRepository.save(service);
        return ResponseEntity.status(HttpStatus.CREATED).body("Service added successfully");
    }
    
    @PostMapping("/delete")
    public ResponseEntity<String> deleteService(@RequestParam Integer serviceId) {
        Optional<Service> existingService = serviceRepository.findById(serviceId);
        if (existingService.isPresent()) {
            serviceRepository.deleteById(serviceId);
            return ResponseEntity.ok("Service deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
    }
    
    @PostMapping("/update")
    public ResponseEntity<String> updateService(@RequestParam Integer serviceId, @RequestBody Service updatedService) {
        Optional<Service> existingService = serviceRepository.findById(serviceId);
        if (existingService.isPresent()) {
            Service service = existingService.get();
            service.setPrice(updatedService.getPrice());
            service.setServiceName(updatedService.getServiceName());
            service.setServiceDescription(updatedService.getServiceDescription());
            service.setCompanyId(updatedService.getCompanyId());
            serviceRepository.save(service);
            return ResponseEntity.ok("Service updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }
    }
}
