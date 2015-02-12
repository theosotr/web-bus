package gr.dmst.ISIA.app;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gr.dmst.ISIA.model.DAO.StopDAOImp;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class that represents a stop where passengers can board and it is included
 * in a route's trip. It described by its name, address, location(latitude and
 * longitude) and the list of routes which pass through it.
 *
 * @author Thodoris Sotiropoulos
 */
public class Stop {

	/** Name of stop. */
	private String stopName;

	/** Name of street or boulevard where stop is located. */
	private String location;

	/** List of coordinates of stop. First index of array is latitude and the
	 * second is longitude.
 	 */
	private String[] coordinates = new String[2];

	/** List of Routes which pass from this stop. */
	private LinkedList<Route> routes = new LinkedList<>();

	/**
	 * Initializes object with the stop's name and the specific location in map.
	 *
	 * @param stopName name of stop.
	 * @param longitude longitude of stop.
	 * @param latitude latitude of stop.
	 */
	public Stop(final String stopName, final String longitude,
			final String latitude) {
		this.stopName = stopName;
		this.coordinates[0] = latitude;
		this.coordinates[1] = longitude;
	}

	/**
	 * Initializes object with a specific name of stop.
	 *
	 * @param stopName name of stop.
	 */
	public Stop(final String stopName) {
		this.stopName = stopName;
	}

	/**
	 * Initializes object all essential information to be fully described.
	 *
	 * @param stopName name of stop.
	 * @param location address of stop where it is located.
	 * @param coordinates coordinates of stop(latitude, longitude).
	 */
	public Stop(final String stopName, final String location,
				final String[] coordinates) {
		this.stopName = stopName;
		this.location = location;
		this.coordinates = coordinates.clone();
	}

