package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.GardenType;
import dreamgarden.entities.Job;
import dreamgarden.entities.JobPhoto;
import dreamgarden.entities.JobReview;
import dreamgarden.entities.JobService;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Photo;
import dreamgarden.entities.PrivateGarden;
import dreamgarden.entities.RestaurantGarden;
import dreamgarden.entities.Service;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.GardenTypeRepository;
import dreamgarden.repositories.JobPhotoRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobReviewRepository;
import dreamgarden.repositories.JobServiceRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.ServiceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.AddJobServicesRequest;
import dreamgarden.request.CreateJobRequest;
import dreamgarden.request.CreateJobReviewRequest;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobPhotoRepository jobPhotoRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private JobReviewRepository jobReviewRepository;
    
    @Autowired
    private JobStatusRepository jobStatusRepository;
    
    @Autowired
    private JobServiceRepository jobServiceRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private GardenTypeRepository gardenTypeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request) {
        if (request.checkCreateJobRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("CreateJobRequest not adequate");
        }
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + request.getUserId());
        }
        if(user.get().getUserTypeId().getUserTypeId()!= 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User must be of type owner. Given: " + user.get().getUserTypeId().getName());
        }
        Optional<User> worker = userRepository.findById(request.getWorkerId());
        if(worker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + request.getWorkerId());
        }
        if(worker.get().getUserTypeId().getUserTypeId()!= 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Worker must be of type decorator. Given: " + worker.get().getUserTypeId().getName());
        }
        Optional<Worker> workerObj = workerRepository.findByUserId(worker.get());
         if(workerObj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not employed for userId " + request.getWorkerId());
        }
        Optional<Company> company = companyRepository.findById(request.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + request.getCompanyId());
        }
        if(workerObj.get().getCompanyId().getCompanyId() != request.getCompanyId()) {
             return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Worker not employed by given company. Worker company: "
                                                                           + workerObj.get().getCompanyId() + ", company: " + company.get());
        }
        Optional<GardenType> gardenType = gardenTypeRepository.findById(request.getGardenTypeId());
        if(gardenType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GardenType not found for ID " + request.getGardenTypeId());
        }
        if(request.getStartDateTime().before(new Date())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("StartDate must be in the future. Given: " + request.getStartDateTime());
        }
        
        Job job = new Job();
        job.setCompanyId(company.get());
        job.setDescription(request.getDescription());
        job.setGardenSize(request.getGardenSize());
        job.setGardenTypeId(gardenType.get());
        job.setJobStatusId(new JobStatus(1));
        job.setRequestDateTime(new Date());
        job.setStartDateTime(request.getStartDateTime());
        job.setUserId(user.get());
        job.setWorkerId(worker.get());
        job = jobRepository.saveAndFlush(job);
        
        switch(job.getGardenTypeId().getGardenTypeId()){
            case 1 -> {
                PrivateGarden privateGarden = new PrivateGarden(job.getJobId(), request.getPrivateGarden());
                job.setPrivateGarden(privateGarden);
            }
            case 2 -> {
                RestaurantGarden restaurantGarden = new RestaurantGarden(job.getJobId(), request.getRestaurantGarden());
                job.setRestaurantGarden(restaurantGarden);
            }
            default -> {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("GardenType ID " + request.getGardenTypeId()+" is currenty unsupported");
            }
        }
        
        // Job will recursively save PrivateGarden and/or RestaurantGarden objects
        job = jobRepository.saveAndFlush(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getJobById(@RequestParam(name = "jobId", required = true) Integer jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(jobOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestParam(name = "jobId", required = true) Integer jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            jobRepository.delete(jobOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Job deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
    }
    
    @PostMapping("/photo/add")
    @Transactional
    public ResponseEntity<?> addJobPhoto(@RequestParam(name = "jobId", required = true) Integer jobId, 
                                         @RequestParam(name = "photoPath", required = true) String photoPath) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
        Optional<Photo> photoByPath = photoRepository.findByPath(photoPath);
        if(photoByPath.isPresent()) {
            //Workers need to upload new photos for each job!
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Photo with provided path already exists. Path: " + photoPath);
        }
        
        Photo photo = new Photo();
        photo.setPath(photoPath);
        photo = photoRepository.saveAndFlush(photo);

        JobPhoto jobPhoto = new JobPhoto();
        jobPhoto.setJobId(jobOptional.get());
        jobPhoto.setPhotoId(photo);
        jobPhoto = jobPhotoRepository.save(jobPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPhoto);
    }   
    
    @PostMapping("/review/add")
    public ResponseEntity<?> addJobReview(@RequestBody CreateJobReviewRequest request) {
        if (!request.checkCreateJobReviewRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Comment, Grade, and JobId required!");
        }
        Optional<Job> job = jobRepository.findById(request.getJobId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + request.getJobId());
        }
        if (job.get().getJobStatusId().getJobStatusId() != 5) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Job must be finished! Job status: " + job.get().getJobStatusId().getStatus());
        }
        Optional<JobReview> jobReviewByJobId = jobReviewRepository.findByJobId(job.get());
        if (jobReviewByJobId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Job already reviewed: " + jobReviewByJobId);
        }
        
        JobReview jobReview = new JobReview();
        jobReview.setJobId(job.get());
        jobReview.setComment(request.getComment());
        jobReview.setGrade(request.getGrade());
        jobReview = jobReviewRepository.saveAndFlush(jobReview);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(jobReview);
    }
    
    @PostMapping("/status/update")
    public ResponseEntity<?> updateJobStatus(@RequestParam(name = "jobId", required = true) Integer jobId, 
                                             @RequestParam(name = "jobStatusId", required = true) Integer jobStatusId,
                                             @RequestParam(name = "rejectedDescription", required = false) String rejectedDescription) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
        Optional<JobStatus> jobStatus = jobStatusRepository.findById(jobStatusId);
        if (jobStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobStatus not found for id " + jobStatusId);
        }
        if (jobStatus.get().getStatus().equals(2) && rejectedDescription.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Declination description is mandatory when rejecting a job");
        }

        job.get().setJobStatusId(jobStatus.get());
        if (jobStatus.get().getStatus().equals(2)) 
            job.get().setRejectedDescription(rejectedDescription);
        Job savedJob = jobRepository.saveAndFlush(job.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedJob);
    }
    
    @PostMapping("/service/addServices")
    public ResponseEntity<?> addServicesToJob(@RequestBody AddJobServicesRequest request) {
        if (request.getJobId() == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("JobId required");
        }
        Optional<Job> job = jobRepository.findById(request.getJobId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + request.getJobId());
        }
        if (request.getServiceIds() == null || request.getServiceIds().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("At least one service required");
        }
        
        List<JobService> jobServiceList = new ArrayList<>();
        for (Integer serviceId : request.getServiceIds()) {
            Optional<Service> service = serviceRepository.findById(serviceId);
            if (service.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service with ID " + serviceId + " not found");
            } 
            if (!service.get().getCompanyId().getCompanyId().equals(job.get().getCompanyId().getCompanyId())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Service with ID " + serviceId + 
                        " not provided by contracted company " + service.get().getCompanyId());
            }
            JobService jobService = new JobService();
            jobService.setJobId(job.get());
            jobService.setServiceId(service.get());
            jobServiceList.add(jobService);
        }
        jobServiceList = jobServiceRepository.saveAllAndFlush(jobServiceList);
        return ResponseEntity.status(HttpStatus.OK).body(jobServiceList);
    }
}
