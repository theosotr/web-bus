
package gr.dmst.ISIA.app;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class that represents a trip.
 *
 * @author Konstantinos Karakatsanis
 */
public class Trip {

	/** Trip's ID */
	private String tripID;

	/** Trip's route */
	private Route route;

	/** Trip's direction */
	private String direction;

	/**
	 * A hash map in which the stops of the trip are stored
	 * with the time that the trip passes
	 */
	private HashMap<Stop, String> sequenceOfStops = new HashMap<>();

	/**
	 * An ArrayList in which the stops of the trip are stored
	 * with the time that the trip passes
	 */
	private List<StopInTrip> sequenceOfStops2 = new ArrayList<>();

	/**
	 * An Array in which the stops of the trip are stored,
	 * ordered by their sequence, with the time that the
	 * trip passes
	 */
	private StopInTrip[] sequenceArray;

	/**
	 * Initializes object with the trip's ID, direction
	 * and route.
	 *
	 * @param route the trip's route
	 * @param direction the trip's direction
	 * @param trip the trip's ID
	 */
	public Trip(Route route, String direction, String trip) {

		this.route = route;
		this.direction = direction;
		this.tripID = trip;

	}

	/**
	 * Gets the trip's direction name.
	 *
	 * @return direction's name
	 */
	public String getDirectionName() {
		String directionName = "Προς ";
		int firstIndex = this.route.getRouteName().indexOf('-');
		int lastIndex = this.route.getRouteName().lastIndexOf('-');
		if (this.direction.equals("Outbound"))
			return directionName + this.route.getRouteName()
					.substring(lastIndex + 1);
		else
			return directionName + this.route.getRouteName()
					.substring(1, firstIndex - 1);
	}

	/**
	 * Gets the trip's sequence of stops.
	 *
	 * @return a hash map in which the stops of the trip are stored
	 * with the time that the trip passes (from the stop)
	 */
	public HashMap<Stop, String> getSequenceOfStops() {
		return this.sequenceOfStops;
	}
	/**
	 * Gets the trip's route.
	 *
	 * @return route object
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * Gets the trip's direction.
	 *
	 * @return direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * Gets the trip's sequence of stops.
	 *
	 * @return an Array in which the stops of the trip are stored,
	 * ordered by their sequence, with the time that the
	 * trip passes (from the stop)
	 */
	public StopInTrip[] getSequenceOfStops2() {
		sequenceArray = sequenceOfStops2.toArray(new StopInTrip
				[sequenceOfStops2.size()]);
		return sequenceArray;
	}

	/**
	 * Adds a stop in the sequence of stops.
	 */
	public void addStopInTrip(StopInTrip stop) {
		sequenceOfStops2.add(stop);
	}

	/**
	 * Adds a stop in the sequence of stops.
	 */
	public void addStop(Stop stop, String tripId) {
		this.sequenceOfStops.put(stop, tripId);
	}
}
