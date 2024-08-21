/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "private_garden")
@NamedQueries({
    @NamedQuery(name = "PrivateGarden.findAll", query = "SELECT p FROM PrivateGarden p"),
    @NamedQuery(name = "PrivateGarden.findByJobId", query = "SELECT p FROM PrivateGarden p WHERE p.jobId = :jobId"),
    @NamedQuery(name = "PrivateGarden.findByPoolSize", query = "SELECT p FROM PrivateGarden p WHERE p.poolSize = :poolSize"),
    @NamedQuery(name = "PrivateGarden.findByNumberOfPools", query = "SELECT p FROM PrivateGarden p WHERE p.numberOfPools = :numberOfPools"),
    @NamedQuery(name = "PrivateGarden.findByGrassSize", query = "SELECT p FROM PrivateGarden p WHERE p.grassSize = :grassSize"),
    @NamedQuery(name = "PrivateGarden.findByPavedSize", query = "SELECT p FROM PrivateGarden p WHERE p.pavedSize = :pavedSize")})
public class PrivateGarden implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "job_id")
    private Integer jobId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pool_size")
    private BigDecimal poolSize;
    @Column(name = "number_of_pools")
    private Integer numberOfPools;
    @Column(name = "grass_size")
    private BigDecimal grassSize;
    @Column(name = "paved_size")
    private BigDecimal pavedSize;
    @JsonIgnore
    @JoinColumn(name = "job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Job job;

    public PrivateGarden() {
    }

    public PrivateGarden(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public BigDecimal getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(BigDecimal poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getNumberOfPools() {
        return numberOfPools;
    }

    public void setNumberOfPools(Integer numberOfPools) {
        this.numberOfPools = numberOfPools;
    }

    public BigDecimal getGrassSize() {
        return grassSize;
    }

    public void setGrassSize(BigDecimal grassSize) {
        this.grassSize = grassSize;
    }

    public BigDecimal getPavedSize() {
        return pavedSize;
    }

    public void setPavedSize(BigDecimal pavedSize) {
        this.pavedSize = pavedSize;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobId != null ? jobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrivateGarden)) {
            return false;
        }
        PrivateGarden other = (PrivateGarden) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PrivateGarden{" + "jobId=" + jobId + ", poolSize=" + poolSize + ", numberOfPools=" + numberOfPools + ", grassSize=" + grassSize + ", pavedSize=" + pavedSize + ", job=" + job + '}';
    }

    
    
}
