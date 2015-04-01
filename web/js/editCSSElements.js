var cssCase = "switchMode";
/**
*A function that change the attributes of html elements when window is resized
*/
$(function() {
	prepareCSS(cssCase);
	$(window).resize(function() {
		if($(window).width() < 800) setCSSSmallScreen();
		else prepareCSS(cssCase);
	});
});

/**
 * Set CSS of three Div when when width of window screen is under of 800px.
 */
function setCSSSmallScreen() {
	var leftInfo = $("#leftDivInfo");
	if (!leftInfo.is(":empty")) {
		leftInfo.css({'width':'100%'});
		$('#map-canvas').css({'margin-left':'0%',
			'margin-top': '0px',
			'width': '100%'});
		$('#rightDivInfo').css({'margin-top':'0px',
			'width': '100%',
			'margin-left':'0%'
		});
	}
	$("#footer").css({'position': 'inherit'});
}

/**
 * Set position of div contains map and left Div of map under the text input
 * where user gives his inputs.
 */
function setMapAndLeftDivDown() {
	if (!$("#terminal").is(":visible")) {
		$('#leftDivInfo').css({
			'width': '45%',
			'margin-top': '0px'
		});
		$('#map-canvas').css({
			'margin-left': '50%',
			'margin-top': '-700px',
			'height': '700px',
			'width': '50%'
		});
	} else {
		$('#leftDivInfo').css({
			'width': '45%',
			'margin-top': '-250px'
		});
		$('#map-canvas').css({
			'margin-left': '50%',
			'margin-top': '-700px',
			'height': '700px',
			'width': '50%'
		});
	}
}

/**
 * Set initial map CSS when no information about stop or route is displayed.
 */
function initialSizeOfMap() {
	$('#leftDivInfo').css({
		'width': '45%',
		'margin-top': '0px'
	});
	$('#map-canvas').css({
		'margin-left': 'auto',
		'margin-right': 'auto',
		'margin-top': '-1000px',
		'height': '350px',
		'width': '50%'
	});
}

/**
 * Set map's position and size accordingly for the route search operation.
 */
function setMapCenter() {
	$("#leftDivInfo").css({
		'width': '25%',
		'margin-top': '0px'
	});
	$("#map-canvas").css({
		'height': '700px',
		'margin-top': '-740px',
		'margin-left': '25%',
		'width': '40%'
	});
}

/**
 * Prepares CSS rules for a number of cases accordingly.
 *
 * @param prepareCase
 */
function prepareCSS(prepareCase) {
	cssCase = prepareCase;
	$("#footer").css({'position': 'fixed'});
	switch (prepareCase) {
		case "optimalPath":
			setMapAndLeftDivDown();
			break;
		case "switchMode":
			initialSizeOfMap();
			break;
		case "route":
			setMapCenter();
			break;
		default :
			setMapAndLeftDivDown();
			break;
	}
	if ($(window).width() < 800)
		setCSSSmallScreen();
}