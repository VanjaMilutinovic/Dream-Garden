/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "job")
@NamedQueries({
    @NamedQuery(name = "Job.findAll", query = "SELECT j FROM Job j"),
    @NamedQuery(name = "Job.findByJobId", query = "SELECT j FROM Job j WHERE j.jobId = :jobId"),
    @NamedQuery(name = "Job.findByRequestDateTime", query = "SELECT j FROM Job j WHERE j.requestDateTime = :requestDateTime"),
    @NamedQuery(name = "Job.findByStartDateTime", query = "SELECT j FROM Job j WHERE j.startDateTime = :startDateTime"),
    @NamedQuery(name = "Job.findByEndDateTime", query = "SELECT j FROM Job j WHERE j.endDateTime = :endDateTime"),
    @NamedQuery(name = "Job.findByGardenSize", query = "SELECT j FROM Job j WHERE j.gardenSize = :gardenSize"),
    @NamedQuery(name = "findByWorkerIdAndJobStatusId", query = "SELECT j FROM Job j WHERE j.workerId = :workerId AND j.jobStatusId = :jobStatusId"),
    @NamedQuery(name = "findByUserIdAndJobStatusId", query = "SELECT j FROM Job j WHERE j.userId = :userId AND j.jobStatusId = :jobStatusId"),
    @NamedQuery(name = "findByCompanyId", query = "SELECT j FROM Job j WHERE j.companyId = :companyId"),
    @NamedQuery(name = "findByCompanyIdAndJobStatusId", query = "SELECT j FROM Job j WHERE j.companyId = :companyId AND j.jobStatusId = :jobStatusId")})
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "job_id")
    private Integer jobId;
    @Basic(optional = false)
    @Column(name = "request_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDateTime;
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Column(name = "end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "garden_size")
    private BigDecimal gardenSize;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "rejected_description")
    private String rejectedDescription;
    @JsonIgnore
    @OneToMany(mappedBy = "jobId")
    private List<JobPhoto> jobPhotoList;
    @JsonIgnore
    @OneToMany(mappedBy = "jobId")
    private List<Maintenance> maintainanceList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "job")
    private PrivateGarden privateGarden;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "job")
    private RestaurantGarden restaurantGarden;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User userId;
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne
    private Company companyId;
    @JoinColumn(name = "worker_id", referencedColumnName = "user_id")
    @ManyToOne
    private User workerId;
    @JoinColumn(name = "job_status_id", referencedColumnName = "job_status_id")
    @ManyToOne
    private JobStatus jobStatusId;
    @JoinColumn(name = "garden_type_id", referencedColumnName = "garden_type_id")
    @ManyToOne
    private GardenType gardenTypeId;
    @OneToMany(mappedBy = "jobId")
    private List<JobService> jobServiceList;
    @OneToMany(mappedBy = "jobId")
    private List<JobReview> jobReviewList;

    public Job() {
    }

    public Job(Integer jobId) {
        this.jobId = jobId;
    }

    public Job(Integer jobId, Date requestDateTime, BigDecimal gardenSize) {
        this.jobId = jobId;
        this.requestDateTime = requestDateTime;
        this.gardenSize = gardenSize;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public BigDecimal getGardenSize() {
        return gardenSize;
    }

    public void setGardenSize(BigDecimal gardenSize) {
        this.gardenSize = gardenSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRejectedDescription() {
        return rejectedDescription;
    }

    public void setRejectedDescription(String rejectedDescription) {
        this.rejectedDescription = rejectedDescription;
    }

    public List<JobPhoto> getJobPhotoList() {
        return jobPhotoList;
    }

    public void setJobPhotoList(List<JobPhoto> jobPhotoList) {
        this.jobPhotoList = jobPhotoList;
    }

    public List<Maintenance> getMaintainanceList() {
        return maintainanceList;
    }

    public void setMaintainanceList(List<Maintenance> maintainanceList) {
        this.maintainanceList = maintainanceList;
    }

    public PrivateGarden getPrivateGarden() {
        return privateGarden;
    }

    public void setPrivateGarden(PrivateGarden privateGarden) {
        this.privateGarden = privateGarden;
    }

    public RestaurantGarden getRestaurantGarden() {
        return restaurantGarden;
    }

    public void setRestaurantGarden(RestaurantGarden restaurantGarden) {
        this.restaurantGarden = restaurantGarden;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public User getWorkerId() {
        return workerId;
    }

    public void setWorkerId(User workerId) {
        this.workerId = workerId;
    }

    public JobStatus getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(JobStatus jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public GardenType getGardenTypeId() {
        return gardenTypeId;
    }

    public void setGardenTypeId(GardenType gardenTypeId) {
        this.gardenTypeId = gardenTypeId;
    }

    public List<JobService> getJobServiceList() {
        return jobServiceList;
    }

    public void setJobServiceList(List<JobService> jobServiceList) {
        this.jobServiceList = jobServiceList;
    }

    public List<JobReview> getJobReviewList() {
        return jobReviewList;
    }

    public void setJobReviewList(List<JobReview> jobReviewList) {
        this.jobReviewList = jobReviewList;
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
        if (!(object instanceof Job)) {
            return false;
        }
        Job other = (Job) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.Job[ jobId=" + jobId + " ]";
    }
    
}
