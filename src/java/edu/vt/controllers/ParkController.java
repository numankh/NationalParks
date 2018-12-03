/*
 * Created by Adam Wilborn on 2018.11.20  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.NationalParks;
import edu.vt.FacadeBeans.ParkFacade;
import edu.vt.globals.Methods;
import edu.vt.globals.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@Named("parkController")
@SessionScoped
public class ParkController implements Serializable {
    
    private List<NationalParks> items = null;
    private List<NationalParks> searchItems = null;
    private NationalParks selected;
    
    private String searchField;
    private String searchString;
    
    @EJB
    private ParkFacade parkFacade;
    private SSLTool tool = new SSLTool();
    
    @Inject
    private ParkMarkers parkMarkers;
    
    public ParkController() {
        
    }
    
    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    public List<NationalParks> getItems() {
        if (items == null) {
            items = getParkFacade().findAll();
        }
        return items;
    }
    
    public List<NationalParks> getSearchItems() {
        if (searchItems == null) {
            switch(searchField) {
                case "Park Name":
                    searchItems = getParkFacade().nameQuery(searchString);
                    break;
                case "State":
                    searchItems = getParkFacade().stateQuery(searchString);
                    break;
                default:
                    searchItems = getParkFacade().allQuery(searchString);
            }
        }
        return searchItems;
    }
    
    public String search() {
        selected = null;
        searchItems = null;
        return "/search/SearchResults?faces-redirect=true";
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
    
    public String displayParkInformation() {
        if (selected == null) {
            return "";
        }
        return "/nationalParks/View?faces-redirect=true";
    }
    
    public String selectedParkDescription() throws Exception {
        tool.disableCertificateValidation();
        String apiUrl = "https://developer.nps.gov/api/v1/parks?parkCode=" + getSelected().getParkCode() + "&api_key=XM0CTjflUAsumArBchomTuUFRFZDA5xcj5I3v1xY";
        String jsonData = readUrlContent(apiUrl);
        JSONObject data = new JSONObject(jsonData);
        JSONArray params = data.getJSONArray("data");
        JSONObject param1 = params.getJSONObject(0);
        String parkDescription = param1.optString("description", "");
        return parkDescription;
    }
    
    public String selectedParkLatLong() throws Exception {
        String apiUrl = "https://developer.nps.gov/api/v1/parks?parkCode=" + getSelected().getParkCode() + "&api_key=XM0CTjflUAsumArBchomTuUFRFZDA5xcj5I3v1xY";
        String jsonData = readUrlContent(apiUrl);
        JSONObject data = new JSONObject(jsonData);
        JSONArray params = data.getJSONArray("data");
        JSONObject param1 = params.getJSONObject(0);
        String parkLatLong = param1.optString("latLong", "");
        parkLatLong = parkLatLong.replace("lat:", "");
        parkLatLong = parkLatLong.replace(" long:", "");
        return parkLatLong;
    }
    
     public String givenParkLat(String parkName) throws Exception {
        String apiUrl = "https://developer.nps.gov/api/v1/parks?parkCode=" + getCodebyName(parkName) + "&api_key=XM0CTjflUAsumArBchomTuUFRFZDA5xcj5I3v1xY";
        String jsonData = readUrlContent(apiUrl);
        JSONObject data = new JSONObject(jsonData);
        JSONArray params = data.getJSONArray("data");
        JSONObject param1 = params.getJSONObject(0);
        String parkLatLong = param1.optString("latLong", "");
        parkLatLong = parkLatLong.replace("lat:", "");
        int index = parkLatLong.indexOf("long:");
        parkLatLong = parkLatLong.substring(0, index);
        int secondIndex = parkLatLong.indexOf(",");
        parkLatLong = parkLatLong.substring(0, secondIndex);
        System.out.println(parkName + " is at " + parkLatLong);
        return parkLatLong;
    }
     
          public String givenParkLong(String parkName) throws Exception {
        String apiUrl = "https://developer.nps.gov/api/v1/parks?parkCode=" + getCodebyName(parkName) + "&api_key=XM0CTjflUAsumArBchomTuUFRFZDA5xcj5I3v1xY";
        String jsonData = readUrlContent(apiUrl);
        JSONObject data = new JSONObject(jsonData);
        JSONArray params = data.getJSONArray("data");
        JSONObject param1 = params.getJSONObject(0);
        String parkLatLong = param1.optString("latLong", "");
        int index = parkLatLong.indexOf("long:");
        parkLatLong = parkLatLong.substring(index);
        parkLatLong = parkLatLong.replace("long:", "");
        return parkLatLong;
    }
          
          public String airportCode(String parkName) throws Exception {
              String apiUrl = "http://aviation-edge.com/v2/public/nearby?";
              apiUrl += "key=";
              apiUrl += Constants.AEKEY;
              apiUrl += "&lat=";
              apiUrl += givenParkLat(parkName);
              apiUrl += "&lng=";
              apiUrl += givenParkLong(parkName);
              apiUrl += "&distance=200";
              String jsonData = readUrlContent(apiUrl);
              System.out.println("apiURL= " + apiUrl);
              JSONArray data = new JSONArray(jsonData);
              String aCode = "Brannon Sucks";
              if (data.length() > 0){
                  JSONObject code = data.getJSONObject(0);
                  aCode = code.optString("codeIataAirport");
              }         
              System.out.println(aCode);
              return aCode;
          }
    
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
    
    public String selectByMarker() {
        getItems();
        String parkName = parkMarkers.getMarker().getTitle();
        for (int i = 0; i < items.size(); i++) {
            if( items.get(i).getFullName().equalsIgnoreCase(parkName) ) {
                selected  = items.get(i);
                return "/nationalParks/View?faces-redirect=true";
            }
        }
        return "index?faces-redirect=true";
    }
    
    public String getNameByCode(String code) {
        getItems();
        for (int i = 0; i < items.size(); i++) {
            if( items.get(i).getParkCode().equalsIgnoreCase(code) ) {
                return items.get(i).getFullName();
            }
        }
        return code;
    }
    
     public String getCodebyName(String name) {
        getItems();
        for (int i = 0; i < items.size(); i++) {
            if( items.get(i).getFullName().equalsIgnoreCase(name) ) {
                System.out.println("didn't fuck up here! " + items.get(i).getParkCode());
                return items.get(i).getParkCode();
            }
        }
        return "failed";
    }
}


