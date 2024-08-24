/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

import java.util.Date;

/**
 *
 * @author vamilutinovic
 */
public class CreateMaintenanceRequest {
    Date startDateTime;
    Integer jobId;

    public CreateMaintenanceRequest() { }

    public CreateMaintenanceRequest(Date startDateTime, Integer jobId) {
        this.startDateTime = startDateTime;
        this.jobId = jobId;
    }

    public boolean checkCreateMaintainanceRequest(){
        return this.jobId!= null &&
               this.startDateTime!= null && 
               this.startDateTime.after(new Date());
    }
    
    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
    
}
