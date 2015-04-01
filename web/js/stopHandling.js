/** coordinates of source stop. */
var source = [];

/** true if user gives an accepted name of source stop, false otherwise. */
var acceptedValueSource = false;

/**
 * A function that is called when user press a key on #name element with as time
 * out of 300ms. Then, it calls StopLocationsServlet to show all possible stops
 * that user may want to search.
 *
 * If user gives an input that it does not mach to any stop, system shows a
 * message to him that there is no stop marches witb the characters given in
 * input element.
 *
 * If user gives an input that it matches with a number of stops, system shows
 * these stops below input element and user can select one stop to complete his
 * search.
 *
 * User might searches for a stop for the calculation of optimal path between
 * this stop defined by this input element (which is the source stop) and another
 * (terminal point).In this case system shows the optimal path between these
 * two points in condition that a user has given an accepted input of a stop on
 * the input of terminal stop.
 */
$(function() {
	acceptedValueSource = false;
	$("#name").autocomplete({
		delay: 300,
		minLength: 2,
		source: function(request, response) {
			$.ajax({
				url: "locations",
				dataType: 'json',
				data: {
					stopName : $("#name").val()
				},
				success: function(data) {
					if(jQuery.isEmptyObject(data)) {
						alertInputNotExists("στάση");
						acceptedValueSource = false;
						return response();
					}
					removeWarningMessage();
					var stops = {};
					for(var i = 0; i < data.locations.length; i++) {
						var keyLocations = data.stop[i] + ", " + data.locations[i];
						var coordinates = data.coordinates[i][0] +
							", " + data.coordinates[i][1];
						stops[coordinates] = keyLocations;
					}
					response($.map(stops, function (value, key) {
						return {
							label: value,
							value: key
						};
					}));
				},
				error: function() {
					alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
				}

			});
		},
		select: function(event, ui) {
			event.preventDefault();
			acceptedValueSource = true;
			source = ui.item.value.split(", ");
			var destinationInput = $("#terminal");
			$("#name").val(ui.item.label);
			if(!destinationInput.is(":visible")) getCoordinates(ui.item.value);
			else {
				if(!acceptedValueTerminal) destinationInput.focus();
				else {
					var optimalPath = new OptimalPath(source, target);
					if(pathConfigured)
						optimalPath.configureCalculationOfOptimumPath($("#dialog " +
							"form #radio input[name='radio']:checked").val(),
							$("#hour").val(), $("#minute").val(),
							$("#transport").val());
					optimalPath.calculateOptimumPath();
					$("#leftDivInfo").show();
					scrollToInfo();
				}
			}
		},
		focus: function(event) {
			event.preventDefault();
		}
	}).autocomplete("widget").addClass("autocompletion");
});

/**
 * Creates a request to StopInformationServlet.java via AJAX. Gets data in JSON
 * format and then changes HTML page accordingly.
 *
 * More specifically, it takes the coordinates of a stop as parameter and with
 * data got by the response of the request it presents this info. Moreover,
 * mark this stop to the map via Google Maps API.
 *
 * @param selectedStop coordinates of stop
 */