	/**
	 * Initializes object with the coordinates of stop (latitude, longitude).
	 *
	 * @param coordinates coordinates of stop (latitude, longitude).
	 */
	public Stop(final String[] coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Initializes object with the known coordinates and address of stop.
	 *
	 * @param coordinates coordinates of stop (latitude, longitude).
	 * @param location address of location where stop is located.
	 */
	public Stop(final String[] coordinates, final String location) {
		this.coordinates = coordinates.clone();
		this.location = location;
	}

	/**
	 * Turn a LinkedList of objects into a JSONArray object.
	 *
	 * @param list LinkedList of objects
	 * @return JSONArray object from LinkedList.
	 */
	private JSONArray toJSONArray(final LinkedList<?> list) {
		JSONArray array = new JSONArray();
		for (Object o: list)
			array.put(o);
		return array;
	}

	/**
	 * Turn a map into a JSONObject object. Map keys are JSONObject object's
	 * keys and Map values are JSONObject object's values too.
	 *
	 * @param values Map to be converted into a JSONObject
	 * @return JSONObject object from map
	 */
	private JSONObject toJSONObject(final TreeMap<String, ?> values) {
		JSONObject json = new JSONObject();
		for (Map.Entry<String, ?> entry: values.entrySet())
			json.put(entry.getKey(), entry.getValue());
		return json;
	}

	/**
	 * Get list of routes which pass through a stop with all the essential
	 * information about them such as, the list of the next arrivals, information
	 * about of the first and last trip of each route the day when the method is
	 * called.
	 *
	 * This method is called when either a user has entered system or a guest
	 * user uses system.
	 *
	 * Apart from above, this method also returns in JSON format if the
	 * specific stop is in the list of favourite stops of User or not.
	 *
	 * @param user User for whom method is called to response his request. If
	 * null, then a guest user (who obviously has no favourites stop) uses system
	 * @return LinkedList of JSONObject objects with all the required information.
	 */
	public LinkedList<JSONObject> getRoutes(final User user) {

		// Get the name of day when this method is called.
		DateManager date = new DateManager("EEEE");
		LinkedList<JSONObject> routes = new LinkedList<>();
		// Initializes the object which has access to database
		StopDAOImp dao = new StopDAOImp();
		boolean isFavourite = false;
		if (user.getUsername() != null)
			// Get all information required from database via the DAO object
			isFavourite = dao.getRoutesOfStop(user, this, date);
		else
			dao.getRoutesOfStop(this, date.getFormattedDate());
		if (this.routes.size() == 0)
			// if not results returned, return the empty list
			return routes;
		for (Route route: this.routes) {
			/*
			* Get all information about the next arrivals of each route and
			* first and last trip.
			*/
			JSONObject routeArrivals = this.getNextArrivals(route);
			JSONArray routeInfo = new JSONArray();
			routeInfo.put(route.getRouteNumber());
			routeInfo.put(route.getRouteName());
			routeArrivals.put("route", routeInfo);
			if (user.getUsername() != null) routeArrivals.put("isFavourite",
					isFavourite);
			routes.add(routeArrivals);
		}
		return routes;
	}

	/**
	 * Get the list of routes which pass through this stop on a day of week.
	 * This method is different from method
	 * {@link gr.dmst.ISIA.app.Stop#getRoutes(gr.dmst.ISIA.app.User user)} because it only
	 * returns the list of routes without any informatiom about trips or
	 * next arrivals.
	 *
	 * @param serviceDay day when the list of routes pass through the stop.
	 * @return JSONObject with two JSONArrays. One is the array of numbers of
	 * routes, and the second one is the array of names of routes.
	 */
	public JSONObject getRoutes(final String serviceDay) {
		StopDAOImp dao = new StopDAOImp();
		dao.getRoutesOfStop(this, serviceDay);
		JSONArray routeName = new JSONArray();
		JSONArray routeNumber = new JSONArray();
		JSONObject json = new JSONObject();
		for (Route route: this.routes) {
			routeNumber.put(route.getRouteNumber());
			routeName.put(route.getRouteName());
		}
		json.put("number", routeNumber);
		json.put("name", routeName);
		return json;
	}

	/**
	 * Returns all information about the next arrivals (within the next 30 minutes
	 * by the time the getRoutes() method is called) of a route as well as the
	 * first and last route's trip of the day.
	 *
	 * Iterates all trips of route which services on a certain day, and then
	 * calculates the difference of current time and the arrival time of trip.
	 *
	 * @param route Route
	 * @return JSONObject object with all required information. Includes:
	 * 1)a JSONArray object with all next arrivals
	 * 2)time of first trip of day
	 * 3)time of last trip of day
	 * 4)direction of trip
	 */
	private JSONObject getNextArrivals(Route route) {
		LinkedList<Integer> nextArrivals = new LinkedList<>();
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int minIndex = 0;
		int maxIndex = 0;
		int counter = 0;
		final int MINUTE_INDEX = 6;
		final int MINUTE = 5;
		final int HOUR = 2;
		final int HOUR_INDEX = 3;
		DateManager hour = new DateManager("HH");
		int currentMinute = Integer.parseInt(new DateManager("mm").getFormattedDate());
		int currentHour = hour.convertHourToDatabaseFormat();
		for (Trip trip: route.getTripList()) {
			String time = trip.getSequenceOfStops().get(this).substring(0,
					MINUTE_INDEX);
			if (time != null) {
				int arrivalHour = Integer.parseInt(time.substring(0, HOUR));
				int arrivalMinute = Integer.parseInt(time.substring(HOUR_INDEX,
						MINUTE));
				int arrivalTime = DateManager.calculateTimeOfNextArrivals(currentHour,
						currentMinute, arrivalHour, arrivalMinute);
				final int upperLimit = 30;
				final int lowerLimit = 0;
				if (arrivalTime <= upperLimit && arrivalTime > lowerLimit)
					nextArrivals.add(arrivalTime);
				if (arrivalTime < min) {
					min = arrivalTime;
					minIndex = counter;
				}
				if (arrivalTime > max) {
					max = arrivalTime;
					maxIndex = counter;
				}
			}

			counter++;
		}
		TreeMap<String, Object> map = new TreeMap<>();
		map.put("nextArrivals", this.toJSONArray(nextArrivals));
		map.put("firstArrival", DateManager.convertTimeIntoNormalFormat(route
				.getTripList().get(minIndex).getSequenceOfStops()
				.get(this).substring(0, MINUTE)));
		map.put("direction", route.getTripList()
				.get(minIndex).getDirectionName());
		map.put("lastArrival", DateManager.convertTimeIntoNormalFormat(route
				.getTripList().get(maxIndex).getSequenceOfStops()
				.get(this).substring(0, MINUTE)));
		return this.toJSONObject(map);
	}

	/**
	 * Get name of Stop.
	 *
	 * @return name of Stop object.
	 */
	public String getStopName() {
		return this.stopName;
	}

	/**
	 * Get coordinates of all stops which are stored in database.
	 *
	 * @return JSONObject which includes :
	 * 1)Coordinates of all stops.
	 * 2)Name of all stops.
	 */
	public static JSONObject getAllCoordinates() {
		
		List<Stop> stops;
		// Initializes the object which has access to database
		StopDAOImp dao = new StopDAOImp();
		// Get all Stop objects which are stored to database
		stops = dao.getAllStops();
		JSONObject json = new JSONObject();
		if (stops.size() == 0) {
			// if no results returned, return empty JSONObject
			return json;
		}
		JSONArray coordinates = new JSONArray();
		JSONArray stopNames = new JSONArray();
		for (Stop stop: stops) {
			String[] coords = stop.getCoordinates();
			stopNames.put(stop.getStopName());
			coordinates.put(coords);
		}
		json.put("stop", stopNames);
		json.put("coordinates", coordinates);
		return json;
	}

	/**
	 * Returns all stops which contain the expression given by the user. The
	 * expression is stored in the field stopName of object.
	 *
	 * @return JSONObject which includes:
	 * 1)name of stops.
	 * 2)location of stops.
	 * 3)coordinates of stops.
	 */
	public JSONObject guessStops() {
		JSONObject json = new JSONObject();
		// Initializes the object which has access to database
		StopDAOImp dao = new StopDAOImp();
		JSONArray locations = new JSONArray();
		JSONArray stopNames = new JSONArray();
		JSONArray coordinates = new JSONArray();
		LinkedList<Stop> stops;
		// Get all stops which match with the characters given by the user as input
		stops = dao.guessStop(this);
		if (stops.size() == 0) {
			//if not results returns, returns empty JSONObject object
			return json;
		}
		for (Stop stop : stops) {
			locations.put(stop.getLocation());
			stopNames.put(stop.getStopName());
			coordinates.put(stop.getCoordinates());
		}
		json.put("stop", stopNames);
		json.put("locations", locations);
		json.put("coordinates", coordinates);
		return json;
	}

	/**
	 * Get name of street where stop is located.
	 *
	 * @return name of street.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set List of routes which pass from the stop according to the value given
	 * as parameter.
	 *
	 * @param routes List of routes.
	 */
	public void setRoutes(LinkedList<Route> routes) {
		this.routes = routes;
	}

	/**
	 * Get coordinates of stop.
	 *
	 * @return coordinates of stop.
	 */
	public String[] getCoordinates() {
		return coordinates;
	}

	/**
	 * Overrides hashCode method of class Object in order to get values from a
	 * HashMap with objects of class Stop as keys
	 *
	 * @return hash code of an attribute
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + ((stopName == null) ? 0 : stopName.hashCode());
		return result;
	}

	/**
	 * Overrides equals method of class Object in order to get values from a
	 * HashMap with objects of class Stop as keys
	 *
	 * @param obj object of class Stop
	 * @return true if equals, otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stop other = (Stop) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		if (stopName == null) {
			if (other.stopName != null)
				return false;
		} else if (!stopName.equals(other.stopName))
			return false;
		return true;
	}
}
