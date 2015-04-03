/**
* A function that make registration form visible or invisible
* according to what is its current state.
*/
function editRegistrationForm() {
	var form = $("#registration");
	if(form.is(":visible")) form.hide();
	else {
		form.show();
		scrollToRegistrationForm();
	}
}

/**
 * Jump to the registration form.
 */
function scrollToRegistrationForm() {
	var MILLI_SECONDS = 2000;
	$('html, body').animate({
		scrollTop: $("#registrationForm").offset().top
	}, MILLI_SECONDS);
}

/**
 * Alerts new distance to search for nearest stops if it is changed by user.
 */
function alertDistance(distance) {
	$("#distance").text("Απόσταση " + distance + "μ");
}

/**
 * Shows a message to user to click to one stop on map in order to see more
 * information about stop.
 */
function alertToSelect() {
	$("#warning").remove();
	$("#buttons").append("<div id ='warning' class='ui-widget'>"
			+ "<div class='ui-state-error ui-corner-all' style='margin-top: "
			+ "20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-alert' "
			+ "style='float: left; margin-right: .3em;'></span><strong>Προσοχή!"
			+ "</strong> Πατήστε πάνω σε μια στάση για να δείτε περισσότερες "
			+ "πληροφορίες.</p></div></div>");
}

/**
 * Empty selected HTML elements.
 */
function removeInitialInfo() {
	$("#indiv").empty();
}

/**
 * Remove selected HTML element from page.
 */
function removeWarningMessage() {
	$("#warningMessage").remove();
}

/**
 * Shows a message to user that input given as parameter does not exists.
 *
 * Input might be a stop. In this case message is appeared when user gives input
 * as stop that does not match with the name of any stop in database.
 *
 * Input might be a route. In this case message is appeared when user gives input
 * as route that does not match with the name or number of any route in database.
 *
 * Input might be a path. In this case message is appeared when Google Maps API
 * Services did not manage to find any path between two specific locations with
 * the configuration given by user with.
 */
function alertInputNotExists(input) {
	removeWarningMessage();
	if (input == "διαδρομή") {
		removeInstructions();
		prepareCSS("switchMode");
	}
	$("#searchBar").append("<div id='warningMessage' class='ui-widget'>"
			+ "<div class='ui-state-error ui-corner-all' style='padding: 0 .7em;'>"
			+ "<p><span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'>"
			+ "</span><strong>Προσοχή:</strong>Δεν υπάρχει η "
			+ input + " που πληκτρολογείτε.</p></div> </div>");
}

/**
 * Change the button to delete/add a route/stop from the list of user's favourites.
 * If user has just added a stop/route in his list of favourites, then button
 * changes to a button to delete the specific stop/routes from his list of
 * favourites.
 *
 * If user has just deleted a stop/route from his list of favourites then button
 * changes to a button to add the specific stop/route to this list of favourites.
 *
 * @param isFavourite true if stop/route is in user's list of favourites, false
 * otherwise.
 * @param input defines if button associated with either a stop or route.
 */
function changeButtonOfFavouriteStops(isFavourite, input) {
	var buttonIcon = $("#favourite");
	var icon;
	var message;
	var onclickFunction;
	if (isFavourite) {
		icon = "minus";
		message = "Αφαίρεση από τις αγαπημένες";
		onclickFunction = "delete" + input + "FromFavourites()";
	} else {
		icon = "plus";
		message = "Προσθήκη στις αγαπημένες";
		onclickFunction = "add" + input + "ToFavourites()";
	}
	buttonIcon.attr("onclick", onclickFunction);
	buttonIcon.button({
		icons: {primary: "ui-icon-circle-" + icon},
		label: message
	});

}

/**
 * Add stop to dialog box which includes the list of favourite stops of an user.
 */
