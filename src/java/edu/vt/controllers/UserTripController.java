/*
 * Created by Osman Balci on 2018.06.20
 * Copyright © 2018 Osman Balci. All rights reserved. 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.EntityBeans.UserTrip;
import edu.vt.FacadeBeans.UserTripFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import edu.vt.pojo.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@Named("userTripController")
@SessionScoped

public class UserTripController implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    @EJB
    private UserFacade userFacade;

    @EJB
    private UserTripFacade userTripFacade;

    // 'items' is a List containing the object references of UserTrip objects
    private List<UserTrip> items = null;

    // 'selected' contains the object reference of the selected UserTrip object
    private UserTrip selected;

    // 'tripItems' is a List containing the object references of Category objects
    private List<Category> tripItems = null;
    
    private List<Category> allTrips = null;

    private String answerToSecurityQuestion;

    /*
    ==================
    Constructor Method
    ==================
     */
    public UserTripController() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    private UserTripFacade getUserTripFacade() {
        return userTripFacade;
    }

    public void setUserTripFacade(UserTripFacade userTripFacade) {
        this.userTripFacade = userTripFacade;
    }

    /*
    ***************************************************************
    Return the List of Trips Created by the Signed-In User
    ***************************************************************
     */
    public List<UserTrip> getItems() {
        if (items == null) {
            /*
            user_id (database primary key) was put into the SessionMap in the
            initializeSessionMap() method in LoginManager upon user's sign in.
             */
            int userPrimaryKey = (int) Methods.sessionMap().get("user_id");

            items = getUserTripFacade().findUserTripsByUserPrimaryKey(userPrimaryKey);
        }
        return items;
    }

    public void setItems(List<UserTrip> items) {
        this.items = items;
    }

    public UserTrip getSelected() {
        return selected;
    }

    public void setSelected(UserTrip selected) {
        tripItems = null;    // Invalidate list of BEVQ items to trigger re-query.
        this.selected = selected;
    }

    public List<Category> getTripItems() {
        if (tripItems == null) {

            tripItems = new ArrayList<>();

            String trip = selected.getTrip();
            JSONArray jsonArray = new JSONArray(trip);

            jsonArray.forEach(object -> {
                // Typecast the object as JSONObject
                JSONObject jsonObject = (JSONObject) object;

                String destination = jsonObject.getString("destination");
                String leaveDate = jsonObject.getString("leaveDate");
                String returnDate = jsonObject.getString("returnDate");

                // Create a Category object using the attributes (Key-Value pairs) of the jsonObject
                Category category = new Category(destination, leaveDate, returnDate);

                // Add newly created Category object to the ArrayList
                tripItems.add(category);
            });
        }
        return tripItems;
    }

    public List<Category> getAllTrips() {
        if (allTrips == null){
            allTrips = new ArrayList<>();
            
           
        }
        return allTrips;
    }

    public void setAllTrips(List<Category> allTrips) {
        this.allTrips = allTrips;
    }
    

    public void setTripItems(List<Category> tripItems) {
        this.tripItems = tripItems;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void setAnswerToSecurityQuestion(String answerToSecurityQuestion) {
        this.answerToSecurityQuestion = answerToSecurityQuestion;
    }

    /*
    ================
    Instance Methods
    ================

    *****************************************************
    Process the Submitted Answer to the Security Question
    *****************************************************
     */
    public void securityAnswerSubmit() {
        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        User signedInUser = (User) Methods.sessionMap().get("user");

        String actualSecurityAnswer = signedInUser.getSecurityAnswer();
        String enteredSecurityAnswer = getAnswerToSecurityQuestion();

        if (actualSecurityAnswer.equals(enteredSecurityAnswer)) {
            // Answer to the security question is correct. Delete the selected trip.
            deleteTrip();

        } else {
            Methods.showMessage("Error", "Answer to the Security Question is Incorrect!", "");
        }
    }

    /*
    *************************************
    Prepare to Create a New Trip
    *************************************
     */
    public UserTrip prepareCreate() {
        /*
        Instantiate a new UserTrip object and store its object reference into instance
        variable 'selected'. The UserTrip class is defined in UserTrip.java
         */
        selected = new UserTrip();

        // Return the object reference of the newly created UserTrip object
        return selected;
    }

    /*
    ******************************************
    Create a New Trip in the Database
    ******************************************
     */
    public void create() {
        /*
        We need to preserve the messages since we will redirect to show a
        different JSF page after successful creation of the trip.
         */
        Methods.preserveMessages();
        /*
        Show the message "Thank You! Your Trip was Successfully Saved in the Database!"
        given in the Bundle.properties file under the UserTripCreated keyword.

        Prevent displaying the same msg twice, one for Summary and one for Detail, by setting the 
        message Detail to "" in the addSuccessMessage(String msg) method in the jsfUtil.java file.
         */
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserTripCreated"));
        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    // We do not allow update of a trip.
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserTripUpdated"));
    }

    /*
    ***************************************************
    Delete the Selected Trip from the Database
    ***************************************************
     */
    public void deleteTrip() {
        /*
        We need to preserve the messages since we will redirect to show a
        different JSF page after successful deletion of the trip.
         */
        Methods.preserveMessages();
        /*
        Show the message "The Trip was Successfully Deleted from the Database!"
        given in the Bundle.properties file under the UserTripDeleted keyword.
        
        Prevent displaying the same msg twice, one for Summary and one for Detail, by setting the 
        message Detail to "" in the addSuccessMessage(String msg) method in the jsfUtil.java file.
         */
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserTripDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /*
     ****************************************************************************
     *   Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *
     ****************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).
                    
                     UserTripFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    getUserTripFacade().edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.
                    
                     UserTripFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    getUserTripFacade().remove(selected);
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

    /*
    ************************************************
    |   Other Auto Generated Methods by NetBeans   |
    ************************************************
     */
    public UserTrip getUserTrip(java.lang.Integer id) {
        return getUserTripFacade().find(id);
    }
    
    public int getSize(){
        return this.getItemsAvailableSelectMany().size();
    }

    public List<UserTrip> getItemsAvailableSelectMany() {
        return getUserTripFacade().findAll();
    }

    public List<UserTrip> getItemsAvailableSelectOne() {
        return getUserTripFacade().findAll();
    }

    @FacesConverter(forClass = UserTrip.class)
    public static class UserTripControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserTripController controller = (UserTripController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userTripController");
            return controller.getUserTrip(getKey(value));
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
            if (object instanceof UserTrip) {
                UserTrip o = (UserTrip) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
                        new Object[]{object, object.getClass().getName(), UserTrip.class.getName()});
                return null;
            }
        }

    }

    /*
    ***************************************************
    Type Converter Methods for PrimeFaces Data Exporter 
    Function to Export Data into PDF, Excel, and CSV
    ***************************************************
    
    PrimeFaces p:dataExporter requires the exported values to be of String type.
    These methods are called in TripDetails.xhtml.
     */
    public String convertIntToString(Integer value) {
        return Integer.toString(value);
    }

    public String convertDoubleToString(Double value) {
        return Double.toString(value);
    }
}