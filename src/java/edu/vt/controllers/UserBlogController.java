/*
 * Created by Adam Wilborn on 2018.11.27  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PublicBlog;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserBlog;
import edu.vt.FacadeBeans.UserBlogFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("userBlogController")
@SessionScoped
public class UserBlogController implements Serializable {
    
    @EJB
    private UserBlogFacade userBlogFacade;
    private List<UserBlog> items = null;
    private UserBlog selected;
    
    @Inject
    private PublicBlogController publicBlogController;
    
    public UserBlogController() {
        
    }
    
    private UserBlogFacade getUserBlogFacade() {
        return userBlogFacade;
    }

    public void setUserBlogFacade(UserBlogFacade userBlogFacade) {
        this.userBlogFacade = userBlogFacade;
    }
    
    public UserBlog getSelected() {
        return selected;
    }

    public void setSelected(UserBlog selected) {
        this.selected = selected;
    }
    
    private UserBlogFacade getFacade() {
        return userBlogFacade;
    }
    
    public UserBlog prepareCreate() {
        selected = new UserBlog();
        User signedInUser = (User) Methods.sessionMap().get("user");
        selected.setUserId(signedInUser);
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserBlogCreated"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void share() {
        PublicBlog publicBlog = new PublicBlog();
        publicBlog.setDescription(selected.getDescription());
        publicBlog.setPark(selected.getPark());
        publicBlog.setRating(selected.getRating());
        publicBlogController.setSelected(publicBlog);
        publicBlogController.create();
    }
    
    public List<UserBlog> getItems() {
        if (items == null) {
            User signedInUser = (User) Methods.sessionMap().get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            items = getUserBlogFacade().findUserBlogsByUserPrimaryKey(primaryKey);
        }
        return items;
    }
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
}