function addStopToList() {
	var id = parseInt(Math.random().toString().split(".")[1]);
	var counter = $("#stops button").length;
	var listEmpty = 0;
	var address = $("#address").text();
	var stopName = $("#nameStop").text();
	var coordinates = $("#lat").text() + ", " + $("#lng").text();
	if (counter == listEmpty) {
		$("#favouriteStops p").remove();
		$("#favouriteStops").append("<p>Λίστα αγαπημένων στάσεων<br>Επέλεξε μία " +
			"από τις παραπάνω στάσεις</p><div id='stops'></div>");
	}
	var favouriteStopOptions = $("#stops");
	favouriteStopOptions.append("<input class='stop" + id + "' type='radio' " +
		"id='stop" + id + "' name='favouriteStop' " +
		"value='" + coordinates + "'>" +
		"<label for='stop" + id + "' style='width: 25em; font-size: 11px;' class='stop" + id + "'>" +
		 stopName + ", " + address + "</label>" +
		"<button class='stop" + id + "' onclick='deleteStopFromFavourites(" + id + ")'" +
		" style='width: 3em;'>" +
		"<span class='ui-icon ui-icon-circle-minus' style='float: left;'></span>" +
		"</button><br class='stop" + id + "'>");
	favouriteStopOptions.buttonset();
}

/**
 * Add route to the dialog box which includes the list of favourite routes of an
 * user.
 */
function addRouteToList() {
	var counter = $("#routes button").length;
	var listEmpty = 0;
	var routeNumber = $("#number").text();
	var routeName = $("#routeName").text();
	var method = "deleteRouteFromFavourites(\"" + routeNumber + "\")";
	if (counter == listEmpty) {
		$("#favouriteRoutes p").remove();
		$("#favouriteRoutes").append("<p>Λίστα αγαπημένων στάσεων<br>Επέλεξε μία " +
		"από τις παραπάνω στάσεις</p><div id='routes'></div>");
	}
	var favouriteRouteOptions = $("#routes");
	favouriteRouteOptions.append("<input class='route" + routeNumber + "' type='radio' " +
		"id='route" + routeNumber + "' name='favouriteRoute' " +
		"value='" + routeNumber + ", " + routeName + "'>" +
		"<label for='route" + routeNumber + "' style='width: 25em;' class='route" + routeNumber + "'>" +
		routeNumber + ", " + routeName + "</label>" +
		"<button class='route" + routeNumber + "' onclick='" + method + "'" +
		" style='width: 3em;'>" +
		"<span class='ui-icon ui-icon-circle-minus' style='float: left;'></span>" +
		"</button><br class='route" + routeNumber + "'>");
	favouriteRouteOptions.buttonset();
}

/**
 * Creates the basic information box which describes a stop when a search is
 * done. More analytically, it presents information such as the coordinates
 * (latitude and longitude), name of stop, address of stop.
 *
 * Apart from this, it creates a jQuery UI slider in order user can define the
 * distance system will looking for nearest stops to the initial one by clicking
 * jQuery button which is also created in this function.
 *
 * If a user is connected to the system, then function creates a button to
 * add/delete stop from his list of favourites, accordingly if stops is in the
 * list or not. If user is a guest then button mentioned above is not created.
 *
 * @param divInfo div that contains all HTML elements that present all above.
 * @param stop name of stop.
 * @param address address of stop.
 * @param button HTML code in String format to create button to add/delete stop
 * from the list of favourites.
 */
function createInitialStopInfoBox(divInfo, stop, address, button) {
	var initialDistance = 500;
	divInfo.append("<p style='text-align: center;'><span class='ui-icon ui-icon-info' "
		+ "style='float: left; margin-right: .3em;'></span>" + button + "&nbsp;" +
		"<b>Συντεταγμένες: </b><span id = 'lat'>" + map.latitude +
		"</span>, <span id = 'lng'>" + map.longitude + "</span>&nbsp;<b>Όνομα στάσης: " +
		"</b><span id='nameStop'>" + stop + "</span>&nbsp;<b>Τοποθεσία: </b>" +
		"<span id = 'address'>" + address + "</span></p></div></div>");
	$("#stopInfo").append("<div id = 'buttons'></div>");
	var distanceButtons = $("#buttons");
	distanceButtons.append("<p style='text-align: center;'>Δείτε τις κοντινές " +
		"στάσεις επιλέγοντας απόσταση σείροντας το slider." +
		"<br><span id='distance'>Απόσταση " + initialDistance + "μ</span></p><div id='slider'></div>" +
		"<p style='text-align: center;'><button id='nearestStops' onclick='" +
		"showNearestStops(" + initialDistance + ")'>Αναζήτηστε κοντινές στάσεις</button></p>");
}

