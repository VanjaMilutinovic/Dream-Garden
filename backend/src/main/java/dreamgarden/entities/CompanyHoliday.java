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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author vamilutinovic
 */
@Entity
@Table(name = "company_holiday")
@NamedQueries({
    @NamedQuery(name = "CompanyHoliday.findAll", query = "SELECT c FROM CompanyHoliday c"),
    @NamedQuery(name = "CompanyHoliday.findByCompanyHolidayId", query = "SELECT c FROM CompanyHoliday c WHERE c.companyHolidayId = :companyHolidayId"),
    @NamedQuery(name = "CompanyHoliday.findByStartDateTime", query = "SELECT c FROM CompanyHoliday c WHERE c.startDateTime = :startDateTime"),
    @NamedQuery(name = "CompanyHoliday.findByEndDateTime", query = "SELECT c FROM CompanyHoliday c WHERE c.endDateTime = :endDateTime")})
public class CompanyHoliday implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "company_holiday_id")
    private Integer companyHolidayId;
    @Basic(optional = false)
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Basic(optional = false)
    @Column(name = "end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    @JsonIgnore
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne
    private Company companyId;

    public CompanyHoliday() {
    }

    public CompanyHoliday(Integer companyHolidayId) {
        this.companyHolidayId = companyHolidayId;
    }

    public CompanyHoliday(Integer companyHolidayId, Date startDateTime, Date endDateTime) {
        this.companyHolidayId = companyHolidayId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Integer getCompanyHolidayId() {
        return companyHolidayId;
    }

    public void setCompanyHolidayId(Integer companyHolidayId) {
        this.companyHolidayId = companyHolidayId;
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

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyHolidayId != null ? companyHolidayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyHoliday)) {
            return false;
        }
        CompanyHoliday other = (CompanyHoliday) object;
        if ((this.companyHolidayId == null && other.companyHolidayId != null) || (this.companyHolidayId != null && !this.companyHolidayId.equals(other.companyHolidayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{companyHolidayId=" + companyHolidayId + ", startDateTime= " + startDateTime + ", endDateTime " + endDateTime + " }";
    }
    
}
