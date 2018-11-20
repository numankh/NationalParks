/*
 * Created by Adam Wilborn on 2018.11.20  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.NationalParks;
import edu.vt.FacadeBeans.ParkFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("parkController")
@SessionScoped
public class ParkController implements Serializable {
    
    private List<NationalParks> items = null;
    private NationalParks selected;
    
    @EJB
    private ParkFacade parkFacade;
    
    public ParkController() {
        
    }
    
    public List<NationalParks> getItems() {
        if (items == null) {
            items = getParkFacade().findAll();
        }
        return items;
    }
    
    public NationalParks getSelected() {
        return selected;
    }
    
    public void setSelected(NationalParks selected) {
        this.selected = selected;
    }

    public ParkFacade getParkFacade() {
        return parkFacade;
    }
    
    public void setParkFacade(ParkFacade parkFacade) {
        this.parkFacade = parkFacade;
    }
}