/**
 * Shows basic information about a stop such as location, coordinates and its name
 *
 * @param stop stop name
 * @param address stop's address
 * @param isFavourite true if stops is in the list of user favourite stops, false
 * otherwise. It it used to initialize button accordingly. For example, if stop
 * is one the user's favourite then a button is created to remove it, if user
 * wants. If method is called by a guest request then no button is created.
 */
function showBasicInformation(stop, address, isFavourite) {
	removeWarningMessage();
	removeInitialInfo();
	var parentDiv = $("#indiv");
	parentDiv.append("<div id='infoStop' class='ui-widget'><div id='stopInfo' "
		+ "class='ui-state-highlight ui-corner-all' style='margin-top: "
		+ "20px; padding: 0 .7em;'></div></div>");
	var divInfo = $("#stopInfo");
	var button = "";
	if (isFavourite != undefined) {
		var icon;
		var message;
		var onclickFunction;
		if (isFavourite) {
			icon = "minus";
			message = "Αφαίρεση από τις αγαπημένες";
			onclickFunction = "deleteStopFromFavourites()";
		} else {
			icon = "plus";
			message = "Προσθήκη στις αγαπημένες";
			onclickFunction = "addStopToFavourites()";
		}
		button = "<button id='favourite' onclick='" + onclickFunction +
			"' style='font-size: 11px;'>" +
			"" + message + "</button>";
	}
	createInitialStopInfoBox(divInfo, stop, address, button);
	$("#favourite").button({
		icons: {primary: "ui-icon-circle-" + icon},
		text: true
	});
	$("#nearestStops").button();
	$( "#slider" ).slider({
		range: "max",
		min: 100,
		max: 1000,
		value: 500,
		slide: function( event, ui ) {
			alertDistance(ui.value);
			$("#nearestStops").attr("onclick",
				"showNearestStops(" + ui.value + ")");
		}
	});
}

/**
 * Creates a table of next arrivals. To be more specific table presents the list
 * of next arrival of routes that pass through a stop within 30 minutes. It is a
 * table with four columns. One is the column of number of a route, the second
 * one is the column of the name of a route, the third one is the column of
 * the direction of route's trip and the last one is the estimated total minutes
 * needs to be taken in order route arrives.
 *
 * When no arrival is calculated within 30 minuted, then a div with message to
 * user is appeared.
 *
 * @param rows HTML code to create table
 */
function showNextArrivalsTable(rows) {
	var infoDiv = $("#leftDivInfo");
	infoDiv.append("<h6><b>Επόμενες αφίξεις</b></h6>");
	if(rows == "")
		infoDiv.append("<div class='ui-widget'>"
		+ "<div class='ui-state-error ui-corner-all' style='padding: 0 .7em;'>"
		+ "<p><span class='ui-icon ui-icon-alert' style='float: left; "
		+ "margin-right: .3em;'></span><strong>Προσοχή:</strong> "
		+ "Δεν υπάρχουν επόμενες αφίξεις στην συγκεκριμένη στάση για τα "
		+ "επόμενα 30'.</p></div></div>");
	else
		infoDiv.append("<table id = 'leftInfo'>" +
			"<tr><th>Αριθμός γραμμής</th>" +
			"<th>Ονομασία γραμμής</th>" +
			"<th>Κατεύθυνση</th>" +
			"<th>Χρόνος διέλευσης</th></tr>" +
			rows + "</table>");
}

/**
 * Scroll page to the selected Element
 */
function scrollToInfo() {
	var MILLI_SECONDS = 2000;
	$('html, body').animate({
		scrollTop: $("#searchBar").offset().top
	}, MILLI_SECONDS);
}

/**
 * Creates table of Stop Information which it presents stop's next arrivals and
 * the list of routes which pass from it. Table with the list of routes which
 * pass through stop includes five columns.
 *
 * First column is the number of a route, the second one is the name of a route,
 * the third one is the direction of a route's trip, the fourth one is the time
 * when the first trip of route in the day pass through and the last one is the
 * time when that last trip of a route in pass through.
 *
 * @param data content of table
 */
