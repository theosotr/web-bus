$(function() {
    initializeFavouriteStopsDialog();
    initializeFavouriteRoutesDialog();
    initializeLoginDialog();
    initializeOptimalPathDialog();
    initializeSelect();
    initializeSpinners();
    initializeRadioButtons();
});

/**
 * Initializes jQuery UI select menus of page.
 */
function initializeSelect() {
    $("#transport").selectmenu();
}

/**
 * Initializes jQuery UI radio buttons of page.
 */
function initializeRadioButtons() {
    $("#radio").buttonset();
    $("#stops").buttonset();
    $("#routes").buttonset();
}

/**
 * Initializes jQuery Ui spinners of page.
 */
function initializeSpinners() {
    var LOWER_LIMIT = 0;
    var UPPER_LIMIT_HOUR =  23;
    var UPPER_LIMIT_MINUTE = 59;
    $( "#hour" ).spinner({
        spin: function( event, ui ) {
            if ( ui.value > UPPER_LIMIT_HOUR ) {
                $( this ).spinner( "value", LOWER_LIMIT );
                return false;
            } else if ( ui.value < LOWER_LIMIT ) {
                $( this ).spinner( "value", UPPER_LIMIT_HOUR );
                return false;
            }
        }
    });
    $( "#minute" ).spinner({
        spin: function( event, ui ) {
            if ( ui.value > UPPER_LIMIT_MINUTE ) {
                $( this ).spinner( "value", LOWER_LIMIT );
                return false;
            } else if ( ui.value < LOWER_LIMIT ) {
                $( this ).spinner( "value", UPPER_LIMIT_MINUTE );
                return false;
            }
        }
    });
}

/**
 * Initializes dialog box with the list of favourite stops of a user. Gives
 * potential to him to select one and see more information about it, or to delete
 * it.
 */
function initializeFavouriteStopsDialog() {
    var favouriteStops = $( "#favouriteStops" ).dialog({
        autoOpen: false,
        height: 500,
        width: 550,
        modal: true,
        buttons: {
            "Υποβολή": function() {
                var listEmpty = 0;
                if ($("#stops button").length != listEmpty) {
                    var selectedStop = $("#stops input[type='radio']:checked").val();
                    //If a user is connected.
                    if ($("#myonoffswitch").attr("onclick") == "optimumPathMode(false)")
                        optimumPathMode(false);
                    searchBasedOnStopLayout();
                    getCoordinates(selectedStop);
                }
                favouriteStops.dialog("close");
            },
            "Άκυρο": function() {
                favouriteStops.dialog( "close" );
            }
        }
    });
    $(".favorite").on("click", function() {
        favouriteStops.dialog("open");
    });
}

/**
 * Initializes dialog box with the list of favourite routes of a user. Gives
 * potential to him to select one and see more information about it, or to delete
 * it.
 */
function initializeFavouriteRoutesDialog() {
    var favouriteRoutes = $( "#favouriteRoutes" ).dialog({
        autoOpen: false,
        height: 500,
        width: 550,
        modal: true,
        buttons: {
            "Υποβολή": function() {
                var listEmpty = 0;
                if ($("#routes button").length != listEmpty) {
                    routeNumber = $("#routes input[type='radio']:checked").val().split(", ")[0];
                    //If a user is connected.
                    if ($("#myonoffswitch").attr("onclick") == "optimumPathMode(false)")
                        optimumPathMode(false);
                    searchBasedOnRouteLayout();
                    checkRoute();
                }
                favouriteRoutes.dialog( "close" );
            },
            "Άκυρο": function() {
                favouriteRoutes.dialog( "close" );
            }
        }
    });
    $(".favouriteRoutes").on("click", function() {
        favouriteRoutes.dialog("open");
    });
}

/**
 * Initializes a dialog box to give the potential to a user to enter system
 * (either by giving his credentials or by registering in system) when he tries
 * to use "Search of Optimal Path" operation.
 */
function initializeLoginDialog() {
    window.loginDialog = $( "#loginDialog" ).dialog({
        autoOpen: false,
        height: 400,
        width: 450,
        modal: true,
        buttons: {
            "Συνδεθείτε": function() {
                $("#login").submit();
            },
            "Άκυρο": function() {
                loginDialog.dialog( "close" );
            }
        }
    });
}

/**
 * Initializes dialog box to give potential to a user to configure the search of
 * optimal path between two points.
 *
 * For example, he can configure search to calculate optimal path based on a
 * specific departure/arrival time or to choose a mean of transport to reach his
 * destination.
 */
function initializeOptimalPathDialog() {
    var dialog = $( "#dialog" ).dialog({
        autoOpen: false,
        height: 500,
        width: 550,
        modal: true,
        buttons: {
            "Υποβολή": function() {
                parameterizeOptimumPath();
                dialog.dialog("close");
            },
            "Άκυρο": function() {
                dialog.dialog( "close" );
            }
        }
    });
    var form = dialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
    });
    $( "#openDialog" ).button({
        disabled: false
    }).on( "click", function() {
        dialog.dialog( "open" );
    });
}