package dreamgarden.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dreamgarden.entities.Company;
import dreamgarden.entities.CompanyHoliday;
import dreamgarden.entities.Service;
import dreamgarden.entities.User;
import dreamgarden.entities.Worker;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.CompanyHolidayRepository;
import dreamgarden.repositories.JobReviewRepository;
import dreamgarden.repositories.ServiceRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.WorkerRepository;
import dreamgarden.request.CreateCompanyHolidayRequest;
import dreamgarden.request.CreateCompanyRequest;
import dreamgarden.request.UpdateCompanyRequest;
import dreamgarden.response.CompanyWithWorkersResponse;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
public class CompanyController {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyHolidayRepository companyHolidayRepository;
    
    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobReviewRepository jobReviewRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;


    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<Company> companies = companyRepository.findAll();
        return ResponseEntity.ok(companies);
    }
    
    @GetMapping("/getById")
    public ResponseEntity<?> getCompanyById(@RequestParam("id") Integer id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok(company.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + id);
        }
    }

    @GetMapping("/getByName")
    public ResponseEntity<?> getCompanyByName(@RequestParam("name") String name) {
        Optional<Company> companies = companyRepository.findByName(name);
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No companies found with name " + name);
        } else {
            return ResponseEntity.ok(companies);
        }
    }
    
    private Double getRating(Company company) {
        Double rating = jobReviewRepository.getRating(company.getCompanyId());
        return rating == null ? 0.0 : rating;
    }
    
    @GetMapping("/getAllCompaniesWithWorkers")
    public ResponseEntity<?> getAllCompaniesWithWorkers() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyWithWorkersResponse> companyWithWorkersList = new ArrayList<>();

        for (Company company : companies) {
            // Get workers associated with this company
            List<Worker> workers = workerRepository.findByCompanyId(company);

            // Create a CompanyWithWorkersResponse object and populate it
            CompanyWithWorkersResponse companyWithWorkersResponse = new CompanyWithWorkersResponse();
            companyWithWorkersResponse.setCompanyId(company.getCompanyId());
            companyWithWorkersResponse.setName(company.getName());
            companyWithWorkersResponse.setAddress(company.getAddress());
            companyWithWorkersResponse.setRating(getRating(company));
            List<CompanyWithWorkersResponse.WorkerResponse> workerResponses = new ArrayList<>();

            for (Worker worker : workers) {
                Optional<User> user = userRepository.findById(worker.getUserId().getUserId());
                if (user.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for worker " + worker);
                }
                CompanyWithWorkersResponse.WorkerResponse workerResponse = companyWithWorkersResponse.createWorkerResponse(user.get().getName(), user.get().getLastname());
                workerResponses.add(workerResponse);
            }
            companyWithWorkersResponse.setWorkers(workerResponses);

            
            // Add the company response to the list
            companyWithWorkersList.add(companyWithWorkersResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(companyWithWorkersList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestBody CreateCompanyRequest request) {
        if (request.getName() == null ||
           request.getAddress() == null ||
           request.getLatitude() == null ||
           request.getLongitude() == null ||
           request.getContactNumber() == null ||
           request.getContactPerson() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Name, Address, Latitude, Longitude, COntactNumer, and ContactPerson are mandatory");
        }
        Optional<Company> companyByName = companyRepository.findByName(request.getName());
        if (companyByName.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Company name must be unique.");
        }
        Company company = new Company();
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setLatitude(request.getLatitude());
        company.setLongitude(request.getLongitude());
        company.setContactNumber(request.getContactNumber());
        company.setContactPerson(request.getContactPerson());
        company = companyRepository.saveAndFlush(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCompany(@RequestBody UpdateCompanyRequest request) {
        if (!request.checkUpdateCompanyRequest()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Neodgovarajući ulazni parametri");
        }
        Optional<Company> companies = companyRepository.findById(request.getCompanyId());
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nema kompanije za ID " + request.getCompanyId());
        }
        Company c = companies.get();
        c.setAddress(request.getAddress());
        c.setName(request.getName());
        c.setLongitude(request.getLongitude());
        c.setLatitude(request.getLatitude());
        c.setContactNumber(request.getContactNumber());
        c.setContactPerson(request.getContactPerson());
        c = companyRepository.saveAndFlush(c);
        return ResponseEntity.ok(c);
    }
    
    @PostMapping("/delete")
    public ResponseEntity<?> deleteCompany(@RequestParam("id") Integer id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return ResponseEntity.ok("Company deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for ID " + id);
        }
    }

    @GetMapping("/holyday/getById")
    public ResponseEntity<?> getCompanyHolidayById(@RequestParam("id") Integer id) {
        Optional<CompanyHoliday> companyHoliday = companyHolidayRepository.findById(id);
        if (companyHoliday.isPresent()) {
            return ResponseEntity.ok(companyHoliday.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company Holiday not found");
        }
    }

    @GetMapping("/holyday/getByCompanyId")
    public ResponseEntity<?> getHolidaysByCompanyId(@RequestParam("companyId") Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + companyId);
        }
        List<CompanyHoliday> holidays = companyHolidayRepository.findByCompanyId(company.get());
        if (holidays.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holidays found for the given company");
        } else {
            return ResponseEntity.ok(holidays);
        }
    }

    @PostMapping("/holyday/create")
    public ResponseEntity<?> createCompanyHoliday(@RequestBody CreateCompanyHolidayRequest request) {
        if (request.getEndDateTime().before(request.getStartDateTime()) ||
            request.getEndDateTime().equals(request.getStartDateTime())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EndDate must be after StartDate");
        }
        Optional<Company> company = companyRepository.findById(request.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + request.getCompanyId());
        }
        CompanyHoliday overlap = checkOverlapHolidays(company.get(), request.getStartDateTime(), request.getEndDateTime());
        if(overlap != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Holiday overlapping with " + overlap);
        }
        CompanyHoliday companyHoliday = new CompanyHoliday();
        companyHoliday.setStartDateTime(request.getStartDateTime());
        companyHoliday.setEndDateTime(request.getEndDateTime());
        companyHoliday.setCompanyId(company.get());
        companyHoliday = companyHolidayRepository.saveAndFlush(companyHoliday);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyHoliday);
    }
    
    private boolean beforeOrEqual(Date date1, Date date2) {
        return (date1.before(date2) || date1.equals(date2));
    }
    private boolean afterOrEqual(Date date1, Date date2) {
        return (date1.after(date2) || date1.equals(date2));
    }
    private CompanyHoliday checkOverlapHolidays(Company company, Date startDate, Date endDate){
        List<CompanyHoliday> holidays = companyHolidayRepository.findByCompanyId(company);
        for (CompanyHoliday holiday : holidays) {
            Date holidayStart = holiday.getStartDateTime();
            Date holidayEnd = holiday.getEndDateTime();
            if ((beforeOrEqual(startDate, holidayStart) && afterOrEqual(endDate, holidayStart)) ||
                (beforeOrEqual(startDate, holidayEnd) && afterOrEqual(endDate, holidayEnd))) {
                return holiday;
            }
        }
        return null;
    }
    

}
