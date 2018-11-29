/*
 * Created by Nick Eda on 2018.10.20
 * Copyright © 2018 Nick Eda. All rights reserved. 
 */
package edu.vt.managers;

import edu.vt.pojo.Category;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.globals.Methods;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import edu.vt.controllers.UserTripController;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.json.JSONArray;

@Named(value = "tripManager")
@SessionScoped

public class TripManager implements Serializable {

    /*
    ==================
    Instance Variables
    ==================
     */
    // Inject (store) the UserFacade object reference into userFacade after it is instantiated at runtime
    @EJB
    private UserFacade userFacade;

    @Inject
    private UserTripController userTripController;

    // 'items' is a List containing the object references of Category objects
    private List<Category> items = null;

    private String question1;
    private String question2;
    private String question3;
    
    /*
    ==================
    Constructor Method
    ==================
     */
    public TripManager() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public UserFacade getUserFacade() {
        return userFacade;
    }

    public UserTripController getUserTripController() {
        return userTripController;
    }

    public List<Category> getItems() {
        return items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }
  

    /*
    ================
    Instance Methods
    ================

    ***********************************
    Process the Submitted Trip
    ***********************************
     */
    public String processTrip() {

        // Instantiate a new 'items' List to contain the object references of the Category objects
        items = new ArrayList<>();

        String destination;
        String leaveDate;
        String returnDate;
        
        /* 
         ******************
         *   Question 1   *
         ******************
         */
        destination = question1;
        leaveDate = question2;
        returnDate = question3;
       
        Category category1 = new Category(destination, leaveDate, returnDate);

        items.add(category1);


        
        /*
        The UserTrip Table in HealthTripqDB database has the following attributes to set:
            Integer id (The DB Primary Key id value is generated and set at the time of UserTrip object creation)
            Date    dateEntered
            String  trip (Stored as MEDIUMTEXT in HealthTripqDB containing the JSON Array of all BEVQ 15 categories)
            User    userId
         */
        //--------------------------------------
        // Create a new UserTrip object
        //--------------------------------------
        userTripController.prepareCreate();

        //-----------------------
        // Set id attribute value 
        //-----------------------
        /*
        The database Primary Key id value is generated and set at the time of UserTrip object creation.
         */
        //--------------------------------
        // Set dateEntered attribute value 
        //--------------------------------
        LocalDate localDate = java.time.LocalDate.now();
        Date currentDate = java.sql.Date.valueOf(localDate);

        // Set Date in the format of YYYY-MM-DD
        userTripController.getSelected().setDateEntered(currentDate);

        //----------------------------------
        // Set trip attribute value 
        //----------------------------------
        /*
        ----------------------------------------------------------------------------------------------
        Convert the List of Category objects into an array of JSON objects as depicted below:
    
        This conversion calls each Getter method in the Category class to define the KEY:VALUE pair of
        a JSON object for each Category object attribute as shown above. If you include other 
        Getter methods in the Category class, their values will also be included in the JSON file.
        Note that the JSON object {...} lists the Category object attributes in no particular order.
        ----------------------------------------------------------------------------------------------
         */
        JSONArray jasonArray = new JSONArray(items);

        // Convert the JSON array into a String
        String trip = jasonArray.toString();

        // Set the trip attribute value
        userTripController.getSelected().setTrip(trip);

        //---------------------------
        // Set userId attribute value 
        //---------------------------
        // Obtain the object reference of the signed-in user
        User signedInUser = (User) Methods.sessionMap().get("user");

        userTripController.getSelected().setUserId(signedInUser);

        //----------------------------------------------------------
        // Store the newly created UserTrip in the database
        //----------------------------------------------------------
        userTripController.create();

        //-----------------------------------------------------------
        // Clear the trip content without displaying message
        //-----------------------------------------------------------
        clearTrip(false);

        return "/index?faces-redirect=true";
    }
    
    /*
    ************************************************
    Clear All of the Selections in the Trip
    ************************************************
     */
    public void clearTrip(Boolean displayMessage) {

        items = null;

        question1 = null;
        question2 = null;
        question3 = null;

        if (displayMessage) {
            Methods.showMessage("Information", "Cleared!", "All Selections are Reset!");
        }
    }

    /*
    *******************************************
    Pre-process the PDF File to be in Landscape 
    Orientation on Letter Size Paper
    *******************************************
     */
    public void preProcessPDF(Object document) {
        Document pdf = (Document) document;
        pdf.setPageSize(PageSize.LETTER.rotate());
        pdf.open();
    }

    /*
    *****************************************
    Warn the User for 5 Minutes of Inactivity
    *****************************************
     */
    public void onIdle() {
        Methods.showMessage("Warning", "No User Action for the Last 5 Minutes!", "Do You Need More Time?");
    }

    /*
    ***************************************************
    Welcome Back the User After 5 Minutes of Inactivity
    ***************************************************
     */
    public void onActive() {
        Methods.showMessage("Warning", "Welcome Back!", "Please Continue Filling Out Your Questionanire!");
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void setUserTripController(UserTripController userTripController) {
        this.userTripController = userTripController;
    }

    
}
