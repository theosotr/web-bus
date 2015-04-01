/**
 * A class that defines a map to be displayed via the Google Maps API
 *
 * @param latitude latitude of map's center
 * @param longitude longitude of map's center
 * @constructor Initializes Map with the given latitude and longitude
 */
function Map(latitude, longitude) {
	/** latitude of map's center. */
	this.latitude = latitude;

	/** longitude of map's center. */
	this.longitude = longitude;

	/**
	 * circle used to define the area that includes the nearest stops of a
	 * point - stop.
	 */
	this.circle = null;

	/** Path of a route depicted to map with polyline. */
	this.polyline = null;

	/** An array of markers of stop that are depicted to map */
	this.markers = [];

	/** Map object of Google Maps API. */
	this.map = null;

	this.displayBlankMap();
}

/**
 * Creates a Map whose center is the center of Athens and it also displays
 * Metro lines of Athens.
 */
Map.prototype.displayBlankMap = function() {
	var mapOptions = {
		zoom: 13,
		center: new google.maps.LatLng(this.latitude, this.longitude)
	};
	this.map = new google.maps.Map(document.getElementById('map-canvas'),
		mapOptions);
	var transitLayer =  new google.maps.TransitLayer();
	transitLayer.setMap(this.map);
};

/**
 * Adds a marker in map and it colors it accordingly if it's a point selected
 * by user or not.
 *
 * More specifically, green marker is the selected stop by user and the stop
 * which system uses to find nearest stops related to it. Red marker is for
 * other stops.
 *
 * @param location location of marker
 * @param selected it is selected by user or not
 * @returns {google.maps.Marker} marker
 */
Map.prototype.addMarker = function(location, selected) {
	var url;
	if(selected)
		url = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
	else
		url = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
	var marker = new google.maps.Marker({
		position: location,
		map: this.map,
		title: "Πατήστε πάνω",
		icon: url
	});
	this.markers.push(marker);
	return marker;
};

/**
 * Delete all markers of map
 */
Map.prototype.deleteMarkers = function() {
	for(var i = 0; i < this.markers.length; i++)
		this.markers[i].setMap(null);
	this.markers = [];
};

/**
 * Mark a point specified by the given latitude and longitude
 *
 * @param latitude latitude of point to be marked
 * @param longitude longitude of point to be marked
 */
Map.prototype.markStops = function(latitude, longitude) {
	this.deleteMarkers();
	this.deleteCircle();
	this.setCoordinates(latitude, longitude);
	this.refreshMap();
	var location = new google.maps.LatLng(parseFloat(this.latitude),
		parseFloat(this.longitude));
	var marker = this.addMarker(location, true);
	this.map.setZoom(17);
	this.map.setCenter(location);

};

/**
 * Draw a circle in map that defines the area that system is going to be
 * looking for stops.
 *
 * @param radius radius of circle in meters.
 */
Map.prototype.drawCircle = function(radius) {
	this.deleteCircle();
	this.refreshMap();
	var location = new google.maps.LatLng(parseFloat(this.latitude),
		parseFloat(this.longitude));
	var circleInfo = {
		strokeColor: '#FF0000',
		strokeOpacity: 0.6,
		strokeWeight: 2,
		fillColor: '#FF0000',
		fillOpacity: 0.35,
		map: this.map,
		center: location,
		radius: parseInt(radius)

	};

	this.circle = new google.maps.Circle(circleInfo);
	this.circle.setMap(this.map);
	this.map.setZoom(14);

};

/**
 * Draws the path of route on map based on the knowledge of the location of all stops
 * where route passes through.
 *
 * @param coordinates An array of coordinates (latitude and longitude) of all
 * stops where route passes through.
 */
