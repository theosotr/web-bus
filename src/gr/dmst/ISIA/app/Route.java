package gr.dmst.ISIA.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import gr.dmst.ISIA.model.DAO.RouteDAOImp;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class that represents a route.
 */
public class Route {

	/** Route's number. */
	private String routeNumber;

	/** Route's name. */
	private String routeName;

	/** The day that the tripList are taking place. */
	private String tripDay;

	/** Route's type. */
	private String routeType;

	/** An Array with the names of the weekdays */
	private String[] days = {"monday", "tuesday", "wednesday", "thursday",
			"friday", "saturday", "sunday"};

	/**
	 * A Linked List in which the tripList of the route
	 * are stored.
	 */
	private LinkedList<Trip> tripList = new LinkedList<>();

	/** The position of the next trip in the trips' array. */
	private int nextTrip;

	/** A boolean variable that shows if the route is circular */
	private boolean circular;

	/** An Array with all the trips */
	private Trip[] trips;

	/** An Array with the outbound trips */
	private Trip[] outboundTrips;

	/** An Array with the inbound trips */
	private Trip[] inboundTrips;

	/**
	 * Initializes object with the route's name, number, type and the
	 * trip's day.
	 *
	 * @param routeNumber number of route.
	 * @param routeName name of route.
	 */
	public Route(final String routeNumber, final String routeName) {

		this.routeNumber = routeNumber;
		this.routeName = routeName;
		DateManager date = new DateManager("EEEE");
		this.tripDay = date.getFormattedDate().toLowerCase();

	}

	/**
	 * Initializes object with the route's name, number, type and the
	 * trip's day.
	 *
	 * @param routeNumber number of route.
	 */
	public Route(final String routeNumber) {
		RouteDAOImp dao = new RouteDAOImp();
		dao.findRoute(this, routeNumber);
		this.tripDay = "monday";
		this.nextTrip = 0;

	}

	/**
	 * Gets the route's shape if circular.
	 *
	 * @return true if the route is circular.
	 */
	public boolean isCircular() {
		return circular;
	}

	/**
	 * Sets the route's shape if circular.
	 */
	public void setCircular(final boolean circular) {
		this.circular = circular;
	}

	/**
	 * Gets the route's next trip.
	 *
	 * @return the position of the next trip in the trips' array.
	 */
	public int getNextTrip() {
		return nextTrip;
	}

	/**
	 * Sets the route's next trip position.
	 */
	public void setNextTrip(final int nextTrip) {
		this.nextTrip = nextTrip;
	}

	/**
	 * Gets number of Route.
	 *
	 * @return number of Route object.
	 */
	public String getRouteNumber() {
		return this.routeNumber;
	}

	/**
	 * Gets name of Route.
	 *
	 * @return name of Route object.
	 */
	public String getRouteName() {
		return this.routeName;
	}

	/**
	 * Gets the tripList of a Route.
	 *
	 * @return Linked List with route's tripList.
	 */
	public LinkedList<Trip> getTripList() {
		return this.tripList;
	}

	/**
	 * Sets the route's trips ordered by their
	 * departure time, using the mergesort algorithm.
	 * Then it divides them to inbound and outbound according
	 * to their direction.
	 */
	public void setTrips(final List<Trip> tripsList) {
		int i = 0;
		int j;
		List<Trip> tripsOut = new ArrayList<>();
		List<Trip> tripsIn = new ArrayList<>();
		while (i < tripsList.size() - 1) {
			j = i + 1;
			while (j < tripsList.size() - 1) {
				if (tripsList.get(i).getSequenceOfStops2()[0].getDepartureTime()
						.equals(tripsList.get(j).getSequenceOfStops2()[0]
								.getDepartureTime())) {
					tripsList.remove(j);
					j = j - 1;
				}
				j++;
			}
			i++;
		}
		trips = tripsList.toArray(new Trip[tripsList.size()]);
		mergeSort(trips);
		for (i = 0; i < trips.length; i++) {
			if (trips[i].getDirection().equalsIgnoreCase("Outbound"))
				tripsOut.add(trips[i]);
			else
				tripsIn.add(trips[i]);
		}
		outboundTrips = tripsOut.toArray(new Trip[tripsOut.size()]);
		inboundTrips = tripsIn.toArray(new Trip[tripsIn.size()]);
	}

	/**
	 * Gets the trips of a Route.
	 *
	 * @return Array with route's trips.
	 */
	public Trip[] getTrips() {
		return trips;
	}

