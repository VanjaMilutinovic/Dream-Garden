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
public class CreateCompanyHolidayRequest {
    private Date startDateTime;
    private Date endDateTime;
    private Integer companyId;

    public CreateCompanyHolidayRequest() {
    }

    public CreateCompanyHolidayRequest(Date startDateTime, Date endDateTime, Integer companyId) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.companyId = companyId;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
}
