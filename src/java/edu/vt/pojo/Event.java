/*
 * Created by Reilly McLaren on 2018.12.03  * 
 * Copyright © 2018 Reilly McLaren. All rights reserved. * 
 */
package edu.vt.pojo;

import java.util.Date;
import org.jsoup.Jsoup;

/**
 *
 * @author reill
 */
public class Event {

    private int id;
    private String description;
    private Date date;
    private String dateString;
    private String time;

    public Event(int id, String description, Date date, String dateString, String time) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.dateString = dateString;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        description = Jsoup.parse(description).text();
        description = description.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        description = description.replaceAll("[^\\p{ASCII}]", "");
        return description;
        //return description.replace("<*>","");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }

}
