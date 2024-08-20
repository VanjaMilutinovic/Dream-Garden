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
public class CreateServiceRequest {
    private BigDecimal price;
    private String serviceName;
    private String serviceDescription;
    private Integer companyId;

    public CreateServiceRequest() {
    }

    public CreateServiceRequest(BigDecimal price, String serviceName, String serviceDescription, Integer companyId) {
        this.price = price;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.companyId = companyId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
}
