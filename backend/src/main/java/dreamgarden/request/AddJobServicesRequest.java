/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

import java.util.List;

/**
 *
 * @author vamilutinovic
 */
public class AddJobServicesRequest {
    
    Integer jobId;
    List<Integer> serviceIds;

    public AddJobServicesRequest() { }

    public AddJobServicesRequest(Integer jobId, List<Integer> serviceIds) {
        this.jobId = jobId;
        this.serviceIds = serviceIds;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }
    
}
