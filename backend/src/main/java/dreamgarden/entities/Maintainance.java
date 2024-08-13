/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "maintainance")
@NamedQueries({
    @NamedQuery(name = "Maintainance.findAll", query = "SELECT m FROM Maintainance m"),
    @NamedQuery(name = "Maintainance.findByMaintainanceId", query = "SELECT m FROM Maintainance m WHERE m.maintainanceId = :maintainanceId"),
    @NamedQuery(name = "Maintainance.findByRequestDateTime", query = "SELECT m FROM Maintainance m WHERE m.requestDateTime = :requestDateTime"),
    @NamedQuery(name = "Maintainance.findByStartDateTime", query = "SELECT m FROM Maintainance m WHERE m.startDateTime = :startDateTime"),
    @NamedQuery(name = "Maintainance.findByEstimatedEndDateTime", query = "SELECT m FROM Maintainance m WHERE m.estimatedEndDateTime = :estimatedEndDateTime")})
public class Maintainance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "maintainance_id")
    private Integer maintainanceId;
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

    public Maintainance() {
    }

    public Maintainance(Integer maintainanceId) {
        this.maintainanceId = maintainanceId;
    }

    public Maintainance(Integer maintainanceId, Date requestDateTime) {
        this.maintainanceId = maintainanceId;
        this.requestDateTime = requestDateTime;
    }

    public Integer getMaintainanceId() {
        return maintainanceId;
    }

    public void setMaintainanceId(Integer maintainanceId) {
        this.maintainanceId = maintainanceId;
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
        hash += (maintainanceId != null ? maintainanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maintainance)) {
            return false;
        }
        Maintainance other = (Maintainance) object;
        if ((this.maintainanceId == null && other.maintainanceId != null) || (this.maintainanceId != null && !this.maintainanceId.equals(other.maintainanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.Maintainance[ maintainanceId=" + maintainanceId + " ]";
    }
    
}
