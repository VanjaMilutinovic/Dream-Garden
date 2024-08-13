package dreamgarden.controllers;

import dreamgarden.entities.Job;
import dreamgarden.entities.JobPhoto;
import dreamgarden.entities.JobReview;
import dreamgarden.entities.JobService;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Photo;
import dreamgarden.entities.Service;
import dreamgarden.repositories.JobPhotoRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobReviewRepository;
import dreamgarden.repositories.JobServiceRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.ServiceRepository;
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

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobRepository.save(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
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
