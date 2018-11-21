/*
 * Created by Adam Wilborn on 2018.11.20  * 
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

@Table(name = "NationalParks")

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NationalParks.findAll", query = "SELECT n FROM NationalParks n")
    , @NamedQuery(name = "NationalParks.findById", query = "SELECT n FROM NationalParks n WHERE n.id = :id")
    , @NamedQuery(name = "NationalParks.findByFullName", query = "SELECT n FROM NationalParks n WHERE n.fullName = :fullName")
    , @NamedQuery(name = "NationalParks.findByStates", query = "SELECT n FROM NationalParks n WHERE n.states = :states")
    , @NamedQuery(name = "NationalParks.findByParkCode", query = "SELECT n FROM NationalParks n WHERE n.parkCode = :parkCode")
    , @NamedQuery(name = "NationalParks.findByImageUrl", query = "SELECT n FROM NationalParks n WHERE n.imageUrl = :imageUrl")})
public class NationalParks implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 44)
    @Column(name = "fullName")
    private String fullName;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "twitterHandle")
    private String twitterHandle;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "states")
    private String states;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "parkCode")
    private String parkCode;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "imageUrl")
    private String imageUrl;

    public NationalParks() {
    }

    public NationalParks(Integer id) {
        this.id = id;
    }

    public NationalParks(Integer id, String fullName, String twitterHandle, String states, String parkCode, String imageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.twitterHandle = twitterHandle;
        this.states = states;
        this.parkCode = parkCode;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getTwitterHandle() {
        return twitterHandle;
    }
    
    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        if (!(object instanceof NationalParks)) {
            return false;
        }
        NationalParks other = (NationalParks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
}
