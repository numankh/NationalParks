/*
 * Created by Brannon Angers on 2018.12.02  * 
 * Copyright © 2018 Brannon Angers. All rights reserved. * 
 */
package edu.vt.controllers;
import edu.vt.EntityBeans.PublicFile;
import edu.vt.FacadeBeans.PublicFileFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javaxt.io.Image;

@Named("photoLocationController")
@SessionScoped
public class PhotoLocationController implements Serializable {
    
    private double longitude;
    private double latitude;
    private PublicFile photo;
    private boolean locationExists = false;
    
    @EJB
    private PublicFileFacade publicFileFacade;
    
    @Inject
    PublicFileController publicFileController;
    
    public PhotoLocationController() {}

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public PublicFile getPhoto() {
        return photo;
    }

    public void setPhoto(PublicFile photo) {
        this.photo = photo;
    }

    public PublicFileFacade getPublicFileFacade() {
        return publicFileFacade;
    }

    public void setPublicFileFacade(PublicFileFacade publicFileFacade) {
        this.publicFileFacade = publicFileFacade;
    }

    public boolean isLocationExists() {
        return locationExists;
    }

    public void setLocationExists(boolean locationExists) {
        this.locationExists = locationExists;
    }
    
    
    
    public String showLocationOfPhoto() {
        String id_string = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pic_id");
        int id = Integer.parseInt(id_string);
        photo = getPublicFileFacade().getPublicFile(id);
        String filePath = photo.getFilePath();
        try {
            Image image = new Image(filePath);
            double[] gps = image.getGPSCoordinate();
            this.longitude = gps[0];
            this.latitude = gps[1];
            this.locationExists = true;
        }
        catch (Exception e) {
            this.locationExists = false;
        }
        return "/userFile/LocationOnMap?faces-redirect=true";
    }
    
}
