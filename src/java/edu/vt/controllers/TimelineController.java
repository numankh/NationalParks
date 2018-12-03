package edu.vt.controllers;

import edu.vt.EntityBeans.PublicBlog;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserTrip;
import edu.vt.FacadeBeans.ParkFacade;
import edu.vt.FacadeBeans.PublicBlogFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import edu.vt.pojo.Event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

@Named("timelineController")
@SessionScoped
public class TimelineController implements Serializable {  
   
    private TimelineModel model;  
   
    private boolean selectable = false;  
    private boolean zoomable = true;  
    private boolean moveable = true;  
    private boolean stackEvents = true;  
    private String eventStyle = "box";  
    private boolean showCurrentTime = true;  
    private boolean showNavigation = true;  
    
    @Inject
    private UserTripController userTripController;
    @Inject
    private ParkController parkController;
   
    @PostConstruct 
    protected void initialize()  {  
        model = new TimelineModel();  
        Calendar cal = Calendar.getInstance();   
        
        // add created date.
        String date = userTripController.getSelected().getDateEntered().toString();
        int year = Integer.parseInt(date.substring(24));
        int month = this.monthToInt(date.substring(4,7));
        int day = Integer.parseInt(date.substring(8, 10));  
        cal.set(year, month, day, 0, 0, 0);  
        model.add(new TimelineEvent("Trip Planned", cal.getTime()));  
        
        // add leave date
        String trip = userTripController.getSelected().getTrip();
        String destination = userTripController.getSelected().getDestination();
        JSONArray arr = new JSONArray(trip);
        JSONObject obj = arr.getJSONObject(0);
        date=obj.optString("leaveDate");
        year = Integer.parseInt(date.substring(24));
        month = this.monthToInt(date.substring(4,7));
        day = Integer.parseInt(date.substring(8, 10));
        cal.set(year, month, day, 0, 0, 0);  
        model.add(new TimelineEvent("Leave for " + destination, cal.getTime())); 
        Date leaveDate = cal.getTime();
         
        
        
        //add return date
        date=obj.optString("returnDate");
        year = Integer.parseInt(date.substring(24));
        month = this.monthToInt(date.substring(4,7));
        day = Integer.parseInt(date.substring(8, 10));
        cal.set(year, month, day, 0, 0, 0);  
        model.add(new TimelineEvent("Return from " + destination, cal.getTime()));
        Date returnDate = cal.getTime();
        List<Event> list;
        try{
        list = parkController.getTripEvents(userTripController.getSelected().getDestination());
        }
        catch(Exception e){
            list = new LinkedList<>();
        }
        for(int x = 0; x<list.size(); x++)
        {
            Event temp = list.get(x);
            int y = x+1;
                if(temp.getDate().before(returnDate)&&temp.getDate().after(leaveDate))
                model.add(new TimelineEvent("Event " +y, temp.getDate()));
                
            
            
            
            
            
        }
        
    }   
      
    public int monthToInt(String month){
        if (month.equals("Jan")){
            return 0;
        }
        else if (month.equals("Feb")){
            return 1;
        }
        else if (month.equals("Mar")){
            return 2;
        }
        else if (month.equals("Apr")){
            return 3;
        }
        else if (month.equals("May")){
            return 4;
        }
        else if (month.equals("Jun")){
            return 5;
        }
        else if (month.equals("Jul")){
            return 6;
        }
        else if (month.equals("Aug")){
            return 7;
        }
        else if (month.equals("Sep")){
            return 8;
        }
        else if (month.equals("Oct")){
            return 9;
        }
        else if (month.equals("Nov")){
            return 10;
        }
        else return 11;

    }
    public TimelineModel getModel() {  
        return model;  
    }  
   
    public boolean isSelectable() {  
        return selectable;  
    }  
   
    public void setSelectable(boolean selectable) {  
        this.selectable = selectable;  
    }  
   
    public boolean isZoomable() {  
        return zoomable;  
    }  
   
    public void setZoomable(boolean zoomable) {  
        this.zoomable = zoomable;  
    }  
   
    public boolean isMoveable() {  
        return moveable;  
    }  
   
    public void setMoveable(boolean moveable) {  
        this.moveable = moveable;  
    }  
   
    public boolean isStackEvents() {  
        return stackEvents;  
    }  
   
    public void setStackEvents(boolean stackEvents) {  
        this.stackEvents = stackEvents;  
    }  
   
    public String getEventStyle() {  
        return eventStyle;  
    }  
   
    public void setEventStyle(String eventStyle) {  
        this.eventStyle = eventStyle;  
    }  
   
    public boolean isShowCurrentTime() {  
        return showCurrentTime;  
    }  
   
    public void setShowCurrentTime(boolean showCurrentTime) {  
        this.showCurrentTime = showCurrentTime;  
    }  
   
    public boolean isShowNavigation() {  
        return showNavigation;  
    }  
   
    public void setShowNavigation(boolean showNavigation) {  
        this.showNavigation = showNavigation;  
    }  
}