function showStopRoutes(data) {
	var infoDiv = $("#leftDivInfo");
	infoDiv.empty();
	infoDiv.append("<h4>Πληροφορίες στάσης</h4>");
	infoDiv.append("<h6><b>Λίστα γραμμών</b></h6>");
	infoDiv.append("<table id = 'leftInfo'>" +
							"<tr><th>Αριθμός γραμμής</th>" +
							"<th>Ονομασία γραμμής</th>" +
							"<th>Κατεύθυνση</th>" +
							"<th>Πρώτη άφιξη</th>" +
							"<th>Τελευταία άφιξη</th></tr></table>");
	var tableOfNextArrivals = "";
	for(var i = 0; i < data[0].length; i++) {
		$("#leftInfo").append("<tr><td>" + data[0][i].route[0] + "</td>" +
							  "<td>" + data[0][i].route[1] + "</td>" +
							  "<td>" + data[0][i].direction + "</td>" +
							  "<td>" + data[0][i].firstArrival + "</td>" +
							  "<td>" + data[0][i].lastArrival + "</td></tr>");
		if(data[0][i].nextArrivals.length >= 1) {
			var routeNumber = "<tr><td>" + data[0][i].route[0] + "</td>";
			var routeName = "<td>" + data[0][i].route[1] + "</td>";
			var direction = "<td>" + data[0][i].direction + "</td>";
			for(var j = 0; j < data[0][i].nextArrivals.length; j++)
				tableOfNextArrivals = tableOfNextArrivals + routeNumber + 
				routeName + direction + "<td>" + data[0][i].nextArrivals[j] +"'" 
				"</td></tr>";
		}
	}
	
	showNextArrivalsTable(tableOfNextArrivals);
	prepareCSS("default");
}

/**
 * A function that switches mode when user is searching based on stop. If optimum
 * path mode is on user has to give two stops( one is the beginning point, and one
 * is the terminal point) and then system calculates the optimal path between these
 * two points via Google Maps API. If optimum path mode is off, then user has to
 * give one stop and system shows all essential information about this specific
 * stop.
 *
 * @param mode true if optimum path mode is on, false otherwise
 */
function optimumPathMode(mode) {
	if(mode)
		initializeOptimumPathModeLayout();
	else
		removeOptimumPathModeLayout();
}

/**
 * Creates all essential HTML elements when optimum path mode is on
 */
function initializeOptimumPathModeLayout() {
	removeInitialInfo();
	$("#warningMessage").remove();
	$("#name").attr("placeholder", "Στάση Αφετηρία");
	$("#terminal").show();
	$("#openDialog").show();
	$(".onoffswitch-checkbox").attr("onclick", "optimumPathMode(false)");
	$("#leftDivInfo").empty();
	prepareCSS("switchMode");
	window.map = new Map(37.979946, 23.727801);
}

/**
 * Removes all HTML elements from DOM which are associated with optimum path mode
 * when is on.
 */
function removeOptimumPathModeLayout() {
	var inputSource = $("#name");
	$("#warningMessage").remove();
	inputSource.attr("placeholder", "Όνομασία στάσης");
	inputSource.val("");
	$("#terminal").hide();
	$("#leftDivInfo").empty();
	$("#rightDivInfo").empty();
	$("#searchBar button").hide();
	$(".onoffswitch-checkbox").attr("onclick", "optimumPathMode(true)");
	prepareCSS("switchMode");
	window.map = new Map(37.979946, 23.727801);
}

/**
 * Creates HTML elements in order user can parameterize optimal path.
 *
 * For example, use can select which mean of transport wants to use, or select
 * departure time etc.
 */
function parametersOptimumPath() {
	$("#initialInfo").append("<p id = 'methodSelection' class = 'bg-info'>"
			+ "<i>Αναζήτησε με βάση την ώρα αναχώρησης </i><input name = "
			+ "'selectMethod' type='radio' value='departureTime'>"
			+ "<i>Αναζήτησε με βάση την ώρα αφίξεως </i><input name = "
			+ "'selectMethod' type='radio' value='arrivalTime'><br><br></p>");
}

/**
 * Empty div which includes the instruction for the optimal path between two
 * points.
 */
function removeInstructions() {
	$("#leftDivInfo").empty();
}

/**
 * Initializes content of div which is going to be include the instructions for
 * the optimal path between two points.
 */
function initializeInstructions() {
	removeInstructions();
	$("#leftDivInfo").append("<h4>Οδηγίες</h4><div id = 'control'></div>");
}

/**
 * Opens dialog box for user to login. It opens when a guest user clicks to the
 * search for an optimal path button. This operation is supported only for
 * connected users.
 */
function messageToLogin() {
	loginDialog.dialog("open");
	$("#myonoffswitch").removeAttr("checked");
}

