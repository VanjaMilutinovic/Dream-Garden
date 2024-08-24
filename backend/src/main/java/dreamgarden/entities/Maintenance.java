/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "maintenance")
@NamedQueries({
    @NamedQuery(name = "Maintenance.findAll", query = "SELECT m FROM Maintenance m"),
    @NamedQuery(name = "Maintenance.findByJobId", query = "SELECT m FROM Maintenance m WHERE m.jobId = :jobId"),
    @NamedQuery(name = "Maintenance.findByMaintenanceId", query = "SELECT m FROM Maintenance m WHERE m.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "Maintenance.findByRequestDateTime", query = "SELECT m FROM Maintenance m WHERE m.requestDateTime = :requestDateTime"),
    @NamedQuery(name = "Maintenance.findByStartDateTime", query = "SELECT m FROM Maintenance m WHERE m.startDateTime = :startDateTime"),
    @NamedQuery(name = "Maintenance.findByEstimatedEndDateTime", query = "SELECT m FROM Maintenance m WHERE m.estimatedEndDateTime = :estimatedEndDateTime")})
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "maintenance_id")
    private Integer maintenanceId;
    @Basic(optional = false)
    @Column(name = "request_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDateTime;
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Column(name = "estimated_end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date estimatedEndDateTime;
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    @ManyToOne
    private Job jobId;
    @JoinColumn(name = "job_status_id", referencedColumnName = "job_status_id")
    @ManyToOne
    private JobStatus jobStatusId;
    @JoinColumn(name = "worker_id", referencedColumnName = "user_id")
    @ManyToOne
    private User workerId;

    public Maintenance() {
    }

    public Maintenance(Integer maintainanceId) {
        this.maintenanceId = maintainanceId;
    }

    public Maintenance(Integer maintainanceId, Date requestDateTime) {
        this.maintenanceId = maintainanceId;
        this.requestDateTime = requestDateTime;
    }

    public Integer getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Integer maintainanceId) {
        this.maintenanceId = maintainanceId;
    }

    public Date getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEstimatedEndDateTime() {
        return estimatedEndDateTime;
    }

    public void setEstimatedEndDateTime(Date estimatedEndDateTime) {
        this.estimatedEndDateTime = estimatedEndDateTime;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    public JobStatus getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(JobStatus jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public User getWorkerId() {
        return workerId;
    }

    public void setWorkerId(User workerId) {
        this.workerId = workerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceId != null ? maintenanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maintenance)) {
            return false;
        }
        Maintenance other = (Maintenance) object;
        if ((this.maintenanceId == null && other.maintenanceId != null) || (this.maintenanceId != null && !this.maintenanceId.equals(other.maintenanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.Maintenance[ maintainanceId=" + maintenanceId + " ]";
    }
    
}
