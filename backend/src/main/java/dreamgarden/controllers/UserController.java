package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.Photo;
import dreamgarden.entities.User;
import dreamgarden.entities.UserStatus;
import dreamgarden.entities.UserType;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.UserStatusRepository;
import dreamgarden.repositories.UserTypeRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.CreateUserRequest;
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
    
    @Autowired
    private PhotoRepository photoRepository;
    
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

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        Optional<User> findByUsername = userRepository.findByUsername(request.getUsername());        
        if (findByUsername.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username must be unique");
        }
        Optional<User> findByEmail = userRepository.findByEmail(request.getEmail());
        if (findByEmail.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email must be unique");
        }
        Optional<UserType> userType = userTypeRepository.findById(request.getUserTypeId());
        if (userType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User type not found for ID " + request.getUserTypeId());
        }
        Optional<Photo> photo = photoRepository.findById(request.getPhotoId());
        if (photo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found for ID " + request.getPhotoId());
        }
        if (!request.getGender().equals('F') && !request.getGender().equals('M')){
           return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Gender must be 'F' or 'M', provided gender not acceptable: " + request.getGender());
        }
        Optional<UserStatus> userStatus = userStatusRepository.findByStatus("pending");
        User user = new User();
        user.setUsername(request.getUsername());
        user.setHashedPassword(request.getHashedPassword());
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setContactNumber(request.getContactNumber());
        user.setEmail(request.getEmail());
        user.setCreditCardNumber(request.getCreditCardNumber());
        user.setUserTypeId(userType.get());
        user.setPhotoId(photo.get());
        user.setUserStatusId(userStatus.get());
        user = userRepository.saveAndFlush(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
        Optional<UserStatus> userStatus = userStatusRepository.findByStatus(status);
        if (userStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserStatus not found for status " + status);
        } else {
            return ResponseEntity.ok(userStatus);
        }
    }
    
    @PostMapping("/worker/addCompany")
    public ResponseEntity<String> addCompanyToWorker(@RequestParam Integer userId, @RequestParam Integer companyId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + userId);
        }
        if (user.get().getUserTypeId().getUserTypeId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User must be a decorator instead of " + user.get().getUserTypeId().getName());
        }

        // Check if the company exists
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for ID " + companyId);
        }

        // Check if the worker already employed
        Optional<Worker> worker = workerRepository.findByUserId(user.get());
        if (worker.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Decorator already employed by " + company.get().getName());
        }
        Worker newWorker = new Worker();
        newWorker.setCompanyId(company.get());
        newWorker.setUserId(user.get());
        System.out.println(company.get());
        workerRepository.saveAndFlush(newWorker);

        return ResponseEntity.ok("Company added to Worker successfully");
    }
}
