package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.CompanyHoliday;
import dreamgarden.entities.GardenType;
import dreamgarden.entities.Job;
import dreamgarden.entities.JobPhoto;
import dreamgarden.entities.JobReview;
import dreamgarden.entities.JobService;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Maintenance;
import dreamgarden.entities.Photo;
import dreamgarden.entities.PrivateGarden;
import dreamgarden.entities.RestaurantGarden;
import dreamgarden.entities.Service;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyHolidayRepository;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.GardenTypeRepository;
import dreamgarden.repositories.JobPhotoRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobReviewRepository;
import dreamgarden.repositories.JobServiceRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.MaintenanceRepository;
import dreamgarden.repositories.PhotoRepository;
import dreamgarden.repositories.ServiceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.AddJobServicesRequest;
import dreamgarden.request.CreateJobRequest;
import dreamgarden.request.CreateJobReviewRequest;
import dreamgarden.request.SetJobStatusRequest;
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
    
    @Autowired
    private CompanyHolidayRepository companyHolidayRepository;
    
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    
    private CompanyHoliday checkCompanyHoliday(Date date, Company company) {
        List<CompanyHoliday> companyHolidays = companyHolidayRepository.findByCompanyId(company);
        if (companyHolidays.isEmpty()) {
            return null;
        }
        for (CompanyHoliday holiday: companyHolidays) {
            if (holiday.getStartDateTime().before(date) && holiday.getEndDateTime().after(date))
                return holiday;
        }
        return null;
    }
    
    private boolean areAllWorkersBusyOnDateForCompany(Date jobDate, Company company) {
        // Step 1: Retrieve all workers for the given company
        List<Worker> allworkers = workerRepository.findByCompanyId(company);
        List<Worker>workers = new ArrayList<>();
        for (Worker w: allworkers)
            if (w.getUserId().getUserStatusId().getUserStatusId()==2)
                workers.add(w);
        // Step 2: Check availability of each worker
        for (Worker worker : workers) {
            List<Job> jobs = jobRepository.findByWorkerIdAndCompanyIdAndDateRange(worker.getUserId().getUserId(), company.getCompanyId(), jobDate);
            List<Maintenance> maintenances = maintenanceRepository.findByWorkerIdAndCompanyIdAndDateRange(worker.getUserId().getUserId(), company.getCompanyId(), jobDate);
            // If the worker has no jobs or maintenance tasks scheduled during the jobDate, they are available
            if (jobs.isEmpty() && maintenances.isEmpty()) {
                return false; // At least one worker is available
            }
        }
        // Step 3: If all workers are busy, return true
        return true;
    }
    
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request) {
        if (!request.checkCreateJobRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Neispravni ulazni parametri");
        }
        Optional<User> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + request.getUserId());
        }
        if(user.get().getUserTypeId().getUserTypeId()!= 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User must be of type owner. Given: " + user.get().getUserTypeId().getName());
        }
        Optional<Company> company = companyRepository.findById(request.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + request.getCompanyId());
        }
        CompanyHoliday checkCompanyHoliday = checkCompanyHoliday(request.getStartDateTime(), company.get());
        if (checkCompanyHoliday != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Company on holiday during: " + checkCompanyHoliday);
        }
        if (areAllWorkersBusyOnDateForCompany(request.getStartDateTime(), company.get())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No workers available at " + request.getStartDateTime());
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
        job.setCanvas(request.getCanvas());
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
    
    @GetMapping("/getByStatusAndUser")
    public ResponseEntity<?> getByStatusAndUser(@RequestParam(name = "userId", required = true) Integer userId,
                                                @RequestParam(name = "jobStatusId", required = true) Integer jobStatusId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with ID " + userId);
        }
        Optional<JobStatus> jobStatus = jobStatusRepository.findById(jobStatusId);
        if (jobStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No JobStatus found with ID " + jobStatusId);
        }
        List<Job> jobs = jobRepository.findByUserIdAndJobStatusId(user.get(), jobStatus.get());
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs for user: " + user.get() + " in status " + jobStatus.get().getStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }
    
    @GetMapping("/getByStatusAndWorker")
    public ResponseEntity<?> getByStatusAndWorker(@RequestParam(name = "userId", required = true) Integer userId,
                                                @RequestParam(name = "jobStatusId", required = true) Integer jobStatusId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema korsnika za ID " + userId);
        }
        Optional<Worker> worker = workerRepository.findByUserId(user.get());
        if (worker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema radnika za  userID " + userId);
        }
        Optional<JobStatus> jobStatus = jobStatusRepository.findById(jobStatusId);
        if (jobStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobStatus nije nađen za ID " + jobStatusId);
        }
        List<Job> jobs = jobRepository.findByWorkerIdAndJobStatusId(user.get(), jobStatus.get());
        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema poslova za radnika: " + user.get() + " u statusu " + jobStatus.get().getStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }


    @GetMapping("/getPendingByCompanyId")
    public ResponseEntity<?> getPendingByCompanyId(@RequestParam(name = "companyId", required = true) Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ne postoji kompanija za ID " + companyId);
        }
        List<Job> maintenances = jobRepository.findByCompanyIdAndJobStatusId(company.get().getCompanyId(), 1);
        if (maintenances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema neobrađenih poslova: " + company.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(maintenances);
    }

    
    @PostMapping("worker/setStatus")
    public ResponseEntity<?> setStatusForWorker(@RequestBody SetJobStatusRequest request){
        if (!request.checkSetJobStatusRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("SetJobStatusRequest not adequate: " + request);
        }
        Optional<User> worker = userRepository.findById(request.getUserWorkerId());
        if (worker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with ID " + request.getUserWorkerId());
        }
        Optional<Job> job = jobRepository.findById(request.getJobId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs found with ID " + request.getJobId());
        }
        Optional<JobStatus> jobStatus = jobStatusRepository.findById(request.getStatusId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No job status found with ID " + request.getJobId());
        }
        if (jobStatus.get().getJobStatusId() == 5 && !job.get().getWorkerId().equals(worker.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wokrer not employeed at this job");
        }
        if ((jobStatus.get().getJobStatusId() == 2 || jobStatus.get().getJobStatusId() == 3) 
                && worker.get().getUserStatusId().getUserStatusId() == 4) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Worker must be active to set status: " + jobStatus.get().getStatus() + 
                    ", Worker in status: " + worker.get().getUserStatusId().getStatus() + " instead");
        }
        
        switch (jobStatus.get().getJobStatusId()) {
            case 3 ->  {
                job.get().setWorkerId(worker.get());
            }
            case 2 ->  {
                job.get().setRejectedDescription(request.getRejectionDescription());
            }
            case 5 ->  {
                job.get().setEndDateTime(new Date());
            }
            case 1, 4, 6 -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wokrer cannot set status " + jobStatus.get().getStatus());
            }
            default -> {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Status not implemented yet");
            }
        }
        job.get().setJobStatusId(jobStatus.get());
        Job savedJob = jobRepository.saveAndFlush(job.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedJob);
    }
    
    private boolean isDifferenceLessThanOneDay(Date date) {
        // Get the difference in milliseconds
        long diffInMillis = Math.abs((new Date()).getTime() - date.getTime());
        // Convert milliseconds to days
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
        // Check if the difference is greater than one day
        return diffInDays < 1;
    }
    
    @PostMapping("owner/cancel")
    public ResponseEntity<?> cancel(@RequestParam(name = "userId", required = true) Integer userId,
                                    @RequestParam(name = "jobId", required = true) Integer jobId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with ID " + userId);
        }
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs found with ID " + jobId);
        }
        if(isDifferenceLessThanOneDay(job.get().getStartDateTime())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Cannot be cancelled less than 24 hours before job");
        }
        job.get().setJobStatusId(new JobStatus(6)); //cancelled
        job.get().setWorkerId(null); //free worker
        Job savedJob = jobRepository.saveAndFlush(job.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedJob);  
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
        //todo popraviti sranja sa slikom i odje
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
        Optional<Photo> photoByPath = photoRepository.findByBase64(photoPath);
        if(photoByPath.isPresent()) {
            //Workers need to upload new photos for each job!
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Photo with provided path already exists. Path: " + photoPath);
        }
        
        Photo photo = new Photo();
        photo.setBase64(photoPath);
        photo = photoRepository.saveAndFlush(photo);

        JobPhoto jobPhoto = new JobPhoto();
        jobPhoto.setJobId(jobOptional.get());
        jobPhoto.setPhotoId(photo);
        jobPhoto = jobPhotoRepository.save(jobPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPhoto);
    }   
    
    @GetMapping("/photo/getTopK")
    public ResponseEntity<?> getTopKJobPhotos(@RequestParam(name = "k", required = true) Integer k){
        if (k<3)
            k =3;
        List<String> paths = photoRepository.findTopKByPhotoPath(k);
        return ResponseEntity.status(HttpStatus.OK).body(paths);
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
        Optional<List<JobReview>> jobReviewByJobId = jobReviewRepository.findByJobId(job.get());
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
    
    @GetMapping("/review/getByCompanyId")
    public ResponseEntity<?> getReviewsForCompany(@RequestParam(name = "companyId", required = true) Integer companyId){
        Optional<Company> companies = companyRepository.findById(companyId);
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema kompanije za ID " + companyId);
        }
        List<Job> jobs = jobRepository.findByCompanyId(companies.get());
        List<JobReview> reviews = new ArrayList<>();
        for (Job j: jobs) {
            Optional<List<JobReview>> rev = jobReviewRepository.findByJobId(j);
            if (rev.isPresent())
                reviews.addAll(rev.get());
        }
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/review/getByJob")
    public ResponseEntity<?> getReviewByJob(@RequestParam(name = "jobId", required = true) Integer jobId){
         Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
        Optional<List<JobReview>> review = jobReviewRepository.findByJobId(jobOptional.get());
        if (review.isEmpty())
            review = null;
         return ResponseEntity.status(HttpStatus.OK).body(review);
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
    
    @GetMapping("/service/getByJob")
        public ResponseEntity<?> geServiceByJob(@RequestParam(name = "jobId", required = true) Integer jobId){
         Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + jobId);
        }
        List<Service> services = jobServiceRepository.findByJobId(jobOptional.get());
         return ResponseEntity.status(HttpStatus.OK).body(services);
     }
}
