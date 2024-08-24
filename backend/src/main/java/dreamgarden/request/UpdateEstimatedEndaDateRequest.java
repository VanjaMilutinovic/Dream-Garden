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
public class UpdateEstimatedEndaDateRequest {
    Integer maintainanceId;
    Integer workerUserId;
    Date estimatedEndDateTime;

    public UpdateEstimatedEndaDateRequest() { }

    public UpdateEstimatedEndaDateRequest(Integer maintainanceId, Integer workerUserId, Date estimatedEndDateTime) {
        this.maintainanceId = maintainanceId;
        this.workerUserId = workerUserId;
        this.estimatedEndDateTime = estimatedEndDateTime;
    }

    public boolean checkUpdateEstimatedEndaDateRequest(){
        return this.estimatedEndDateTime != null &&
               this.maintainanceId != null &&
               this.workerUserId != null;
    }
    
    public Integer getMaintainanceId() {
        return maintainanceId;
    }

    public void setMaintainanceId(Integer maintainanceId) {
        this.maintainanceId = maintainanceId;
    }

    public Integer getWorkerUserId() {
        return workerUserId;
    }

    public void setWorkerUserId(Integer workerUserId) {
        this.workerUserId = workerUserId;
    }

    public Date getEstimatedEndDateTime() {
        return estimatedEndDateTime;
    }

    public void setEstimatedEndDateTime(Date estimatedEndDateTime) {
        this.estimatedEndDateTime = estimatedEndDateTime;
    }
    
}
