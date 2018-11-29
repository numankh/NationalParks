/*
 * Created by Nick Eda on 2018.10.14
 * Copyright © 2018 Nick Eda. All rights reserved.
 */
package edu.vt.pojo;

// This class provides a Plain Old Java Object (POJO) representing a BEVQ category
public class Category {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String destination;
    private String leaveDate;
    private String returnDate;

    public Category(String destination, String leaveDate, String returnDate) {
        this.destination = destination;
        this.leaveDate = leaveDate;
        this.returnDate = returnDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    
    
}

