package dreamgarden.controllers;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Maintainance;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.MaintainanceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.CreateMaintainanceRequest;
import dreamgarden.request.UpdateEstimatedEndaDateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveMaintainance(@RequestBody CreateMaintainanceRequest request) {
        if (!checkCreateMaintainanceRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("JobId, WorkerId, and StartDateTime(in future) are required.");
        }
        Optional<Job> job = jobRepository.findById(request.getJobId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + request.getJobId());
        }
        if (job.get().getJobStatusId().getJobStatusId() != 5) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Job must be finished. Job in status: " + job.get().getJobStatusId().getStatus());
        }

        List<Maintainance> existingMaintainances = maintainanceRepository.findByJobId(job.get());
        if(checkUnfinishedMaintainances(existingMaintainances)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You have an unfinished maintainance scheduled");
        }
        Maintainance maintainance = new Maintainance();
        maintainance.setJobId(job.get());
        maintainance.setJobStatusId(new JobStatus(1));
        maintainance.setRequestDateTime(new Date());
        maintainance.setStartDateTime(request.getStartDateTime());
        maintainance = maintainanceRepository.saveAndFlush(maintainance);
        return ResponseEntity.ok(maintainance);
    }

    private boolean checkCreateMaintainanceRequest(CreateMaintainanceRequest request){
        return request.getJobId() != null &&
               request.getStartDateTime() != null && 
               request.getStartDateTime().after(new Date());
    }
    
    private boolean checkUnfinishedMaintainances(List<Maintainance> maintainances){
        Date now = new Date();
        for(Maintainance m: maintainances){
            if ((m.getEstimatedEndDateTime() == null ||m.getEstimatedEndDateTime().after(now)) && 
                (m.getJobStatusId().getJobStatusId()==1 || m.getJobStatusId().getJobStatusId()==3 || m.getJobStatusId().getJobStatusId()==4))
                return true;
        }
        return false;
    }
    
    @PostMapping("/estimateEndDate")
    public ResponseEntity<?> updateEstimatedEndaDate(@RequestBody UpdateEstimatedEndaDateRequest request){
        if (!checkUpdateEstimatedEndaDateRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("maintainanceId, workerUserId, and estimatedEndDateTime are required");
        }
        Integer maintainanceId = request.getMaintainanceId();
        Integer workerUserId= request.getWorkerUserId();
        Date estimatedEndDateTime = request.getEstimatedEndDateTime();
        Optional<Maintainance> maintainanceOptional = maintainanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintainance not found for ID " + maintainanceId);
        }
        if (maintainanceOptional.get().getEstimatedEndDateTime() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Maintainance alredy estimated");
        }
        if (maintainanceOptional.get().getJobStatusId().getJobStatusId() != 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Only pending maintainance requests can be estimated. Maintainance in status: " + maintainanceOptional.get().getJobStatusId().getStatus());
        }
        if(maintainanceOptional.get().getStartDateTime().after(estimatedEndDateTime)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("EstimatedEnd can't be before StartDateTime");
        }
        Optional<User> userWorker = userRepository.findById(workerUserId);
        if (userWorker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found for userID " + workerUserId);
        }
        if (userWorker.get().getUserTypeId().getUserTypeId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Provided user must be a decorator. Provided: " + userWorker.get().getUserTypeId().getName());
        }
        Optional<Worker> worker = workerRepository.findByUserId(userWorker.get());
        Job job = maintainanceOptional.get().getJobId();
        if (!job.getCompanyId().getCompanyId().equals(worker.get().getCompanyId().getCompanyId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Worker(" + worker.get() + ") not employed by company(" + 
                   job.getCompanyId() + ") that did the initial job" + workerUserId);
        }

        Maintainance maintainance = maintainanceOptional.get();
        maintainance.setEstimatedEndDateTime(estimatedEndDateTime);
        maintainance.setJobStatusId(new JobStatus(3));
        maintainance.setWorkerId(userWorker.get());
        maintainance = maintainanceRepository.saveAndFlush(maintainance);
        return ResponseEntity.ok(maintainance);
    }
    
    private boolean checkUpdateEstimatedEndaDateRequest(UpdateEstimatedEndaDateRequest request){
        return request.getEstimatedEndDateTime() != null &&
               request.getMaintainanceId() != null &&
               request.getWorkerUserId() != null;
    }
    
    @GetMapping("/findById")
    public ResponseEntity<?> getMaintainanceById(@RequestParam(name = "maintainanceId", required = true) Integer maintainanceId) {
        Optional<Maintainance> maintainanceOptional = maintainanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isPresent()) {
            return ResponseEntity.ok(maintainanceOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintainance not found for ID " + maintainanceId);
        }
    }
    
}
