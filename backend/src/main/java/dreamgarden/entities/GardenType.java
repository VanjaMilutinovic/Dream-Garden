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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "garden_type")
@NamedQueries({
    @NamedQuery(name = "GardenType.findAll", query = "SELECT g FROM GardenType g"),
    @NamedQuery(name = "GardenType.findByGardenTypeId", query = "SELECT g FROM GardenType g WHERE g.gardenTypeId = :gardenTypeId"),
    @NamedQuery(name = "GardenType.findByType", query = "SELECT g FROM GardenType g WHERE g.type = :type")})
public class GardenType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "garden_type_id")
    private Integer gardenTypeId;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @JsonIgnore
    @OneToMany(mappedBy = "gardenTypeId")
    private List<Job> jobList;

    public GardenType() {
    }

    public GardenType(Integer gardenTypeId) {
        this.gardenTypeId = gardenTypeId;
    }

    public GardenType(Integer gardenTypeId, String type) {
        this.gardenTypeId = gardenTypeId;
        this.type = type;
    }

    public Integer getGardenTypeId() {
        return gardenTypeId;
    }

    public void setGardenTypeId(Integer gardenTypeId) {
        this.gardenTypeId = gardenTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        hash += (gardenTypeId != null ? gardenTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GardenType)) {
            return false;
        }
        GardenType other = (GardenType) object;
        if ((this.gardenTypeId == null && other.gardenTypeId != null) || (this.gardenTypeId != null && !this.gardenTypeId.equals(other.gardenTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.GardenType[ gardenTypeId=" + gardenTypeId + " ]";
    }
    
}
