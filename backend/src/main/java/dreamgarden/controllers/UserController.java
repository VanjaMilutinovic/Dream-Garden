package dreamgarden.controllers;

import dreamgarden.entities.*;
import dreamgarden.repositories.*;
import dreamgarden.request.CreateUserRequest;
import dreamgarden.request.UpdateUserRequest;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobPhotoRepository jobPhotoRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;


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
        if (user.get().getUserStatusId().getUserStatusId() != 2 && user.get().getUserStatusId().getUserStatusId() != 4) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be active!");
        }
        if (user.get().getUserTypeId().getUserTypeId() != 1 && user.get().getUserTypeId().getUserTypeId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not allowed to login!");
        }
        if (user.get().getUserTypeId().getUserTypeId() == 2) {
            checkWorker(user.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    private void checkWorker(User user) {
        List<Job> finishedJobs = jobRepository.findByWorkerIdAndJobStatusId(user, new JobStatus(5));
        for (Job job : finishedJobs) {
            // Check if the job has any photos uploaded
            List<JobPhoto> photos = jobPhotoRepository.findByJobId(job);

            if (photos.isEmpty()) {
                // Check if more than 24 hours have passed since the job's end time
                Date currentDate = new Date();
                long timeDifference = currentDate.getTime() - job.getEndDateTime().getTime();
                long hoursDifference = timeDifference / (1000 * 60 * 60);

                if (hoursDifference > 24) {
                    user.setUserStatusId(new UserStatus(4));
                    userRepository.save(user);
                    return;
                }
            }
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam(name = "username", required = true) String username,
                                            @RequestParam(name = "oldHashedPassword", required = true) String oldHashedPassword,
                                            @RequestParam(name = "newHashedPassword", required = true) String newHashedPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOptional.get();
        if (!user.getHashedPassword().equals(oldHashedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }
        user.setHashedPassword(newHashedPassword);
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getUserById(@RequestParam(name = "userId", required = true) Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found for ID " + userId);
        }
    }

    @GetMapping("/getByUserTypeId")
    public ResponseEntity<?> getByUserTypeId(@RequestParam(name = "userTypeId", required = true) Integer userTypeId) {
        Optional<UserType> userType = userTypeRepository.findById(userTypeId);
        if (userType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserType not found for ID " + userTypeId);
        }
        List<User> user = userRepository.findByUserTypeId(userType.get());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users not found for UserTypeID " + userTypeId);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }
    
    @GetMapping("/worker/getUnemployed")
    public ResponseEntity<?> getUnemployed() {
        List<User> users = userRepository.findByUserTypeId(new UserType(2));
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nisu pronađeni dekorateri");
        } 
        List<User> unemployed = new ArrayList<>();
        for (User u: users) {
            Optional<Worker> employed = workerRepository.findByUserId(u);
            if (employed.isEmpty()) {
                unemployed.add(u);
            }
        }
        if (unemployed.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nisu pronađeni nezaposleni dekorateri. Kreirajte radnike!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(unemployed);
    }
    
    @GetMapping("/worker/getEmployed")
    public ResponseEntity<?> getEmployed(@RequestParam(name = "companyId", required = true) Integer companyId) {
        Optional<Company> companies = companyRepository.findById(companyId);
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema kompanije za ID " + companyId);
        }
        List<Worker> employed = workerRepository.findByCompanyId(companies.get());
        List<User> users = new ArrayList<>();
        for (Worker w: employed) {
            users.add(w.getUserId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    
    @GetMapping("/worker/get")
    public ResponseEntity<?> getWorker(@RequestParam(name = "userId", required = true) Integer userId) {
        Optional<User> users = userRepository.findById(userId);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nije pronađen korisnik");
        } 
        Optional<Worker> worker = workerRepository.findByUserId(users.get());
        if (worker.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dekorater nije zaposlen");
        return ResponseEntity.status(HttpStatus.OK).body(worker.get());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request){
        if (request.getCreditCardNumber() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Credit card is mandatory for owners");
        }
        request.setUserTypeId(1);
        return createUser(request);
    }

    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        if (!request.checkCreateUserRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("CreateUserRequest not adequate");
        }
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
        
        User user = new User();
        if(request.getBase64() != null){    
            Optional<Photo> photoOptional = photoRepository.findByBase64(request.getBase64());
            log.info("photoOptional: " + photoOptional);
            Photo photo;
            if (photoOptional.isEmpty()){
                photo = new Photo();
                photo.setBase64(request.getBase64());
                photo = photoRepository.saveAndFlush(photo);
            }
            else{
                photo=photoOptional.get();
            }
            user.setPhotoId(photo);
        }
        if (!request.getGender().equals('F') && !request.getGender().equals('M')){
           return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Gender must be 'F' or 'M', provided gender not acceptable: " + request.getGender());
        }
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
        if (userType.get().getUserTypeId()==1)
            user.setUserStatusId(new UserStatus(1)); //pending
        else
            user.setUserStatusId(new UserStatus(2));
        user = userRepository.saveAndFlush(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@RequestBody UpdateUserRequest request) {
        if (!request.checkUpdateUserRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("UpdateUserRequest not adequate");
        }
        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID " + request.getUserId());
        }
        Optional<Photo> findByPath = photoRepository.findByBase64(request.getBase64());
        if (findByPath.isEmpty()){
            Photo photo = new Photo();
            photo.setBase64(request.getBase64());
            photo = photoRepository.saveAndFlush(photo);
            user.get().setPhotoId(photo);
        } else {
            user.get().setPhotoId(findByPath.get());
        }
        user.get().setAddress(request.getAddress());
        user.get().setEmail(request.getEmail());
        user.get().setName(request.getName());
        user.get().setLastname(request.getLastname());
        user.get().setContactNumber(request.getContactNumber());
        user.get().setCreditCardNumber(request.getCreditCardNumber());
        User savedUser = userRepository.saveAndFlush(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam(name = "userId", required = true) Integer userId) {
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
    public ResponseEntity<?> getUserStatusById(@RequestParam(name = "userStatusId", required = true) Integer userStatusId) {
        Optional<UserStatus> userStatusOptional = userStatusRepository.findById(userStatusId);
        if (userStatusOptional.isPresent()) {
            return ResponseEntity.ok(userStatusOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("UserStatus not found for ID " + userStatusId);
        }
    }

    @GetMapping("/status/findByStatus")
    public ResponseEntity<?> getUserStatusByStatus(@RequestParam(name = "status", required = true) String status) {
        Optional<UserStatus> userStatus = userStatusRepository.findByStatus(status);
        if (userStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserStatus not found for status " + status);
        } else {
            return ResponseEntity.ok(userStatus);
        }
    }

}
