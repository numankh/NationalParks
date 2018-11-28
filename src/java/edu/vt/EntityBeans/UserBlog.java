/*
 * Created by Adam Wilborn on 2018.11.27  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author brn2s
 */
@Entity
@Table(name = "UserBlog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserBlog.findAll", query = "SELECT u FROM UserBlog u")
    , @NamedQuery(name = "UserBlog.findById", query = "SELECT u FROM UserBlog u WHERE u.id = :id")
    , @NamedQuery(name = "UserBlog.findByPark", query = "SELECT u FROM UserBlog u WHERE u.park = :park")
    , @NamedQuery(name = "UserBlog.findByDescription", query = "SELECT u FROM UserBlog u WHERE u.description = :description")
    , @NamedQuery(name = "UserBlog.findByRating", query = "SELECT u FROM UserBlog u WHERE u.rating = :rating")
    , @NamedQuery(name = "UserBlog.findUserBlogsByUserId", query = "SELECT p FROM UserBlog p WHERE p.userId.id = :userId")})
public class UserBlog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 44)
    @Column(name = "park")
    private String park;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "rating")
    private String rating;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public UserBlog() {
    }

    public UserBlog(Integer id) {
        this.id = id;
    }

    public UserBlog(Integer id, String park, String description, String rating) {
        this.id = id;
        this.park = park;
        this.description = description;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserBlog)) {
            return false;
        }
        UserBlog other = (UserBlog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.vt.EntityBeans.UserBlog[ id=" + id + " ]";
    }
    
}
