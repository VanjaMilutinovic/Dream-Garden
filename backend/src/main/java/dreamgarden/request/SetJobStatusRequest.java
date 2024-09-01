/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

/**
 *
 * @author vamilutinovic
 */
public class SetJobStatusRequest {
    private Integer jobId;
    private Integer statusId;
    private String rejectionDescription;
    private Integer userWorkerId;
    
    public boolean checkSetJobStatusRequest(){
        return jobId != null &&
               statusId != null &&
               ((statusId == 2 && rejectionDescription != null) || statusId != 2) &&
               userWorkerId != null;
    }

    public SetJobStatusRequest() { }

    public SetJobStatusRequest(Integer jobId, Integer statusId, String rejectionDescription, Integer userWorkerId) {
        this.jobId = jobId;
        this.statusId = statusId;
        this.rejectionDescription = rejectionDescription;
        this.userWorkerId = userWorkerId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getRejectionDescription() {
        return rejectionDescription;
    }

    public void setRejectionDescription(String rejectionDescription) {
        this.rejectionDescription = rejectionDescription;
    }

    public Integer getUserWorkerId() {
        return userWorkerId;
    }

    public void setUserWorkerId(Integer userWorkerId) {
        this.userWorkerId = userWorkerId;
    }
    
}
