var source = [];
var routeNumber;
var timeOfNextTrip;
/**
 * A function that is called when user press a key on #routeNumber element with as time
 * out of 500ms. Then, it calls RouteGuessingServlet to show all possible routes
 * that user may want to search.
 */
$(function() {
	$("#routeNumber").autocomplete({
		delay: 300,
		minLength: 1,
		source: function(request, response) {
			$.ajax({
				url: "RouteGuessingServlet",
				dataType: 'json',
				data: {
					routeName: $("#routeNumber").val()
				},
				success: function(data) {
					if(jQuery.isEmptyObject(data)) {
						alertInputNotExists("γραμμή");
						return response();
					}
					removeWarningMessage();
					var routes = {};
					for(var i = 0; i < data.names.length; i++) {
						routes[i] = data.names[i] + ", " + data.numbers[i];
					}
					response($.map(routes, function (value, key) {
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
			$("#routeNumber").val(ui.item.label);
			routeNumber = ui.item.label.split(", ")[1];
			checkRoute();
		},
		focus: function(event, ui) {
			event.preventDefault();
		}
	}).autocomplete("widget").addClass("autocompletion");
});

/**
 * A function that is called when user selects a route.
 * Then, it calls RouteFindingServlet to show route's
 * basic information.
 */
function checkRoute(route) {
	prepareCSS("route");
	$.ajax({
		url: "RouteFindingServlet",
		dataType: 'json',
		data: {
			routeNumber: routeNumber
		},
		success: function(data) {
			emptyRouteElements();
			showRouteInformation(data);
			chooseDay('Outbound');
			scrollToInfo();
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
	});
}

/**
 * It calls ServiceDaysServlet to show route's trip days.
 */
function chooseDay(dir) {
	$.ajax({
		url: "ServiceDaysServlet",
		dataType: 'json',
		data: {
			routeNumber: routeNumber,
			direction: dir
		},
		success: function(data) {
			selectDay(data);
			$("#leftDivInfo").empty();
			getTrips(dir);
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
	});
}

/**
 * It firstly finds the current day. Then it calls
 * RouteInformationServlet to show the trips.
 */
function getTrips(dir, serviceDay) {
	var nextTrip = false;
	if (serviceDay == undefined) {
		var weekDays = ["sunday", "monday", "tuesday", "wednesday", "thursday",
			"friday", "saturday"];
		var date = new Date();
		serviceDay = weekDays[getServiceDay(date.getDay())];
		nextTrip = true;
	}
	$.ajax({
		url: "RouteInformationServlet",
		data: {
			routeNumber: routeNumber,
			direction: dir,
			day: serviceDay,
			nextTrip: nextTrip
		},
		success: function(data) {
			if (jQuery.isEmptyObject(data)) alertRouteNoServices();
			else {
				if (nextTrip) {
					var sequenceNextTrip = data[data.length - 2];
					window.timeOfNextTrip = data[sequenceNextTrip];
				}
				showRouteTrips(data);
				$("#rightDivInfo").empty();
				getTripInformation(dir, data[data.length - 2], serviceDay);
			}
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
	});
}

/**
 * It firstly finds the current day. Then it calls
 * TripInformationServlet to show a trip's information.
 */
function getTripInformation(dir, i) {
	var selectedDay = $("#days input[type='radio']:checked").val();
	if (selectedDay == undefined) {
		var weekDays = ["sunday", "monday", "tuesday", "wednesday", "thursday",
			"friday", "saturday"];
		var date = new Date();
		selectedDay = weekDays[getServiceDay(date.getDay())];
	}
	$.ajax({
		url: "TripInformationServlet",
		data: {
			routeNumber: routeNumber,
			direction: dir,
			day: selectedDay,
			sequence: i
		},
		success: function(data) {
			map.drawRoute(data.coordinates);
			showTripInfo(data);
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}
	});
}

/**
 * It calls updateRoutes to delete a route from favourites.
 */
function deleteRouteFromFavourites(selected) {
	var route = [];
	if (selected != undefined)
		route = $("#route" + selected).val().split(", ");
	else {
		route.push($("#number").text());
		route.push($("#routeName").text());
	}
	$.ajax({
		url: "updateRoutes",
		data: {
			routeNumber: route[0],
			routeName: route[1],
			action: "deletion"
		},
		success: function() {
			changeButtonOfFavouriteStops(false, "Route");
			if (selected == undefined) {
				var rowToDelete = "route" + route[0];
				$("." + rowToDelete).remove();
			} else $(".route" + selected).remove();
		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}

	});
}

/**
 * It calls updateRoutes to add a route to favourites.
 */
function addRouteToFavourites() {
	var routeNumber = $("#number").text();
	var routeName = $("#routeName").text();
	$.ajax({
		url: "updateRoutes",
		data: {
			routeNumber: routeNumber,
			routeName: routeName,
			action: "addition"
		},
		success: function() {
			addRouteToList();
			changeButtonOfFavouriteStops(true, "Route");

		},
		error: function() {
			alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
		}

	});
}

/**
 * Gets the day of service until a route end its duty.
 *
 * For example all routes finish their service at 5:00 PM of the next day. So if
 * it 's Tuesday at 01:00, it's Monday service day for the routes.
 *
 * @param day current day.
 * @returns {*} service day of routes.
 */
function getServiceDay(day) {
	var MIDNIGHT = 0;
	var ONE_PM = 1;
	var TWO_PM = 2;
	var THREE_PM = 3;
	var FOUR_PM = 4;
	var date = new Date();
	var currentHour = date.getHours();
	if (currentHour == MIDNIGHT || currentHour == ONE_PM || currentHour == TWO_PM
		|| currentHour == THREE_PM  || currentHour == FOUR_PM)
		return day - 1;
	else return day;
}