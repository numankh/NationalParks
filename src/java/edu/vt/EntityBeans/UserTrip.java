/*
 * Created by Nicholas Eda on 2018.11.28  * 
 * Copyright © 2018 Nicholas Eda. All rights reserved. * 
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Nick
 */
@Entity
@Table(name = "UserTrip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserTrip.findAll", query = "SELECT u FROM UserTrip u")
    , @NamedQuery(name = "UserTrip.findById", query = "SELECT u FROM UserTrip u WHERE u.id = :id")
    , @NamedQuery(name = "UserTrip.findByDateEntered", query = "SELECT u FROM UserTrip u WHERE u.dateEntered = :dateEntered")
    , @NamedQuery(name = "UserTrip.findTripsByUserPrimaryKey", query = "SELECT u FROM UserTrip u WHERE u.userId.id = :primaryKey")})

public class UserTrip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_entered")
    @Temporal(TemporalType.DATE)
    private Date dateEntered;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "trip")
    private String trip;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;
    
    public UserTrip() {
    }

    public UserTrip(Integer id) {
        this.id = id;
    }

    public UserTrip(Integer id, Date dateEntered, String trip) {
        this.id = id;
        this.dateEntered = dateEntered;
        this.trip = trip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
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
        if (!(object instanceof UserTrip)) {
            return false;
        }
        UserTrip other = (UserTrip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.vt.EntityBeans.UserTrip[ id=" + id + " ]";
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
    
    
    public String getDestination() {
        JSONArray arr = new JSONArray(this.trip);
        JSONObject obj = arr.getJSONObject(0);
        return obj.optString("destination");
    }
    
}
