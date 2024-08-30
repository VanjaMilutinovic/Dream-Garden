/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.response;

import java.util.List;

/**
 *
 * @author vamilutinovic
 */
public class CompanyWithWorkersResponse {
    
    public class WorkerResponse {
        public String name;
        public String lastname;
        
        public WorkerResponse(String name, String lastname){ 
            this.name = name;
            this.lastname = lastname;
        }
    }
    
    private Integer companyId;
    private String name;
    private String address;
    private List<WorkerResponse> workers;
    private Double rating;

    public CompanyWithWorkersResponse() { }

    public CompanyWithWorkersResponse(Integer companyId, String name, String address, List<WorkerResponse> workers, Double rating) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.workers = workers;
        this.rating = rating;
    }
    
    public WorkerResponse createWorkerResponse(String name, String lastname){
        return new WorkerResponse(name, lastname);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<WorkerResponse> getWorkers() {
        return workers;
    }

    public void setWorkers(List<WorkerResponse> workers) {
        this.workers = workers;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    
}