/**
 * Empty selected HTML elements.
 */
function emptyRouteElements() {
	$("#indiv").empty();
	$("#leftDivInfo").empty();
	$("#rightDivInfo").empty();
}

/**
 * Creates the basic information box which describes a route when a search is
 * done. More analytically, it presents information such as the route number,
 * route number, number of stops that route pass through, type of route (tram,
 * bus, metro, etc).
 *
 * Apart from this, it creates firstly, a jQuery UI buttonset in order user can
 * define the direction of trip which route services.
 *
 * If a user is connected to the system, then function creates a button to
 * add/delete routes from his list of favourites, accordingly if routes is in the
 * list or not. If user is a guest then button mentioned above is not created.
 *
 * @param data data associated with the route.
 * @param button HTML code in String format to create button to add/delete stop
 * from the list of favourites.
 */
function createsBasicInfoBoxRoutes(data, button) {
	$("#indiv").append("<div style='width: 100%;' id='infoRoute' class='ui-widget'>"
		+ "<div id='routeInfo' class='ui-state-highlight ui-corner-all' style='margin-top: "
		+ "20px; padding: 0 .7em; width: 100%;'></div></div>");
	var divInfo = $("#routeInfo");
	divInfo.append("<p style='text-align: center;'><span class='ui-icon ui-icon-info' "
		+ "style='float: left; margin-right: .3em;'></span>" + button
		+ "<b>Αριθμός γραμμής: </b><span id='number'>"
		+ data[0] + "</span>&nbsp;<b>Όνομασία γραμμής: </b><span id='routeName'>"
		+ data[1] + "</span>&nbsp;<b>Σύνολο στάσεων: </b>"
		+ data[3] + "&nbsp;<b>Τύπος Γραμμής: </b>" + data[4] + "<br>"
		+ "</p><div id='direction' style='width: 100%;'><p style='text-align: center;'>"
		+ "<b>Επιλογή κατέθυνσης: </b><input id='outbound' onclick = 'chooseDay(\"Outbound\")' "
	 	+ "name = 'selectMethod' type='radio' value='Outbound' checked><label for='outbound'>"
		+ data[1] + "</label></p></div>");
	if (!data[5])
		$("#direction p").append("<input id='inbound' onclick = 'chooseDay(\"Inbound\")'"
			+ "name = 'selectMethod' type='radio' value='Inbound'><label for='inbound'>"
			+ data[2] + "</label>");
}

/**
 * Creates the basic info box for route.
 *
 * @param data data associated for route in order to creates content.
 */
function showRouteInformation(data) {
	$("#indiv").empty();
	var button = "";
	var isFavourite = null;
	if (data[6] != undefined)
		isFavourite = data[6];
	if (isFavourite != null) {
		var icon;
		var message;
		var onclickFunction;
		if (isFavourite) {
			icon = "minus";
			message = "Αφαίρεση από τις αγαπημένες";
			onclickFunction = "deleteRouteFromFavourites()";
		} else {
			icon = "plus";
			message = "Προσθήκη στις αγαπημένες";
			onclickFunction = "addRouteToFavourites()";
		}
		button = "<button id='favourite' onclick='" + onclickFunction +
		"' style='font-size: 11px;'>" +
		"" + message + "</button>";
	}
	createsBasicInfoBoxRoutes(data, button);
	$("#direction").buttonset();
	$("#favourite").button({
		icons: {primary: "ui-icon-circle-" + icon},
		text: true
	});
}

/**
 * it creates a jQuery UI buttonset in order user can show the list of trips of
 * route on that specific day.
 *
 * @param data day of services of route.
 */