	/**
	 * @return the route's name
	 */
	public String showRouteName() {
		StringBuilder name;
		int counter = routeName.split("-").length - 1;

		if (counter == 0)
			name = new StringBuilder()
					.append(routeName.substring(1, routeName.length() - 1));
		else if (counter == 1)
			name = new StringBuilder()
					.append(routeName.substring(1, routeName.indexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1));
		else if (counter == 2)
			name = new StringBuilder()
					.append(routeName.substring(1, routeName.indexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.indexOf('-') + 1,
							routeName.lastIndexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1));
		else
			name = new StringBuilder()
					.append(routeName.substring(1, routeName.indexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.indexOf('-') + 1,
							routeName.indexOf('-', routeName.indexOf('-') + 1)))
					.append(" - ")
					.append(routeName.substring(
							routeName.indexOf('-', routeName.indexOf('-') + 1) + 1,
							routeName.lastIndexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1));
		return String.valueOf(name);
	}

	/**
	 * @return the reversed route's name
	 */
	public String reverseRouteName() {
		StringBuilder reversedName;
		int counter = routeName.split("-").length - 1;
		if (counter == 0)
			reversedName = new StringBuilder()
					.append(routeName.substring(1, routeName.length() - 1));
		else if (counter == 1)
			reversedName = new StringBuilder()
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1)).append(" - ")
					.append(routeName.substring(1, routeName.indexOf('-')));
		else if (counter == 2)
			reversedName = new StringBuilder()
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1))
					.append(" - ")
					.append(routeName.substring(routeName.indexOf('-') + 1,
							routeName.lastIndexOf('-'))).append(" - ")
					.append(routeName.substring(1, routeName.indexOf('-')));
		else
			reversedName = new StringBuilder()
					.append(routeName.substring(routeName.lastIndexOf('-') + 1,
							routeName.length() - 1))
					.append(" - ")
					.append(routeName.substring(
							routeName.indexOf('-', routeName.indexOf('-') + 1) + 1,
							routeName.lastIndexOf('-')))
					.append(" - ")
					.append(routeName.substring(routeName.indexOf('-') + 1,
							routeName.indexOf('-', routeName.indexOf('-') + 1)))
					.append(" - ")
					.append(routeName.substring(1, routeName.indexOf('-')));
		return String.valueOf(reversedName);
	}

	/**
	 * Sets the route's number.
	 */
	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	/**
	 * Sets the route's name.
	 */
	public void setRouteName(final String routeName) {
		this.routeName = routeName;
	}

	/**
	 * Sets the route's trip day.
	 */
	public void setTripDay(final String tripDay) {
		this.tripDay = tripDay;
	}

	/**
	 * Guessing the route names based on the input
	 * the user gave.
	 *
	 * @return A JSONObject with the names and the
	 * numbers of the guessed routes.
	 */
	public JSONObject guessRoutes() {
		JSONObject json = new JSONObject();
		RouteDAOImp dao = new RouteDAOImp();
		JSONArray names = new JSONArray();
		JSONArray numbers = new JSONArray();
		List<Route> routes = dao.guessRoute(this);
		if (routes.size() == 0) {
			return json;
		} else {
			for (Route route : routes) {
				names.put(route.showRouteName());
				numbers.put(route.getRouteNumber());
			}
			json.put("numbers", numbers);
			json.put("names", names);
			return json;
		}
	}

	/**
	 * Sets the route's type.
	 */
	public void setRouteType(final String routeType) {
		this.routeType = routeType;
	}

	/**
	 * Initializes the route's trips.
	 */
	public boolean initializeTrips(final User user) {
		RouteDAOImp dao = new RouteDAOImp();
		boolean isFavourite = false;
		if (user == null) dao.getTrips(this, tripDay);
		else isFavourite = dao.getTrips(user, this, this.tripDay);
		return isFavourite;
	}

	/**
	 * Initializes the route's trips.
	 *
	 * @return A JSONObject with the days of a trip and
	 * its direction.
	 */
	public JSONObject getRouteDays(final String direction) {
		RouteDAOImp dao = new RouteDAOImp();
		return dao.getRouteDays(this, direction);
	}

	public void findNextTrip(final String direction) {
		int position = 0;
		final int MINUTE = 5;
		final int HOUR = 2;
		final int HOUR_INDEX = 3;
		int min = Integer.MAX_VALUE;
		DateManager hour = new DateManager("HH");
		int currentMinute = Integer.parseInt(new DateManager("mm")
				.getFormattedDate());
		int currentHour = hour.convertHourToDatabaseFormat();
		if (direction.equalsIgnoreCase("Outbound")) {
			for (int i = 0; i < outboundTrips.length; i++) {
				String time = this.outboundTrips[i].getSequenceOfStops2()[0]
						.getDepartureTime();
				int arrivalHour = Integer.parseInt(time.substring(0, HOUR));
				int arrivalMinute = Integer.parseInt(time.substring(HOUR_INDEX,
						MINUTE));
				int value = DateManager.calculateTimeOfNextArrivals(currentHour,
						currentMinute, arrivalHour, arrivalMinute);
				if (value < min && value >= 0 ) {
					min = value;
					position = i;
				}
			}
		} else {
			for (int i = 0; i < inboundTrips.length; i++) {
				String time = this.inboundTrips[i].getSequenceOfStops2()[0]
						.getDepartureTime();
				int arrivalHour = Integer.parseInt(time.substring(0, HOUR));
				int arrivalMinute = Integer.parseInt(time.substring(HOUR_INDEX,
						MINUTE));
				int value = DateManager.calculateTimeOfNextArrivals(currentHour,
						currentMinute, arrivalHour, arrivalMinute);
				if (value < min && value >= 0) {
					min = value;
					position = i;
				}
			}
		}
		this.setNextTrip(position);
	}

	/**
	 * Gets the trip's info such as duration and the stops that the 
	 * route passes from, with their coordinates and departure times.
	 *
	 * @return A JSONObject with the trip's info.
	 */
	public JSONObject getTripInfo(final int i, final String direction) {
		JSONObject json = new JSONObject();
		JSONArray duration = new JSONArray();
		JSONArray stopNames = new JSONArray();
		JSONArray departureTimes = new JSONArray();
		JSONArray coordinates = new JSONArray();
		Trip trip;
		if (direction.equalsIgnoreCase("Outbound")) trip = outboundTrips[i];
		else trip = inboundTrips[i];
		String time1 = trip.getSequenceOfStops2()[0].getDepartureTime();
		String time2 = trip.getSequenceOfStops2()[trip.getSequenceOfStops2()
				.length - 1].getDepartureTime();
		duration.put(DateManager.findDuration(time1, time2));
		for (StopInTrip stop : trip.getSequenceOfStops2()) {
			stopNames.put(stop.getStopName());
			departureTimes.put(DateManager.convertTimeIntoNormalFormat(stop
					.getDepartureTime()));
			String[] coords =  {
					stop.getCoordinates()[0],
					stop.getCoordinates()[1]
			};
			coordinates.put(coords);
		}
		json.put("duration", duration);
		json.put("stopNames", stopNames);
		json.put("departureTimes", departureTimes);
		json.put("coordinates", coordinates);
		return json;
	}

	/**
	 * Gets the trips that a route performs.
	 *
	 * @return A JSONArray with the route's trips.
	 */
	public JSONArray getRouteTrips(final String direction) {
		JSONArray departureTimes = new JSONArray();
		if (direction.equalsIgnoreCase("Outbound")) {
			for (Trip trip : outboundTrips) {
				departureTimes.put(DateManager.convertTimeIntoNormalFormat(trip
						.getSequenceOfStops2()[0].getDepartureTime()));
			}
		} else {
			for (Trip trip : inboundTrips) {
				departureTimes.put(DateManager.convertTimeIntoNormalFormat(trip
						.getSequenceOfStops2()[0].getDepartureTime()));
			}
		}
		return departureTimes;
	}

	/**
	 * The mergesort algorithm.
	 */
	public static Trip[] mergeSort(Trip[] trips) {
		if (trips.length > 1) {
			int bElements = trips.length / 2;
			int cElements = bElements;
			if ((trips.length % 2) == 1)
				cElements += 1;
			Trip[] trips1 = new Trip[bElements];
			Trip[] trips2 = new Trip[cElements];
			System.arraycopy(trips, 0, trips1, 0, bElements);
			System.arraycopy(trips, bElements, trips2, bElements - bElements,
					bElements + cElements - bElements);
			trips1 = mergeSort(trips1);
			trips2 = mergeSort(trips2);
			trips = merge(trips1, trips2, trips);
		}
		return trips;
	}

	public static Trip[] merge(Trip[] trips1, Trip[] trips2, Trip[] trips) {
		int i = 0;
		int j = 0;
		int k = 0;
		while (trips1.length != i && trips2.length != j) {
			if (DateManager.getDifference(trips1[i]
							.getSequenceOfStops2()[0].getDepartureTime(),
					trips2[j].getSequenceOfStops2()[0]
							.getDepartureTime()) > 0) {
				trips[ k ] = trips1[ i ];
				i++;
				k++;
			} else {
				trips[k] = trips2[j];
				j++;
				k++;
			}
		}
		while (trips1.length != i) {
			trips[k] = trips1[i];
			i++;
			k++;
		}
		while (trips2.length != j) {
			trips[k] = trips2[j];
			j++;
			k++;
		}
		return trips;
	}

	/**
	 * Add a trip to the list of route's tripList.
	 *
	 * @param trip Trip to be added.
	 */
	public void addTrip(final Trip trip) {
		this.tripList.add(trip);
	}

	/**
	 * Gets type of Route.
	 *
	 * @return type of Route object.
	 */
	public String getRouteType() {
		return routeType;
	}

	/**
	 * Gets the days in which a route performs trips.
	 *
	 * @return An Array with the days.
	 */
	public String[] getDays() {
		return days;
	}
}
