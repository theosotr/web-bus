var target = [];
var acceptedValueTerminal = false;
/**
 * A function that is called when user press a key on #name element with as time
 * out of 300ms. Then, it calls StopLocationsServlet to show all possible stops
 * that user may want to search.
 *
 * If user gives an input that it does not mach to any stop, system shows a
 * message to him that there is no stop marches with the characters given in
 * input element.
 *
 * If user gives an input that it matches with a number of stops, system shows
 * these stops below input element and user can select one stop to complete his
 * search.
 *
 * System shows the optimal path between these
 * two points in condition that a user has given an accepted input of a stop on
 * the input of source stop.
 */
$(function() {
    acceptedValueTerminal = false;
    $("#terminal").autocomplete({
        delay: 300,
        minLength: 2,
        source: function(request, response) {
            $.ajax({
                url: "locations",
                dataType: 'json',
                data: {
                    stopName : $("#terminal").val()
                },
                success: function(data) {
                    if(jQuery.isEmptyObject(data)) {
                        alertInputNotExists("στάση");
                        acceptedValueTerminal = false;
                        return response();
                    }
                    removeInitialInfo();
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
            acceptedValueTerminal = true;
            target = ui.item.value.split(", ");
            $("#terminal").val(ui.item.label);
            if (!acceptedValueSource) $("#name").focus();
            else {
                var optimalPath = new OptimalPath(source, target);
                if (pathConfigured)
                    optimalPath.configureCalculationOfOptimumPath($("#dialog " +
                        "form #radio input[name='radio']:checked").val(),
                        $("#hour").val(), $("#minute").val(), $("#transport").val());
                optimalPath.calculateOptimumPath();
                $("#leftDivInfo").show();
                scrollToInfo();
            }
        },
        focus: function(event) {
            event.preventDefault();
        }
    }).autocomplete("widget").addClass("autocompletion");
});