function selectDay(data) {
	var daysButtonSet = $("#days");
	daysButtonSet.remove();
	if(data.length != 0) {
		$("#routeInfo").append("<div id='days'><p style='text-align: center;'>" +
			"<b>Ημέρες εξυπηρέτησης: </b>&nbsp;</div>");
		var buttonSet = $("#days p");
		daysButtonSet = $("#days");
		if (data.daily)
			buttonSet.append("<input id = 'daily' type='radio' onclick='getTrips(\"" +
				data.direction + "\", \"friday\")' name ='days' value='monday'>" +
				"<label for='daily'>Δευτέρα - Πέμπτη</label>");
		if (data.friday)
			buttonSet.append("<input id = 'friday' type='radio' onclick='getTrips(\"" +
				data.direction + "\", \"friday\" )' name ='days' value='friday'>" +
				"<label for='friday'>Παρασκευή</label>");
		if (data.saturday)
			buttonSet.append("<input id = 'saturday' type='radio' onclick='getTrips(\"" +
				data.direction + "\", \"saturday\" )' name ='days' value='saturday'>" +
				"<label for='saturday'>Σάββατο</label>");
		if (data.sunday)
			buttonSet.append("<input id = 'sunday' type='radio' onclick='getTrips(\"" +
				data.direction + "\", \"sunday\" )' name ='days' value='sunday'>" +
				"<label for='sunday'>Κυριακή</label>");
		daysButtonSet.buttonset();
	}
}

/**
 * It creates a Div contained in the basic information box of a route which
 * displays the time when next route's trip departures from the source point stop
 * the day method is called.
 */
function showNextTrip() {
	$("#nextTrip").remove();
	$("#days").append("<div id='nextTrip' class='ui-widget'>"
		+ "<div class='ui-state-error ui-corner-all' style='padding: 0 .7em;'>"
		+ "<p style='text-align: center';>"
		+ "<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'>"
		+ "</span><strong>Επόμενο δρομολόγιο γραμμής στις : </strong>" + timeOfNextTrip
		+ "</p></div> </div>");

}

/**
 * Creates a Message to user that the selected route does not carry out any trip
 * the day method is called.
 *
 * For example a user wants to see information about route A on sunday. However,
 * route A has no any trips to execute on sunday. Then system will inform user
 * and will encourage him to look trips about the selected route another day.
 */
function alertRouteNoServices() {
	$("#days").append("<div id='nextTrip' class='ui-widget'>"
		+ "<div class='ui-state-error ui-corner-all' style='padding: 0 .7em;'>"
		+ "<p style='text-align: center';>"
		+ "<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'>"
		+ "</span><strong>Προσοχή! Η επιλεγμένη γραμμή δεν εκτελεί δρομολόγια "
		+ "την σημερινή ημέρα. Μπορείτε να αναζήτησετε πληροφορίες για τις μέρες "
		+ "που παρέχει τις υπηρεσίες της</strong></p></div> </div>");
}

/**
 * Creates a table with the list of trips that route services on the selected
 * day by user. Table included three columns.
 *
 * The first column is the number in sequence trip is serviced, the second one
 * is the time when route departures from the source point and the last one
 * includes radio button in order user can a select a trip to see more information
 * about it.
 *
 * @param data associated with the user.
 */
function showRouteTrips(data) {
	if(data.length > 1) {
		$("#rightDivInfo").empty();
		var parentDiv = $("#leftDivInfo");
		parentDiv.empty();
		parentDiv.append("<h4>Δρομολόγια γραμμής</h4>");
		parentDiv.append("<div id='scroll'>" +
		"<table id='leftInfo'>" +
		"<tr>" +
		"<th>#Δρομολόγιο</th>" +
		"<th>Ώρα αναχώρησης</th>" +
		"<th>Επέλεξε δρομολόγιο</th>" +
		"</tr>");
		var tableOfTrips = $("#leftInfo");
		for(var i = 0; i < data.length - 2; i++) {
			if (data[data.length-2] == i) {
				tableOfTrips.append("<tr>" +
					"<td>" + (i + 1) + "</td>" +
					"<td>" + data[i] + "</td>" +
					"<td><input onclick = 'getTripInformation(\"" +
					data[data.length-1] + "\", " + i + ")' " +
					"type = 'radio' name = 'moreInfo' checked/></td>" +
					"</tr>");
				if (timeOfNextTrip != undefined) showNextTrip(data[i]);
			} else {
				tableOfTrips.append("<tr>" +
					"<td>" + (i + 1) + "</td>" +
					"<td>" + data[i] + "</td>" +
					"<td><input onclick = 'getTripInformation(\"" +
					data[data.length-1] + "\", " + i + ")' " +
					"type = 'radio' name = 'moreInfo'/></td>" +
					"</tr>");
			}
		}
		parentDiv.append("</table>");
	}
}

