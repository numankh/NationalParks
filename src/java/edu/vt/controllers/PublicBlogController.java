/*
 * Created by Adam Wilborn on 2018.11.27  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PublicBlog;
import edu.vt.FacadeBeans.PublicBlogFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("publicBlogController")
@SessionScoped
public class PublicBlogController implements Serializable {
    
    @EJB
    private PublicBlogFacade publicBlogFacade;
    private List<PublicBlog> items = null;
    private PublicBlog selected;
    
    @Inject
    private UserBlogController userBlogController;
    
    public PublicBlogController() {
        
    }

    public UserBlogController getUserBlogController() {
        return userBlogController;
    }
    
    public void setUserBlogController(UserBlogController userBlogController) {
        this.userBlogController = userBlogController;
    }
    
    private PublicBlogFacade getPublicBlogFacade() {
        return publicBlogFacade;
    }

    public void setPublicBlogFacade(PublicBlogFacade publicBlogFacade) {
        this.publicBlogFacade = publicBlogFacade;
    }
    
    public PublicBlog getSelected() {
        return selected;
    }

    public void setSelected(PublicBlog selected) {
        this.selected = selected;
    }

    private PublicBlogFacade getFacade() {
        return publicBlogFacade;
    }
    
    public PublicBlog prepareCreate() {
        selected = new PublicBlog();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PublicBlogCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public List<PublicBlog> getItems() {
        if (items == null) {
            items = getFacade().findAll();
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

    public PublicBlog getPublicBlog(java.lang.Integer id) {
        return getFacade().find(id);
    }
    
    @FacesConverter(forClass = PublicBlog.class)
    public static class PublicBlogControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PublicBlogController controller = (PublicBlogController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "publicBlogController");
            return controller.getPublicBlog(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PublicBlog) {
                PublicBlog o = (PublicBlog) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PublicBlog.class.getName()});
                return null;
            }
        }

    }
    
}
