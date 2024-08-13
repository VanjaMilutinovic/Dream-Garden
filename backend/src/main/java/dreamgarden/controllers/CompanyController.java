package dreamgarden.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dreamgarden.entities.Company;
import dreamgarden.entities.CompanyHoliday;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.CompanyHolidayRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
//@CrossOrigin(origins = "http://localhost:4200/")
public class CompanyController {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyHolidayRepository companyHolidayRepository;


    @GetMapping("/getById")
    public ResponseEntity<?> getCompanyById(@RequestParam("id") Integer id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return ResponseEntity.ok(company.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
    }

    @GetMapping("/getByName")
    public ResponseEntity<?> getCompanyByName(@RequestParam("name") String name) {
        List<Company> companies = companyRepository.findByName(name);
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No companies found with the given name");
        } else {
            return ResponseEntity.ok(companies);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        Company savedCompany = companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCompany(@RequestParam("id") Integer id, @RequestBody Company updatedCompany) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            updatedCompany.setCompanyId(id);
            companyRepository.save(updatedCompany);
            return ResponseEntity.ok(updatedCompany);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCompany(@RequestParam("id") Integer id) {
        Optional<Company> existingCompany = companyRepository.findById(id);
        if (existingCompany.isPresent()) {
            companyRepository.delete(existingCompany.get());
            return ResponseEntity.ok("Company deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
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
        if (!company.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
        List<CompanyHoliday> holidays = companyHolidayRepository.findByCompanyId(company.get());
        if (holidays.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holidays found for the given company");
        } else {
            return ResponseEntity.ok(holidays);
        }
    }

    @PostMapping("/holyday/create")
    public ResponseEntity<?> createCompanyHoliday(@RequestBody CompanyHoliday companyHoliday) {
        CompanyHoliday savedHoliday = companyHolidayRepository.save(companyHoliday);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHoliday);
    }

    @PostMapping("/holyday/update")
    public ResponseEntity<?> updateCompanyHoliday(@RequestParam("id") Integer id, @RequestBody CompanyHoliday updatedHoliday) {
        Optional<CompanyHoliday> existingHoliday = companyHolidayRepository.findById(id);
        if (existingHoliday.isPresent()) {
            updatedHoliday.setCompanyHolidayId(id);
            companyHolidayRepository.save(updatedHoliday);
            return ResponseEntity.ok(updatedHoliday);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company Holiday not found");
        }
    }

    @PostMapping("/holyday/delete")
    public ResponseEntity<?> deleteCompanyHoliday(@RequestParam("id") Integer id) {
        Optional<CompanyHoliday> existingHoliday = companyHolidayRepository.findById(id);
        if (existingHoliday.isPresent()) {
            companyHolidayRepository.delete(existingHoliday.get());
            return ResponseEntity.ok("Company Holiday deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company Holiday not found");
        }
    }
}
