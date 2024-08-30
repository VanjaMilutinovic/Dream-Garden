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
import java.io.Serializable;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "job_photo")
@NamedQueries({
    @NamedQuery(name = "JobPhoto.findAll", query = "SELECT j FROM JobPhoto j"),
    @NamedQuery(name = "JobPhoto.findByJobPhotoId", query = "SELECT j FROM JobPhoto j WHERE j.jobPhotoId = :jobPhotoId"),
    @NamedQuery(name = "JobPhoto.findByJobId", query = "SELECT j FROM JobPhoto j WHERE j.jobId = :jobId")})
public class JobPhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_photo_id")
    private Integer jobPhotoId;
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    @ManyToOne
    private Job jobId;
    @JoinColumn(name = "photo_id", referencedColumnName = "photo_id")
    @ManyToOne
    private Photo photoId;

    public JobPhoto() {
    }

    public JobPhoto(Integer jobPhotoId) {
        this.jobPhotoId = jobPhotoId;
    }

    public Integer getJobPhotoId() {
        return jobPhotoId;
    }

    public void setJobPhotoId(Integer jobPhotoId) {
        this.jobPhotoId = jobPhotoId;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    public Photo getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Photo photoId) {
        this.photoId = photoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobPhotoId != null ? jobPhotoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobPhoto)) {
            return false;
        }
        JobPhoto other = (JobPhoto) object;
        if ((this.jobPhotoId == null && other.jobPhotoId != null) || (this.jobPhotoId != null && !this.jobPhotoId.equals(other.jobPhotoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.JobPhoto[ jobPhotoId=" + jobPhotoId + " ]";
    }
    
}
