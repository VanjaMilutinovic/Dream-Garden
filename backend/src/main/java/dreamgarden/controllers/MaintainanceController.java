package dreamgarden.controllers;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Maintainance;
import dreamgarden.entities.User;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.MaintainanceRepository;
import dreamgarden.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

/**
 * Controller for managing Maintainance-related operations.
 * Provides endpoints to interact with Maintainance entities.
 * 
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/maintainance")
public class MaintainanceController {

    @Autowired
    private MaintainanceRepository maintainanceRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobStatusRepository jobStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveMaintainance(
            @RequestParam(required = true) Integer maintainanceId,
            @RequestParam(required = false) Date requestDateTime,
            @RequestParam(required = false) Date startDateTime,
            @RequestParam(required = false) Date estimatedEndDateTime,
            @RequestParam Integer jobId,
            @RequestParam Integer jobStatusId,
            @RequestParam Integer workerId) {

        // Find the associated entities
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        Optional<JobStatus> jobStatusOptional = jobStatusRepository.findById(jobStatusId);
        Optional<User> workerOptional = userRepository.findById(workerId);

        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }

        if (!jobStatusOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobStatus not found");
        }

        if (!workerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Job job = jobOptional.get();
        JobStatus jobStatus = jobStatusOptional.get();
        User worker = workerOptional.get();

        // Create or update the Maintainance entry
        Maintainance maintainance;
        if (maintainanceId != null) {
            Optional<Maintainance> existingMaintainance = maintainanceRepository.findById(maintainanceId);
            if (existingMaintainance.isPresent()) {
                maintainance = existingMaintainance.get();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintainance not found");
            }
        } else {
            maintainance = new Maintainance();
        }

        maintainance.setRequestDateTime(requestDateTime);
        maintainance.setStartDateTime(startDateTime);
        maintainance.setEstimatedEndDateTime(estimatedEndDateTime);
        maintainance.setJobId(job);
        maintainance.setJobStatusId(jobStatus);
        maintainance.setWorkerId(worker);

        maintainanceRepository.save(maintainance);

        return ResponseEntity.ok("Maintainance saved successfully");
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getMaintainanceById(@RequestParam Integer maintainanceId) {
        Optional<Maintainance> maintainanceOptional = maintainanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isPresent()) {
            return ResponseEntity.ok(maintainanceOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintainance not found for ID " + maintainanceId);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMaintainance(@RequestParam Integer maintainanceId) {
        Optional<Maintainance> maintainanceOptional = maintainanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isPresent()) {
            maintainanceRepository.deleteById(maintainanceId);
            return ResponseEntity.ok("Maintainance deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintainance not found for ID " + maintainanceId);
        }
    }
}
