package gr.dmst.ISIA.app;

/**
 * A class that represents a stop in a trip (extends Stop class).
 *
 * @author Konstantinos Karakatsanis
 */
public class StopInTrip extends Stop {
	/** Stop's sequence */
	private int sequence;

	/** The time that the trip passes from the stop */
	private String departureTime;

	/**
	 * Initializes object with the stop's name,the specific location in map,
	 * the sequence of stop and the departure time.
	 * Calls the {@link gr.dmst.ISIA.app.Stop#Stop(String stopName, String longitude, String latitude))}
	 * method. (Superclass' constructor)
	 *
	 * @param stopName name of stop
	 * @param longitude longitude of stop
	 * @param latitude latitude of stop
	 * @param sequence stop's sequence
	 * @param departureTime departure from stop
	 */
	public StopInTrip(String stopName, String longitude, String latitude,
					  int sequence, String departureTime) {
		super(stopName, longitude, latitude);
		this.setSequence(sequence);
		this.setDepartureTime(departureTime);
	}

	/**
	 * Sets the stop's sequence
	 *
	 * @param sequence stop's sequence
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets the stop's sequence
	 *
	 * @return the stop's sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * Sets the departure time from the stop
	 *
	 * @param departureTime departure from stop
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * Gets the departure time from the stop
	 *
	 * @return departure time from the stop
	 */
	public String getDepartureTime() {
		return departureTime;
	}
}