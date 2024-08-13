package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.User;
import dreamgarden.entities.UserStatus;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.UserStatusRepository;
import dreamgarden.repositories.UserTypeRepository;
import dreamgarden.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing User and UserStatus-related operations.
 * Provides endpoints to interact with User and UserStatus entities.
 * 
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;
    
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getUserById(@RequestParam Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found for ID " + userId);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok("User saved successfully");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found for ID " + userId);
        }
    }
        
    @GetMapping("/status/getAll")
    public ResponseEntity<?> getAllUserStatuses() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return ResponseEntity.ok(userStatuses);
    }

    @GetMapping("/status/getById")
    public ResponseEntity<?> getUserStatusById(@RequestParam Integer userStatusId) {
        Optional<UserStatus> userStatusOptional = userStatusRepository.findById(userStatusId);
        if (userStatusOptional.isPresent()) {
            return ResponseEntity.ok(userStatusOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("UserStatus not found for ID " + userStatusId);
        }
    }

    @GetMapping("/status/findByStatus")
    public ResponseEntity<?> getUserStatusByStatus(@RequestParam String status) {
        List<UserStatus> userStatuses = userStatusRepository.findByStatus(status);
        if (userStatuses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("UserStatus not found for status " + status);
        } else {
            return ResponseEntity.ok(userStatuses);
        }
    }

    @PostMapping("/status/save")
    public ResponseEntity<String> saveUserStatus(@RequestBody UserStatus userStatus) {
        userStatusRepository.save(userStatus);
        return ResponseEntity.ok("UserStatus saved successfully");
    }

    @PostMapping("/status/delete")
    public ResponseEntity<String> deleteUserStatus(@RequestParam Integer userStatusId) {
        if (userStatusRepository.existsById(userStatusId)) {
            userStatusRepository.deleteById(userStatusId);
            return ResponseEntity.ok("UserStatus deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("UserStatus not found for ID " + userStatusId);
        }
    }
    
    @PostMapping("/worker/addCompany")
    public ResponseEntity<String> addCompanyToWorker(@RequestParam Integer userId, @RequestParam Integer companyId) {
        // Check if the user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + userId);
        }

        // Check if the company exists
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for ID " + companyId);
        }

        // Check if the worker exists
        Worker worker = workerRepository.findByUserId(userId);
        if (worker == null) {
            // If not, create a new Worker entity
            worker = new Worker();
            worker.setUserId(userId);
            worker.setUser(user);
        }

        // Set the company for the worker
        worker.setCompanyId(company);

        // Save the worker entity
        workerRepository.save(worker);

        return ResponseEntity.ok("Company added to Worker successfully");
    }
}
