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
public class CreateMaintainanceRequest {
    Date startDateTime;
    Integer jobId;

    public CreateMaintainanceRequest() { }

    public CreateMaintainanceRequest(Date startDateTime, Integer jobId) {
        this.startDateTime = startDateTime;
        this.jobId = jobId;
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
