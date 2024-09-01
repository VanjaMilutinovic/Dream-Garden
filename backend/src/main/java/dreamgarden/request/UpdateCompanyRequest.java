/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

import java.math.BigDecimal;

/**
 *
 * @author vamilutinovic
 */
public class UpdateCompanyRequest {
    
    private Integer companyId;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String contactNumber;
    private String contactPerson;

    public UpdateCompanyRequest() {  }
    
    public boolean checkUpdateCompanyRequest() {
         return companyId != null &&
               name != null &&
               address != null &&
               latitude != null &&
               longitude != null &&
               contactNumber != null &&
               contactPerson != null;
    }

    public UpdateCompanyRequest(Integer companyId, String name, String address, BigDecimal latitude, BigDecimal longitude, String contactNumber, String contactPerson) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactNumber = contactNumber;
        this.contactPerson = contactPerson;
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
}
