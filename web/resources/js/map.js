/* 
 * Created by Osman Balci on 2017.10.23  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */

/********************************************************************************************* 
 You must provide your Google Maps JavaScript API Developer Key to able to show maps and
 get directions. I specified mine in siteTemplate.xhtml as follows:
 
 <script src="https://maps.google.com/maps/api/js?key=AIzaSyDUMWJdnV_fekj5InNCV82_hBq_gKWG8lw" 
 type="text/javascript"></script>
 
 This is given in siteTemplate.xhtml so that each page can use Google Maps JavaScript API. 

 This file provides the JavaScript functions we need to display Google maps and get directions.
 *********************************************************************************************/

/* Global variables */

var google;

// Object reference 'map' to point to a Google Map object
var map;

// Object reference 'currentMarker' to point to a VT building location on map
var currentMarker = null;

/*
 You can obtain directions via driving, bicycling, bus, or walking by using the DirectionsService object.
 This object communicates with the Google Maps API Directions Service which receives direction requests
 and returns computed results. You can handle these directions results by using the DirectionsRenderer
 object to render these results. [https://developers.google.com/maps/documentation/javascript/directions]
 */

// Instantiate a DirectionsService object and store its object reference into directionsService.
var directionsService = new google.maps.DirectionsService();

// Instantiate a DirectionsRenderer object and store its object reference into directionsDisplay.
var directionsDisplay = new google.maps.DirectionsRenderer();

// Create and display a Virginia Tech campus map
function initializeMap() {

    /*
     Instantiate a new Virginia Tech campus map object and set its properties.
     document.getElementById('map') --> Obtains the Google Map style definition
     from the div element with id="map" in ShowOnMap.xhtml 
     */
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: {lat: 37.227264, lng: -80.420745},
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.BOTTOM_LEFT
        },
        mapTypeId: google.maps.MapTypeId.HYBRID
    });

    // Show the desired map using the map created above by calling the display() function.
    display();

}

/*
 The Virginia Tech campus map created in the initializeMap() function above is used to show:
 (1) directions from one building to another on campus,
 (2) the location of a single VT building, or
 (3) locations of VT buildings in a given building category.
 */
function display() {
    /*
     document.getElementById("destinationName").value --> Obtains the name of the destination 
     VT building from the hidden input element with id="destinationName" in ShowOnMap.xhtml 
     */
    if (document.getElementById("photoLocation") !== null && document.getElementById("photoLocation").value !== '') {
        /*
         If destinationName has a value, the user asked for directions.
         Show directions on the VT campus map created in the initializeMap() function.
         */
        displayPhotoLocation();
    }
    else{
        console.log("got here");
        drawRoute();
    }

}

function displayPhotoLocation() {
    var latitude = document.getElementById('lat').innerHTML;
    var longitude = document.getElementById('lon').innerHTML;
    
    var latLong = new google.maps.LatLng(latitude, longitude);
    
    map.setCenter(latLong);
    
    currentMarker = new google.maps.Marker({
        title: '',
        position: latLong,
        map: map
    });
    
    currentMarker.setMap(map);
}


// Draws the route on map showing directions to go from one VT building to another
function drawRoute() {

    // Identify the VT campus map as the Map to display Directions on
    directionsDisplay.setMap(map);

    // Since the DirectionsRequest object must be of type 'literal', we convert lat and long numbers to String type.

    /******************************* Start Geolocation Determination *******************************/

    // Obtain the starting Latitude as String from the hidden input element with id="startLat" in ShowOnMap.xhtml
    var startingLatitudeAsString = document.getElementById("resultsForm:datalist:0:startLat").value.toString();

    // Obtain the starting Longitude as String from the hidden input element with id="startLong" in ShowOnMap.xhtml
    var startingLongitudeAsString = document.getElementById("resultsForm:datalist:0:startLong").value.toString();

    // Instantiate the starting geolocation object for obtaining directions FROM
    var startGeolocation = new google.maps.LatLng(startingLatitudeAsString, startingLongitudeAsString);
    console.log(startGeolocation.toString());

    /**************************** Destination Geolocation Determination ****************************/

    // Obtain the destination Latitude as String from the hidden input element with id="destinationLat" in ShowOnMap.xhtml
    var destinationLatitudeAsString = document.getElementById("resultsForm:datalist:0:destinationLat").value.toString();
    console.log(destinationLatitudeAsString);

    // Obtain the destination Longitude as String from the hidden input element with id="destinationLong" in ShowOnMap.xhtml      
    var destinationLongitudeAsString = document.getElementById("resultsForm:datalist:0:destinationLong").value.toString();
    console.log(destinationLongitudeAsString);

    // Instantiate the ending geolocation object for obtaining directions TO
    var endGeolocation = new google.maps.LatLng(destinationLatitudeAsString, destinationLongitudeAsString);
    console.log(endGeolocation.toString());

    /********************************** Travel Mode Determination **********************************/

    // Obtain the selected Travel Mode from the hidden input element with id="travelMode" in ShowOnMap.xhtml
    var selectedTravelMode = "DRIVING";

    /***************************** Directions Request Object Creation ******************************/

    // Create a DirectionsRequest object named 'request' with the following properties as key:value pairs
    var request = {
        origin: startGeolocation,
        destination: endGeolocation,
        travelMode: google.maps.TravelMode[selectedTravelMode]
    };

    console.log(request);
    /***************************** Obtaining and Displaying Directions *****************************/

    /*
     "To use directions in the Google Maps JavaScript API, create an object of type DirectionsService
     and call DirectionsService.route() to initiate a request to the Directions service, passing it a 
     DirectionsRequest object literal containing the input terms and a callback method to execute upon
     receipt of the 'response'." [Google]
     
     Values of the 'response' and 'status' parameters of the callback method are returned from the
     Google Maps Directions API.
     
     status   --> must be okay if the directions can be computed by the Google Maps Directions API
     response --> contains the requested directions
     */
    directionsService.route(request, function (response, status) {

        // The operator === tests for equal value and equal type
        if (status === google.maps.DirectionsStatus.OK) {

            // If DirectionsStatus is okay, then display the route for the directions on map
            directionsDisplay.setDirections(response);
        }
        else{
            directionsDisplay.setMap(null);
        }
    });
}