function getCoordinates(selectedStop) {
	var ACCEPTED_LENGTH = 2;
	var stop = selectedStop.split(", ");
	if(stop.length != ACCEPTED_LENGTH)
		return alertInputNotExists();
	$.ajax({
		url: "/ismgroup26/coordinates",
		dataType: 'json',
		data: {
			latitude : stop[0],
			longitude: stop[1]
		},
		success: function(data) {
			if (data.length == 0) alertInputNotExists();
			else {
				var isFavourite = data[0][0].isFavourite;
				scrollToInfo();
				showStopRoutes(data);
				map.markStops(stop[0], stop[1]);
				map.getAddress(stop[0], stop[1], isFavourite);

			}
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
		
	});
}

/**
 * Creates a request to AllStopCoordinatesServlet.java via AJAX. Gets data in JSON
 * format and then shows on map the nearest stops to the selected point by user.
 *
 * More specifically, it draws a circle on Map with diameter specified by the
 * parameter and then system test for all existing stops if are in the circle.
 *
 * @param diameter diameter of circle in meters.
 */
function showNearestStops(diameter) {
	map.drawCircle(diameter);
	$.ajax({
		url: "/ismgroup26/allCoordinates",
		dataType: 'json',
		data: {
		},
		success: function(data) {
			map.showNearestStops(data);
			alertToSelect();
			scrollToInfo();
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
		
	});
}

/**
 * Takes coordinates of two points(one is source point, and one is target point)
 * and calculates optimal path between these two points giving direction via
 * Google Maps API.
 */
function calculateOptimumPath() {
	var ACCEPTED_LENGTH = 2;
	var sourceCoordinates = $("#name").val().split(", ");
	var targetCoordinates = $("#terminal").val().split(", ");
	if (!(sourceCoordinates.length != ACCEPTED_LENGTH
		|| targetCoordinates.length != ACCEPTED_LENGTH))
		map.calculateOptimumPath(sourceCoordinates, targetCoordinates);
}

/**
 * Creates an AJAX request to UpdateStopListServlet to delete a selected stop
 * from the list of favourites stops of a user. It can be done with two ways.
 *
 * Firstly, user can delete a stop from his list from the dialog box of favourite
 * stop given selected parameter which is a unique id for each stop specified
 * in onclick attribute of each deletion button in order to delete every HTML
 * element associated with the stop user wants to delete.
 *
 * Secondly, user can delete stop from button of the basic information box which
 * is appeared when a search for a specific stop is done. Then no parameter is given to this
 * function the stop is defined by the coordinates which are shown in the basic
 * information box mentioned above.
 *
 * @param selected unique id for each stop to define which HTML elements must
 * be deleted which are associated with the deleted stop.
 */
function deleteStopFromFavourites(selected) {
	var coordinates = [];
	if (selected != undefined)
		coordinates = $("#stop" + selected).val().split(", ");
	else {
		coordinates.push($("#lat").text());
		coordinates.push($("#lng").text());
	}
	$.ajax({
		url: "/ismgroup26/updateStop",
		data: {
			latitude: coordinates[0],
			longitude: coordinates[1],
			action: "deletion"
		},
		success: function() {
			changeButtonOfFavouriteStops(false, "Stop");
			if (selected == undefined) {
				var rowToDelete = $("#stops").find("input[value = '"
					+ coordinates[0] + ", " + coordinates[1] + "']")
					.attr("class").split(" ")[0];
				$("." + rowToDelete).remove();
			} else $(".stop" + selected).remove();
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}

	});
}

/**
 * Creates an AJAX request to UpdateStopListServlet to add a selected stop
 * to the list of favourites stops of a user. The way it can be done it is described
 * below.
 *
 * User can add a stop to his list of favourites by clicking a button (with
 * the condition that stop is not already to the list of his favourites) which is
 * appears in the basic information box when the search of a stop is finished.
 */
function addStopToFavourites() {
	var coordinates = [];
	var address = $("#address").text();
	coordinates.push($("#lat").text());
	coordinates.push($("#lng").text());
	$.ajax({
		url: "/ismgroup26/updateStop",
		data: {
			latitude: coordinates[0],
			longitude: coordinates[1],
			address: address,
			action: "addition"
		},
		success: function() {
			addStopToList();
			changeButtonOfFavouriteStops(true, "Stop");

		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}

	});
}

/**
 * Function that configures optimal path between two points. It gets the values
 * of dialog box inputs to determine whe way optimal path is configured.
 *
 * For example, calculation of optimal path might be based on the departure time
 * from source point or might be based on the arrival time to the destination point.
 * Moreover, it is determined the mean of transport user is going to user to reach
 * destination, such as urban transit, car, walking.
 *
 * Then if the inputs of both source and terminal stop are accepted system
 * shows the optimal path between these two points and gives instructions to user
 * the way to reach his destination.
 */
function parameterizeOptimumPath() {
	var calculationWay = $("#dialog form #radio input[name='radio']:checked").val();
	var hour = $("#hour").val();
	var minute = $("#minute").val();
	var meanOfTransport = $("#transport").val();
	pathConfigured = true;
	if (acceptedValueSource && acceptedValueTerminal) {
		var optimalPath = new OptimalPath(source, target);
		optimalPath.configureCalculationOfOptimumPath(calculationWay, hour,
			minute, meanOfTransport);
		optimalPath.calculateOptimumPath();
		scrollToInfo();
	}
}

/**
 * Creates an AJAX request to the StopRoutesServlet to get the list of the routes
 * which pass through a specific stop specified by the parameters.
 *
 * Then creates a Google Maps info window for that stop with the list of the routes.
 *
 * @param latitude latitude of the stop.
 * @param longitude longitude of the stop.
 * @param serviceDay day of week to search for which routes pass throut the specific
 * route. If it is not defined, today is selected as a service day by the system.
 */
function getStopRoutes(latitude, longitude, serviceDay) {
	if (serviceDay == undefined) {
		var weekDays = ["sunday", "monday", "tuesday", "wednesday", "thursday",
			"friday", "saturday"];
		var date = new Date();
		serviceDay = weekDays[date.getDay()];
	} else serviceDay = $("#days input[type='radio']:checked").val();
	$.ajax({
		url: "/ismgroup26/routes",
		data: {
			latitude: latitude,
			longitude: longitude,
			serviceDay: serviceDay
		},
		success: function(data) {
			window.map.stopInfo(latitude, longitude,
				createInfoWindowContent(latitude, longitude, data.name,
					data.number));

		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}

	});
}