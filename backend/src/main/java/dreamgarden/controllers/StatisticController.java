/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.controllers;

import dreamgarden.entities.Company;
import dreamgarden.entities.User;
import dreamgarden.repositories.CompanyRepository;
import dreamgarden.repositories.JobRepository;
import dreamgarden.repositories.JobStatusRepository;
import dreamgarden.repositories.UserRepository;
import dreamgarden.repositories.UserTypeRepository;
import dreamgarden.response.CountingStatisticResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vamilutinovic
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobStatusRepository jobStatusRepository;
    
    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @Autowired
    private UserRepository userRepository;   
    
    @Autowired
    private CompanyRepository companyRepository;
    

    @GetMapping("/job/getByStatus")
    public ResponseEntity<?> getJobByStatus() {
        List<Object[]> statistic = jobStatusRepository.findStatusStatistic();
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unexpected error occured");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }
    
    @GetMapping("/user/getByType")
    public ResponseEntity<?> getUserByType() {
        List<Object[]> statistic = userTypeRepository.findTypeStatistic();
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unexpected error occured");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }
    
    @GetMapping("/job/getByRequestTime")
    public ResponseEntity<?> getJobByRequestTime() {
        List<Object[]> statistic = jobRepository.findJobCountsByRequestTime();
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unexpected error occured");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }
    
    @GetMapping("/job/getByWorker")
    public ResponseEntity<?> getJobByWorker(@RequestParam(name = "workerId", required = true) Integer workerId) {
        Optional<User> user = userRepository.findById(workerId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID " + workerId);
        }
        if (user.get().getUserTypeId().getUserTypeId() != 2) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User must be a decorator instead of " + user.get().getUserTypeId().getName());
        }
        List<Object[]> statistic = jobRepository.findJobCountByWorkerAndMonth(workerId);
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker has no jobs");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }
    
    @GetMapping("/job/getByCompany")
    public ResponseEntity<?> getJobByCompany(@RequestParam(name = "companyId", required = true) Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id " + companyId);
        }
        List<Object[]> statistic = jobRepository.findJobCountsByCompany(companyId);
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company doesn't have jobs");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }
    
    @GetMapping("/job/getByDay")
    public ResponseEntity<?> getJobByDay() {
        List<Object[]> statistic = jobRepository.findJobCountsByDay();
        if (statistic.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unexpected error occured");
        }
        return ResponseEntity.status(HttpStatus.OK).body(statistic.stream()
                      .map(result -> new CountingStatisticResponse((String) result[0], ((Number) result[1]).intValue()))
                      .collect(Collectors.toList()));
    }

}
