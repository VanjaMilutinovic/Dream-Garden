/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "job_service")
@NamedQueries({
    @NamedQuery(name = "JobService.findAll", query = "SELECT j FROM JobService j"),
    @NamedQuery(name = "JobService.findByJobId", query = "SELECT j.serviceId FROM JobService j WHERE j.jobId = :jobId"),
    @NamedQuery(name = "JobService.findByJobServiceId", query = "SELECT j FROM JobService j WHERE j.jobServiceId = :jobServiceId")})
public class JobService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_service_id")
    private Integer jobServiceId;
    @JsonIgnore
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    @ManyToOne
    private Job jobId;
    @JoinColumn(name = "service_id", referencedColumnName = "service_id")
    @ManyToOne
    private Service serviceId;

    public JobService() {
    }

    public JobService(Integer jobServiceId) {
        this.jobServiceId = jobServiceId;
    }

    public Integer getJobServiceId() {
        return jobServiceId;
    }

    public void setJobServiceId(Integer jobServiceId) {
        this.jobServiceId = jobServiceId;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobServiceId != null ? jobServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobService)) {
            return false;
        }
        JobService other = (JobService) object;
        if ((this.jobServiceId == null && other.jobServiceId != null) || (this.jobServiceId != null && !this.jobServiceId.equals(other.jobServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.JobService[ jobServiceId=" + jobServiceId + " ]";
    }
    
}
