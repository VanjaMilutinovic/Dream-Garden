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
@Table(name = "user_status")
@NamedQueries({
    @NamedQuery(name = "UserStatus.findAll", query = "SELECT u FROM UserStatus u"),
    @NamedQuery(name = "UserStatus.findByUserStatusId", query = "SELECT u FROM UserStatus u WHERE u.userStatusId = :userStatusId"),
    @NamedQuery(name = "UserStatus.findByStatus", query = "SELECT u FROM UserStatus u WHERE u.status = :status")})
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_status_id")
    private Integer userStatusId;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "userStatusId")
    private List<User> userList;

    public UserStatus() {
    }

    public UserStatus(Integer userStatusId) {
        this.userStatusId = userStatusId;
    }

    public UserStatus(Integer userStatusId, String status) {
        this.userStatusId = userStatusId;
        this.status = status;
    }

    public Integer getUserStatusId() {
        return userStatusId;
    }

    public void setUserStatusId(Integer userStatusId) {
        this.userStatusId = userStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userStatusId != null ? userStatusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserStatus)) {
            return false;
        }
        UserStatus other = (UserStatus) object;
        if ((this.userStatusId == null && other.userStatusId != null) || (this.userStatusId != null && !this.userStatusId.equals(other.userStatusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.UserStatus[ userStatusId=" + userStatusId + " ]";
    }
    
}
