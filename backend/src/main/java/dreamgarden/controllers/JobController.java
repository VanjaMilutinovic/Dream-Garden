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
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.GardenTypeRepository;
import dreamgarden.repositories.JobPhotoRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobReviewRepository;
import dreamgarden.repositories.JobServiceRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.PrivateGardenRepository;
import dreamgarden.repositories.RestaurantGardenRepository;
import dreamgarden.repositories.ServiceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.request.CreateJobRequest;
import dreamgarden.request.CreatePrivateGardenRequest;
import dreamgarden.request.CreateRestaurantGardenRequest;
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
    private PrivateGardenRepository privateGardenRepository;
    
    @Autowired
    private RestaurantGardenRepository restaurantGardenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + request.getUserId());
        }
        Optional<User> worker = userRepository.findById(request.getWorkerId());
        if(worker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + request.getWorkerId());
        }
        Optional<Company> company = companyRepository.findById(request.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + request.getCompanyId());
        }
        Optional<GardenType> gardenType = gardenTypeRepository.findById(request.getGardenTypeId());
        if(gardenType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GardenType not found for ID " + request.getGardenTypeId());
        }
        if(request.getStartDateTime().before(new Date())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("StartDate must be in the future. Given: " + request.getStartDateTime());
        }
        
        Optional<JobStatus> jobStatusPending = jobStatusRepository.findById(1);
        Job job = new Job();
        job.setCompanyId(company.get());
        job.setDescription(request.getDescription());
        job.setGardenSize(request.getGardenSize());
        job.setGardenTypeId(gardenType.get());
        job.setJobStatusId(jobStatusPending.get());
        job.setRequestDateTime(new Date());
        job.setStartDateTime(request.getStartDateTime());
        job.setUserId(user.get());
        job.setWorkerId(worker.get());
        job = jobRepository.saveAndFlush(job);
        
        if (job.getGardenTypeId().getGardenTypeId() == 1) {
            PrivateGarden garden = new PrivateGarden(job.getJobId(), request.getPrivateGarden());
            job.setPrivateGarden(garden);
        }
        else if (job.getGardenTypeId().getGardenTypeId() == 2) {
            RestaurantGarden garden = new RestaurantGarden(job.getJobId(), request.getRestaurantGarden());
            job.setRestaurantGarden(garden);
        }
        /*
            Није потребно одвојено чувати PrivateGarden и RestaurantGarden 
        објекте зато што су дио Job објекта. Кад се позове следећа линија, она 
        ће рекурзивно да сачува објекте који сачинјавају објекат Job job.
        */
        job = jobRepository.saveAndFlush(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getJobById(@RequestParam Integer jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(jobOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateJob(@RequestBody Job updatedJob) {
        Integer jobId = updatedJob.getJobId();
        if (jobId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job ID is required");
        }

        Optional<Job> existingJobOptional = jobRepository.findById(jobId);
        if (existingJobOptional.isPresent()) {
            Job existingJob = existingJobOptional.get();
            updatedJob.setJobId(existingJob.getJobId());
            Job savedJob = jobRepository.save(updatedJob);
            return ResponseEntity.status(HttpStatus.OK).body(savedJob);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJob(@RequestParam Integer jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            jobRepository.delete(jobOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Job deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
    }
    
    @PostMapping("/photo/add")
    public ResponseEntity<?> addJobPhoto(@RequestParam("job_id") Integer jobId, @RequestParam("photo_id") Integer photoId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }

        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if (!photoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found");
        }

        JobPhoto jobPhoto = new JobPhoto();
        jobPhoto.setJobId(jobOptional.get());
        jobPhoto.setPhotoId(photoOptional.get());

        JobPhoto savedJobPhoto = jobPhotoRepository.save(jobPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJobPhoto);
    }
    
    @PostMapping("/review/add")
    public ResponseEntity<String> addJobReview(@RequestParam Integer jobId, @RequestBody JobReview review) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        
        if (optionalJob.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }
        
        Job job = optionalJob.get();
        review.setJobId(job);
        
        jobReviewRepository.save(review);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Job review added successfully");
    }
    
    @PostMapping("/status/update")
    public ResponseEntity<String> updateJobStatus(@RequestParam Integer jobId, @RequestParam Integer jobStatusId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        Optional<JobStatus> optionalJobStatus = jobStatusRepository.findById(jobStatusId);

        if (optionalJob.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }

        if (optionalJobStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobStatus not found");
        }

        Job job = optionalJob.get();
        JobStatus jobStatus = optionalJobStatus.get();
        job.setJobStatusId(jobStatus);
        
        jobRepository.save(job);
        
        return ResponseEntity.status(HttpStatus.OK).body("Job status updated successfully");
    }
    
    @PostMapping("/service/addServices")
    public ResponseEntity<String> addServicesToJob(@RequestParam Integer jobId, @RequestBody List<Integer> serviceIds) {
        // Find the job by ID
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
        }

        Job job = jobOptional.get();

        // Add each service to the job
        for (Integer serviceId : serviceIds) {
            Optional<Service> serviceOptional = serviceRepository.findById(serviceId);
            if (serviceOptional.isPresent()) {
                Service service = serviceOptional.get();
                
                JobService jobService = new JobService();
                jobService.setJobId(job);
                jobService.setServiceId(service);

                jobServiceRepository.save(jobService);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service with ID " + serviceId + " not found");
            }
        }

        return ResponseEntity.ok("Services added to job successfully");
    }
}
