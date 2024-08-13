/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "job_review")
@NamedQueries({
    @NamedQuery(name = "JobReview.findAll", query = "SELECT j FROM JobReview j"),
    @NamedQuery(name = "JobReview.findByJobReviewId", query = "SELECT j FROM JobReview j WHERE j.jobReviewId = :jobReviewId"),
    @NamedQuery(name = "JobReview.findByGrade", query = "SELECT j FROM JobReview j WHERE j.grade = :grade")})
public class JobReview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_review_id")
    private Integer jobReviewId;
    @Lob
    @Column(name = "comment")
    private String comment;
    @Column(name = "grade")
    private Integer grade;
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    @ManyToOne
    private Job jobId;

    public JobReview() {
    }

    public JobReview(Integer jobReviewId) {
        this.jobReviewId = jobReviewId;
    }

    public Integer getJobReviewId() {
        return jobReviewId;
    }

    public void setJobReviewId(Integer jobReviewId) {
        this.jobReviewId = jobReviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobReviewId != null ? jobReviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobReview)) {
            return false;
        }
        JobReview other = (JobReview) object;
        if ((this.jobReviewId == null && other.jobReviewId != null) || (this.jobReviewId != null && !this.jobReviewId.equals(other.jobReviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.JobReview[ jobReviewId=" + jobReviewId + " ]";
    }
    
}