/**
 * Creates a table with the list of stops a route pass through on specific day,
 * on a specific trip. Table includes three columns.
 *
 * The first column is the number in sequence route pass through, the second one
 * is name of stop and the last one is the time when route arrives at the specific
 * stop.
 *
 * Moreover, it displays the estimated total duration of the trip.
 *
 * @param data with list of information associated with stops, route pass through.
 */
function showTripInfo(data) {
	if(data.length != 0) {
		var parentDiv = $("#rightDivInfo");
		parentDiv.empty();
		parentDiv.append("<h4>Πληροφορίες Δρομολογίου</h4>");
		parentDiv.append("<p><b><i>Εκτιμώμενος χρόνος διαδρομής </i></b>: "
			+ data.duration[0] + "</p>");
		parentDiv.append("<table id = 'rightInfo'>" +
			"<tr>" +
			"<th>Σειρά στάσης</th>" +
			"<th>Ονομασία στάσης</th>" +
			"<th>Ώρα διέλευσης</th>" +
			"</tr>");
		var tableOfStops = $("#rightInfo");
		for(var i = 0; i < data.stopNames.length; i++) {
			var id = Math.random().toString().split(".")[1];
			tableOfStops.append("<tr>" +
				"<td>" + (i + 1) + "</td>" +
				"<td><input id='" + id + "' class = 'stopsSelection' onclick='getStopRoutes("
				+ data.coordinates[i][0] + ", " + data.coordinates[i][1] + ")' type = 'submit' " +
				"value = '" + data.stopNames[i] + "'/></td>" +
				"<td>" + data.departureTimes[i] + "</td>" +
				"</tr>");
			$("#" + id).button();
		}
		parentDiv.append("</table>");
	}
}

/**
 * Creates all essential elements for the search based on route set of operations.
 */
function searchBasedOnRouteLayout() {
	removeWarningMessage();
	removeInitialInfo();
	emptyRouteElements();
	var stopInput = $("#name");
	$("#nolink").hide();
	stopInput.hide();
	stopInput.val("");
	$("#routeNumber").show();
	$("#stopChoice").removeAttr("class");
	$("#routeChoice").addClass("active");
	$("#inputDescription").text("Αναζήτηση γραμμής");
	prepareCSS("switchMode");
	window.map = new Map(37.979946, 23.727801);
}

/**
 * Creates all essential elements for the search based on stop set of operations.
 */
function searchBasedOnStopLayout() {
	removeWarningMessage();
	removeInitialInfo();
	emptyRouteElements();
	var routeInput = $("#routeNumber");
	$("#nolink").show();
	$("#name").show();
	routeInput.hide();
	routeInput.val("");
	$("#routeChoice").removeAttr("class");
	$("#stopChoice").addClass("active");
	$("#inputDescription").text("Αναζήτηση στάσης");
	$("#myonoffswitch").removeAttr("checked");
	prepareCSS("switchMode");
	window.map = new Map(37.979946, 23.727801);
}

/**
 * Creates the content of Google Maps info window. Info window is displayed when
 * users clicks on stop from the list displayed when users selects a specific
 * trip of a route. Info window contains the list of routes which pass through
 * this specific stop. Routes are displayed as button that when user clicks on
 * system searches for the route which user clicks on.
 *
 * In addition, there is potential for user to see analytical information about
 * that stop if he clicks on button which system points.
 *
 * @param latitude latitude of stop.
 * @param longitude longitude of stop.
 * @param name An array of name of routes which pass through the stop.
 * @param number An array of number routes which pass through the stop.
 * @returns {string} HTML code in String format which creates the content of
 * info window.
 */
function createInfoWindowContent(latitude, longitude, name, number) {
	var selectedStop = latitude + ", " + longitude;
	var infoContent = "<div id='content' style='width:400px;'>" +
		"<h1 style='font-size: 16px'>Λίστα γραμμών που διέρχονται από την στάση</h1>" +
		"<p>Για περισσότερες πληροφορίες για την στάση πατήστε " +
			"<a onclick='searchBasedOnStopLayout(); getCoordinates(\""
			+ selectedStop + "\");'>εδώ</a></p>";
	for (var i = 0; i < name.length; i++) {
		infoContent = infoContent + "<button style='min-width: 100%;' "
		+ "onclick='window.routeNumber = \"" + number[i] + "\"; checkRoute();'>"
		+ number[i] + ", " + name[i] + "</button><br>";
	}
	infoContent = infoContent + "</div>";
	return infoContent;
}

