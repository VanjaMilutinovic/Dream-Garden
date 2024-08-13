/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "job_status")
@NamedQueries({
    @NamedQuery(name = "JobStatus.findAll", query = "SELECT j FROM JobStatus j"),
    @NamedQuery(name = "JobStatus.findByJobStatusId", query = "SELECT j FROM JobStatus j WHERE j.jobStatusId = :jobStatusId"),
    @NamedQuery(name = "JobStatus.findByStatus", query = "SELECT j FROM JobStatus j WHERE j.status = :status")})
public class JobStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_status_id")
    private Integer jobStatusId;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "jobStatusId")
    private List<Maintainance> maintainanceList;
    @OneToMany(mappedBy = "jobStatusId")
    private List<Job> jobList;

    public JobStatus() {
    }

    public JobStatus(Integer jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public JobStatus(Integer jobStatusId, String status) {
        this.jobStatusId = jobStatusId;
        this.status = status;
    }

    public Integer getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(Integer jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Maintainance> getMaintainanceList() {
        return maintainanceList;
    }

    public void setMaintainanceList(List<Maintainance> maintainanceList) {
        this.maintainanceList = maintainanceList;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobStatusId != null ? jobStatusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobStatus)) {
            return false;
        }
        JobStatus other = (JobStatus) object;
        if ((this.jobStatusId == null && other.jobStatusId != null) || (this.jobStatusId != null && !this.jobStatusId.equals(other.jobStatusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.JobStatus[ jobStatusId=" + jobStatusId + " ]";
    }
    
}
