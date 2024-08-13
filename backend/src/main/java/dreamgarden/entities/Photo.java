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
@Table(name = "photo")
@NamedQueries({
    @NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p"),
    @NamedQuery(name = "Photo.findByPhotoId", query = "SELECT p FROM Photo p WHERE p.photoId = :photoId"),
    @NamedQuery(name = "Photo.findByPath", query = "SELECT p FROM Photo p WHERE p.path = :path")})
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "photo_id")
    private Integer photoId;
    @Basic(optional = false)
    @Column(name = "path")
    private String path;
    @OneToMany(mappedBy = "photoId")
    private List<JobPhoto> jobPhotoList;
    @OneToMany(mappedBy = "photoId")
    private List<User> userList;

    public Photo() {
    }

    public Photo(Integer photoId) {
        this.photoId = photoId;
    }

    public Photo(Integer photoId, String path) {
        this.photoId = photoId;
        this.path = path;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<JobPhoto> getJobPhotoList() {
        return jobPhotoList;
    }

    public void setJobPhotoList(List<JobPhoto> jobPhotoList) {
        this.jobPhotoList = jobPhotoList;
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
        hash += (photoId != null ? photoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) object;
        if ((this.photoId == null && other.photoId != null) || (this.photoId != null && !this.photoId.equals(other.photoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dreamgarden.entities.Photo[ photoId=" + photoId + " ]";
    }
    
}
