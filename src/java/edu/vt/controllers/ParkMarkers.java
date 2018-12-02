/*
 * Created by Brannon Angers on 2018.11.25  * 
 * Copyright © 2018 Brannon Angers. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.NationalParks;
import edu.vt.FacadeBeans.ParkFacade;
import edu.vt.globals.Methods;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

@Named("parkMarkers")
@SessionScoped
/**
 * Class for displaying markers of all of the parks on the home page
 */
public class ParkMarkers implements Serializable{
    private List<NationalParks> items = null;
    
    /**
     * Allows us to get list of parks from the database
     */
    @EJB
    private ParkFacade parkFacade;
    /**
     * Used to disable restrictions so park api works
     */
    private SSLTool tool = new SSLTool();
    
    /**
     * PrimeFaces object for displaying a map
     */
    private MapModel parkModel = null;
  
    /**
     * Marker that has been selected by clicking on a marker from the map
     */
    private Marker marker;
    
    /**
     * Basic constructor
     */
    public ParkMarkers() {}
    
    /**
     * Constructs the map model with all of the markers
     */
    @PostConstruct
    public void init() {
        tool.disableCertificateValidation();
        parkModel = new DefaultMapModel(); // Create map model for park
        getItems(); // Initialize list of parks
        
        /***************Construct request for data from NPS API ***************/
        String apiUrl = "https://developer.nps.gov/api/v1/parks?";
        apiUrl += "parkCode=";
        apiUrl += items.get(0).getParkCode();
        for(int i = 1; i < items.size(); i++) {
            apiUrl += ",";
            apiUrl += items.get(i).getParkCode();
        }
        apiUrl += "&api_key=7Rm7J7uafcu5ov3oZxLNnUybQNYjxRkAXWXuMemx";
        /**********************************************************************/
        
        /*********Try to make request to api and parse data********************/
        try {
            // Get response from api in JSON format
            String jsonData = readUrlContent(apiUrl);
            JSONObject data = new JSONObject(jsonData);
            // Get the array of all park information
            JSONArray params = data.getJSONArray("data");
            
            // Iterate through list of park data
            for(int j = 0; j < params.length(); j++) {
                // Get specific park data
                JSONObject param = params.getJSONObject(j);
                // Get string containg latitude and longitude
                String latLong = param.optString("latLong", "");
                // Get Name of park
                String parkName = param.optString("name", "");
                // Convert shortened park name from api to full park name
                String name = "";
                if (parkName.equals("Denali") || parkName.equals("Gates Of The Arctic") ||
                        parkName.equals("Glacier Bay") || parkName.equals("Great Sand Dunes") ||
                        parkName.equals("Katmai") || parkName.equals("Lake Clark") ||
                        parkName.equals("Wrangell - St Elias")){
                    name = parkName + " National Park & Preserve";
                }
                else if(parkName.equals("Redwood")){
                    name = parkName + " National and State Parks";
                }
                else if(parkName.equals("Sequoia & Kings Canyon")){
                    name = parkName + " National Parks";
                }
                else if(parkName.equals("National Park of American Samoa")) {
                    name = parkName;
                }
                else {
                    name = parkName + " National Park";
                }
                
                // Parse out latitude and longitide
                String[] splits = latLong.split("[:,]");
                Double lat = Double.parseDouble(splits[1]);
                Double lon = Double.parseDouble(splits[3]);
                LatLng coord = new LatLng(lat, lon);
                // Add marker to map model
                parkModel.addOverlay(new Marker(coord, name, ""));
            }
        }
        catch (Exception ex) {
            Methods.showMessage("Error", "Cannot load Park information",
                    "See: " + ex.getMessage());
        }
    }
    
    /**
     * Get list of parks from Database
     * @return list of NationParks object
     */
    public List<NationalParks> getItems() {
        if (items == null) {
            items = getParkFacade().findAll();
        }
        return items;
    }
    
    /**
     * Get National Park Facade
     * @return national park facade
     */
    public ParkFacade getParkFacade() {
        return parkFacade;
    }
    
    /**
     * Set park facade
     * @param parkFacade - new park facade
     */
    public void setParkFacade(ParkFacade parkFacade) {
        this.parkFacade = parkFacade;
    }

    /**
     * The map model for the parks
     * @return Map Model with markers for all parks
     */
    public MapModel getParkModel() {
        if (parkModel == null) {
            init();
        }
        return parkModel;
    }

    /**
     * Set new map model
     * @param parkModel new map model
     */
    public void setParkModel(MapModel parkModel) {
        this.parkModel = parkModel;
    }

    /**
     * Marker that was selected
     * @return last marker to be clicked on
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * Set new current marker
     * @param marker new marker
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
    
    /**
     * Function runs when marker is clicked on map
     * @param event marker that was clicked
     */
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
    
    /**
     * Parse response form api into JSON
     * @param webServiceURL
     * @return string representation of api data in JSON format
     * @throws Exception 
     */
    public String readUrlContent(String webServiceURL) throws Exception {
        /*
        reader is an object reference pointing to an object instantiated from the BufferedReader class.
        Currently, it is "null" pointing to nothing.
         */
        BufferedReader reader = null;

        try {
            // Create a URL object from the webServiceURL given
            URL url = new URL(webServiceURL);
            
            /*
            The BufferedReader class reads text from a character-input stream, buffering characters
            so as to provide for the efficient reading of characters, arrays, and lines.
             */
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            // Create a mutable sequence of characters and store its object reference into buffer
            StringBuilder buffer = new StringBuilder();

            // Create an array of characters of size 10240
            char[] chars = new char[10240];

            int numberOfCharactersRead;
            /*
            The read(chars) method of the reader object instantiated from the BufferedReader class
            reads 10240 characters as defined by "chars" into a portion of a buffered array.

            The read(chars) method attempts to read as many characters as possible by repeatedly
            invoking the read method of the underlying stream. This iterated read continues until
            one of the following conditions becomes true:

                (1) The specified number of characters have been read, thus returning the number of characters read.
                (2) The read method of the underlying stream returns -1, indicating end-of-file, or
                (3) The ready method of the underlying stream returns false, indicating that further input requests would block.

            If the first read on the underlying stream returns -1 to indicate end-of-file then the read(chars) method returns -1.
            Otherwise the read(chars) method returns the number of characters actually read.
             */
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            // Return the String representation of the created buffer
            return buffer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
