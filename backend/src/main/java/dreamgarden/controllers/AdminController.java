/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.User;
import dreamgarden.entities.UserStatus;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.UserStatusRepository;
import dreamgarden.repositories.UserTypeRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.CreateUserRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

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
    
    @Autowired
    private UserController userController;
    
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam(name = "username", required = true) String username, 
                                   @RequestParam(name = "hashedPassword", required = true) String hashedPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        if (!user.get().getHashedPassword().equals(hashedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        if (user.get().getUserStatusId().getUserStatusId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be active!");
        }
        if (user.get().getUserTypeId().getUserTypeId() != 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not allowed to login!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }
    
    @GetMapping("/user/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/user/setStatus")
    public ResponseEntity<?> setUserStatus(@RequestParam(name = "userId", required = true) Integer userId, 
                                           @RequestParam(name = "userStatusId", required = true) Integer userStatusId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + userId);
        }

        Optional<UserStatus> userStatus= userStatusRepository.findById(userStatusId);
        if (userStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User status not found for ID: " + userStatusId);
        }
        user.get().setUserStatusId(userStatus.get());
        User savedUser = userRepository.save(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }
    
    @PostMapping("/user/createWorker")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request){
        request.setUserTypeId(2);
        if (request.getPhotoPath() == null)
            request.setPhotoPath("default-profile.jpg");
        return userController.createUser(request);
    }
    
    @PostMapping("/worker/employ")
    public ResponseEntity<String> addCompanyToWorker(@RequestParam(name = "userId", required = true) Integer userId, 
                                                     @RequestParam(name = "companyId", required = true) Integer companyId) {
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
