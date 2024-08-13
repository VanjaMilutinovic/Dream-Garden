/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "restaurant_garden")
@NamedQueries({
    @NamedQuery(name = "RestaurantGarden.findAll", query = "SELECT r FROM RestaurantGarden r"),
    @NamedQuery(name = "RestaurantGarden.findByJobId", query = "SELECT r FROM RestaurantGarden r WHERE r.jobId = :jobId"),
    @NamedQuery(name = "RestaurantGarden.findByFountainSize", query = "SELECT r FROM RestaurantGarden r WHERE r.fountainSize = :fountainSize"),
    @NamedQuery(name = "RestaurantGarden.findByNumberOfFountains", query = "SELECT r FROM RestaurantGarden r WHERE r.numberOfFountains = :numberOfFountains"),
    @NamedQuery(name = "RestaurantGarden.findByGrassSize", query = "SELECT r FROM RestaurantGarden r WHERE r.grassSize = :grassSize"),
    @NamedQuery(name = "RestaurantGarden.findByNumberOfTables", query = "SELECT r FROM RestaurantGarden r WHERE r.numberOfTables = :numberOfTables"),
    @NamedQuery(name = "RestaurantGarden.findByNumberOfSeats", query = "SELECT r FROM RestaurantGarden r WHERE r.numberOfSeats = :numberOfSeats")})
public class RestaurantGarden implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "job_id")
    private Integer jobId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fountain_size")
    private BigDecimal fountainSize;
    @Column(name = "number_of_fountains")
    private Integer numberOfFountains;
    @Column(name = "grass_size")
    private BigDecimal grassSize;
    @Column(name = "number_of_tables")
    private Integer numberOfTables;
    @Column(name = "number_of_seats")
    private Integer numberOfSeats;
    @JoinColumn(name = "job_id", referencedColumnName = "job_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Job job;

    public RestaurantGarden() {
    }

    public RestaurantGarden(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public BigDecimal getFountainSize() {
        return fountainSize;
    }

    public void setFountainSize(BigDecimal fountainSize) {
        this.fountainSize = fountainSize;
    }

    public Integer getNumberOfFountains() {
        return numberOfFountains;
    }

    public void setNumberOfFountains(Integer numberOfFountains) {
        this.numberOfFountains = numberOfFountains;
    }

    public BigDecimal getGrassSize() {
        return grassSize;
    }

    public void setGrassSize(BigDecimal grassSize) {
        this.grassSize = grassSize;
    }

    public Integer getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(Integer numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
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
        if (!(object instanceof RestaurantGarden)) {
            return false;
        }
        RestaurantGarden other = (RestaurantGarden) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.RestaurantGarden[ jobId=" + jobId + " ]";
    }
    
}