Map.prototype.drawRoute = function(coordinates) {
	var LATITUDE_INDEX = 0;
	var LONGITUDE_INDEX = 1;
	this.deletePolyline();
	this.deleteMarkers();
	this.refreshMap();
	var location = new google.maps.LatLng(parseFloat(coordinates[0][LATITUDE_INDEX]),
		parseFloat(coordinates[0][LONGITUDE_INDEX]));
	var flightPlanCoordinates = [];
	for(var i = 0; i < coordinates.length; i++) {
		flightPlanCoordinates.push(new google.maps.LatLng(parseFloat(coordinates[i][LATITUDE_INDEX]),
			parseFloat(coordinates[i][LONGITUDE_INDEX])));
	}
	this.polyline = new google.maps.Polyline({
		path: flightPlanCoordinates,
		geodesic: true,
		strokeColor: 'blue',
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	this.map.setZoom(12);
	this.map.setCenter(location);
	this.polyline.setMap(this.map);
};

/**
 * Get analytical address of a location using Google Maps Geocoding.
 *
 * Based on the result of Geocoding, system creates the basic information box
 * that includes information of stop such as name of stop, coordinates of stop,
 * address of stop.
 *
 * @param latitude latitude of point.
 * @param longitude longitute of point.
 * @param isFavourite true if stop is in the list of user favourite stops,
 * false otherwise. It used to create a button to delete stop from favourite
 * stops if stop is already favourite for user, or to create a button to add stop
 * to favourites if stop is not in the list of user's favourites.
 */
Map.prototype.getAddress = function(latitude, longitude, isFavourite) {
	this.setCoordinates(latitude, longitude);
	var geocoder;
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(this.latitude, this.longitude);
	geocoder.geocode({'latLng': latlng}, function(results, status) {
		if(status == google.maps.GeocoderStatus.OK) {
			if (results[1]) {
				showBasicInformation(results[0].formatted_address.split(", ")[0],
					results[1].formatted_address, isFavourite);
			}
			else {
				alert("No results");
			}
		} else {
			alert("Geocoding unsuccessful: Status " + status);
		}
	});
};

/**
 * Mark all stops that are inside of the circle.
 *
 * @param coordinates An array of coordinates of all stops to be tested if that
 * are included in the circle or not.
 */
Map.prototype.markMutipleStops = function(coordinates) {
	var LATITUDE_INDEX = 0;
	var LONGITUDE_INDEX = 1;
	this.deleteMarkers();
	for(var i = 0; i < coordinates.length; i++) {
		var location = new google.maps.LatLng(parseFloat(coordinates[i][LATITUDE_INDEX]),
			parseFloat(coordinates[i][LONGITUDE_INDEX]));
		var marker;
		if(coordinates[i][LATITUDE_INDEX] != this.latitude)
			marker = this.addMarker(location, false);
		else
			marker = this.addMarker(location, true);
		google.maps.event.addListener(marker, 'click', (function(map, marker,
																 i, coordinates) {
			return function() {
				$.getScript("js/stopHandling.js", function() {
					getCoordinates(coordinates[i][LATITUDE_INDEX] + ", "
						+ coordinates[i][LONGITUDE_INDEX]);
				});
			}
		})(this, marker, i, coordinates));
	}
};

/**
 * Select coordinates that belong to circle
 *
 * @param stops all coordinates of stops which are stored to database
 */
Map.prototype.showNearestStops = function(stops) {
	var LATITUDE_INDEX = 0;
	var LONGITUDE_INDEX = 1;
	var coord = [];
	var stop = [];
	for(var i = 0; i < stops.coordinates.length; i++) {
		var location = new google.maps.LatLng(stops.coordinates[i][LATITUDE_INDEX],
			stops.coordinates[i][LONGITUDE_INDEX]);
		if(this.circle.getBounds().contains(location)) {
			stop.push(stops.stop[i]);
			coord.push(stops.coordinates[i]);
		}

	}
	this.markMutipleStops(coord);
	this.map.setZoom(16);
	this.map.setCenter(this.circle.getCenter());

};

/**
 * Delete circle of map
 */
Map.prototype.deleteCircle = function() {
	if(this.circle != null)
		this.circle.setMap(null);
};

/**
 * Mark a stop on map specified by the parameters (latitude and longitude) and
 * then creates a Google Maps info window with the list of a routes which pass
 * through this stop. Content of info window is specified by the infoContent
 * parameter.
 *
 * Info window opens, when user clicks on the marker of stop.
 *
 * @param latitude latitude of stop to be marked.
 * @param longitude longitude of stop to be marked.
 * @param infoContent content of stop's info window.
 */
Map.prototype.stopInfo = function(latitude, longitude, infoContent) {
	var MARKER_CREATED_INDEX = 0;
	this.markStops(latitude, longitude);
	var infowindow = new google.maps.InfoWindow({
		content: infoContent
	});
	var marker = this.markers[MARKER_CREATED_INDEX];
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(this.map, marker);
	});
};

/**
 * Refresh content of map.
 */
Map.prototype.refreshMap = function() {
	this.displayBlankMap();
}

/**
 * Delete polyline of map
 */
Map.prototype.deletePolyline = function() {
	if(this.polyline != null)
		this.polyline.setMap(null);
};

/**
 * Set new coordinates to the Map Object
 *
 * @param latitude latitude
 * @param longitude longitude
 */
Map.prototype.setCoordinates = function(latitude, longitude) {
	this.latitude = latitude;
	this.longitude = longitude;
};


OptimalPath.prototype = Object.create(Map.prototype);
OptimalPath.prototype.constructor = OptimalPath;

