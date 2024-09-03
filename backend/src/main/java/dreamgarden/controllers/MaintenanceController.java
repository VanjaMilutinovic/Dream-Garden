package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.CompanyHoliday;
import dreamgarden.entities.Job;
import dreamgarden.entities.JobStatus;
import dreamgarden.entities.Maintenance;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyHolidayRepository;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.MaintenanceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.CreateMaintenanceRequest;
import dreamgarden.request.UpdateEstimatedEndaDateRequest;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing Maintenance-related operations.
 * Provides endpoints to interact with Maintenance entities.
 * 
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private CompanyHolidayRepository companyHolidayRepository;
    
    
    @GetMapping("/getPendingByCompanyId")
    public ResponseEntity<?> getPendingByCompanyId(@RequestParam(name = "companyId", required = true) Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No companies found with ID " + companyId);
        }
        List<Maintenance> maintenances = maintenanceRepository.findByCompanyIdAndJobStatusId(company.get().getCompanyId(), 1);
        if (maintenances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No pending maintenance for company: " + company.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(maintenances);
    }

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
    
    private Date getLastDate(Job job){
        List<Maintenance> maintenances = maintenanceRepository.findByJobId(job);
        if (maintenances.isEmpty()) {
            return job.getEndDateTime();
        }
        Date lastDate = job.getEndDateTime();
        for (Maintenance m: maintenances) {
            if (m.getEstimatedEndDateTime().after(lastDate))
                lastDate = m.getEstimatedEndDateTime();
        }
        return lastDate;
    }
    
    @GetMapping("/isDifferenceLessThanSixMonths")
    public ResponseEntity<?> isDifferenceLessThanSixMonths(@RequestParam(name = "jobId", required = true) Integer jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
        Date date = getLastDate(job.get());
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        // Get the date to compare
        Calendar compareDate = Calendar.getInstance();
        compareDate.setTime(date);
        // Calculate the difference in years and months
        int yearDiff = currentDate.get(Calendar.YEAR) - compareDate.get(Calendar.YEAR);
        int monthDiff = currentDate.get(Calendar.MONTH) - compareDate.get(Calendar.MONTH);
        // Convert the year difference to months and add to the month difference
        int totalMonthDiff = (yearDiff * 12) + monthDiff;
        // Check if the difference is less than 6 months
        boolean result = totalMonthDiff < 6;
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    private boolean areAllWorkersBusyOnDateForCompany(Date jobDate, Company company) {
        // Step 1: Retrieve all workers for the given company
        List<Worker> workers = workerRepository.findByCompanyId(company);
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
    public ResponseEntity<?> saveMaintenance(@RequestBody CreateMaintenanceRequest request) {
        if (!request.checkCreateMaintainanceRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("JobId, WorkerId, and StartDateTime(in future) are required.");
        }
        Optional<Job> job = jobRepository.findById(request.getJobId());
        if (job.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found for id " + request.getJobId());
        }
        CompanyHoliday checkCompanyHoliday = checkCompanyHoliday(request.getStartDateTime(), job.get().getCompanyId());
        if (checkCompanyHoliday != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Company on holiday during: " + checkCompanyHoliday);
        }
        if (areAllWorkersBusyOnDateForCompany(request.getStartDateTime(), job.get().getCompanyId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No workers available at " + request.getStartDateTime());
        }
        if (job.get().getJobStatusId().getJobStatusId() != 5) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Job must be finished. Job in status: " + job.get().getJobStatusId().getStatus());
        }

        List<Maintenance> existingMaintenances = maintenanceRepository.findByJobId(job.get());
        if(checkUnfinishedMaintenances(existingMaintenances)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You have an unfinished maintainance scheduled");
        }
        Maintenance maintainance = new Maintenance();
        maintainance.setJobId(job.get());
        maintainance.setJobStatusId(new JobStatus(1));
        maintainance.setRequestDateTime(new Date());
        maintainance.setStartDateTime(request.getStartDateTime());
        maintainance = maintenanceRepository.saveAndFlush(maintainance);
        return ResponseEntity.ok(maintainance);
    }
    
    private boolean checkUnfinishedMaintenances(List<Maintenance> maintainances){
        Date now = new Date();
        for(Maintenance m: maintainances){
            if ((m.getEstimatedEndDateTime() == null ||m.getEstimatedEndDateTime().after(now)) && 
                (m.getJobStatusId().getJobStatusId()==1 || m.getJobStatusId().getJobStatusId()==3 || m.getJobStatusId().getJobStatusId()==4))
                return true;
        }
        return false;
    }
    
    @PostMapping("/estimateEndDate")
    public ResponseEntity<?> updateEstimatedEndsDate(@RequestBody UpdateEstimatedEndaDateRequest request){
        if (!request.checkUpdateEstimatedEndaDateRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("maintainanceId, workerUserId, and estimatedEndDateTime are required");
        }
        Integer maintainanceId = request.getMaintainanceId();
        Integer workerUserId= request.getWorkerUserId();
        Date estimatedEndDateTime = request.getEstimatedEndDateTime();
        Optional<Maintenance> maintainanceOptional = maintenanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintenance not found for ID " + maintainanceId);
        }
        if (maintainanceOptional.get().getEstimatedEndDateTime() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Maintenance alredy estimated");
        }
        if (maintainanceOptional.get().getJobStatusId().getJobStatusId() != 1) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Only pending maintainance requests can be estimated. Maintenance in status: " + maintainanceOptional.get().getJobStatusId().getStatus());
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
        if (userWorker.get().getUserStatusId().getUserStatusId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Provided user must be a active. Provided: " + userWorker.get().getUserStatusId().getStatus());
        }
        Optional<Worker> worker = workerRepository.findByUserId(userWorker.get());
        Job job = maintainanceOptional.get().getJobId();
        if (!job.getCompanyId().getCompanyId().equals(worker.get().getCompanyId().getCompanyId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Worker(" + worker.get() + ") not employed by company(" + 
                   job.getCompanyId() + ") that did the initial job" + workerUserId);
        }

        Maintenance maintainance = maintainanceOptional.get();
        maintainance.setEstimatedEndDateTime(estimatedEndDateTime);
        maintainance.setJobStatusId(new JobStatus(3));
        maintainance.setWorkerId(userWorker.get());
        maintainance = maintenanceRepository.saveAndFlush(maintainance);
        return ResponseEntity.ok(maintainance);
    }
    
    @PostMapping("/reject")
    public ResponseEntity<?> reject(@RequestParam(name = "maintainanceId", required = true) Integer maintainanceId) {
        Optional<Maintenance> maintainance = maintenanceRepository.findById(maintainanceId);
        if (maintainance.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintenance not found for ID " + maintainanceId);
        }
        Optional<User> userWorker = userRepository.findById(maintainance.get().getWorkerId().getUserId());
        if (userWorker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found for userID " + maintainance.get().getWorkerId().getUserId());
        }
        if (userWorker.get().getUserTypeId().getUserTypeId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Provided user must be a decorator. Provided: " + userWorker.get().getUserTypeId().getName());
        }
        if (userWorker.get().getUserStatusId().getUserStatusId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Provided user must be a active. Provided: " + userWorker.get().getUserStatusId().getStatus());
        }
        maintainance.get().setJobStatusId(new JobStatus(2));
        Maintenance savedMaintenance = maintenanceRepository.saveAndFlush(maintainance.get());
        return ResponseEntity.status(HttpStatus.OK).body(savedMaintenance);
   }
    
    @GetMapping("/findById")
    public ResponseEntity<?> getMaintenanceById(@RequestParam(name = "maintainanceId", required = true) Integer maintainanceId) {
        Optional<Maintenance> maintainanceOptional = maintenanceRepository.findById(maintainanceId);
        if (maintainanceOptional.isPresent()) {
            return ResponseEntity.ok(maintainanceOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maintenance not found for ID " + maintainanceId);
        }
    }
    
}