/**
 * OptimalPath class defines the optimal path between two points (source and
 * target).Includes both the depiction of path in map and the analytical
 * instructions of how to reach destination. It uses the Google Maps Api services.
 * Moreover, it extends Map class.
 *
 * @param source An array of source point's coordinates (latitude, longitude).
 * @param target An array of target point's coordinates (latitude, longitude).
 *
 * @constructor Initializes OptimalPath class by defining the location of both
 * source point and target point. Moreover, it calls the constructor of superclass
 * to refresh the map content, initializes the DirectionService and
 * DirectionDisplay objects and the HTML Div where the analytical instructions
 * of how to reach destination are displayed.
 */
function OptimalPath(source, target) {
	var LATITUDE_INDEX = 0;
	var LONGITUDE_INDEX = 1;
	initializeInstructions();
	prepareCSS("optimalPath");
	this.source = new google.maps.LatLng(source[LATITUDE_INDEX], source[LONGITUDE_INDEX]);
	this.target = new google.maps.LatLng(target[LATITUDE_INDEX], target[LONGITUDE_INDEX]);
	Map.call(this, source[0], source[1]);
	this.directionService = new google.maps.DirectionsService();
	this.directionsDisplay = new google.maps.DirectionsRenderer();
	this.directionsDisplay.setPanel(document.getElementById('leftDivInfo'));
	this.directionsDisplay.setMap(this.map);
	this.control = document.getElementById('control');
	this.control.style.display = 'block';
	this.map.controls[google.maps.ControlPosition.TOP_CENTER].push(control);
	this.defaultConfiguration();
}

/**
 * Calculates optimal path between two points via Google Maps API services.
 * Path between these two points and instruction are displayed.
 */
OptimalPath.prototype.calculateOptimumPath = function() {
	var request = this.initializeRequestForPath(this.source, this.target);
	var directions = this.directionsDisplay;
	this.directionService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			removeWarningMessage();
			directions.setDirections(response);
		} else {
			alertInputNotExists("Διαδρομή");
		}
	});
};

/**
 * Gets the means of transport in order optimal path between two points
 * to be calculated in the calculateOptimumPath method.
 */
OptimalPath.prototype.getTravelMode = function() {
	switch (this.meanOfTransport) {
		case "TRANSIT":
			return google.maps.TravelMode.TRANSIT;
		case "DRIVING":
			return google.maps.TravelMode.DRIVING;
		default :
			return google.maps.TravelMode.WALKING;
	}
};

/**
 * Initializes request in order to search for the optimal path using the Google Maps
 * API. It is specified by the origin point, the destination point, the mean of
 * transport is going to be used and desirable time user might want to begin or
 * end his trip.
 */
OptimalPath.prototype.initializeRequestForPath = function() {
	var request;
	var date = new Date();
	var YEAR = date.getUTCFullYear();
	var MONTH = date.getUTCMonth() + 1;
	var DAY = date.getUTCDate();
	var SECONDS = 0;
	alert(YEAR);
	alert(MONTH);
	alert(DAY);
	if (this.arrivalTime == "true")
		request = {
			origin: this.source,
			destination: this.target,
			travelMode: this.getTravelMode(this.meanOfTransport),
			transitOptions: {
				arrivalTime: new Date(YEAR, MONTH, DAY, this.hour,
					this.minute, SECONDS)
			}
		};
	else
		request = {
			origin: this.source,
			destination: this.target,
			travelMode: this.getTravelMode(this.meanOfTransport),
			transitOptions: {
				departureTime: new Date(YEAR, MONTH, DAY, this.hour,
					this.minute, SECONDS)
			}
		};
	return request;
};

/**
 * Default configuration for the calculation of Optimum Path. Calculation of
 * optimal path between two points by default is configures, with urban
 * transit as the selected mean of transport to reach destination, calculation
 * based on the departure time which is the time when method is called.
 */
OptimalPath.prototype.defaultConfiguration = function() {
	var date = new Date();
	this.arrivalTime = false;
	this.meanOfTransport = "TRANSIT";
	this.hour = date.getHours();
	this.minute = date.getMinutes();
};

/**
 * Calculation of optimal path between two points is configured by user.
 *
 * More specifically, user selects the way calculation will be based on
 * (either based on arrival time or departure time), selects the arrival/departure
 * time giving departure/arrival hour and departure/arrival minute and selects
 * the mean of transport he wants to use to reach destination from the list of
 * the available ways (urban transit, car, walking).
 *
 * @param calculationWay Calculation based on arrivalTime if true, calculation
 * based of departure time otherwise.
 * @param hour Arrival/Departure hour.
 * @param minute Arrival/Departure minute.
 * @param meanOfTransport Selected mean of transport.
 */
OptimalPath.prototype.configureCalculationOfOptimumPath = function(calculationWay,
		hour, minute, meanOfTransport) {
	this.arrivalTime = calculationWay;
	this.meanOfTransport = meanOfTransport;
	this.hour = hour;
	this.minute = minute;
};
