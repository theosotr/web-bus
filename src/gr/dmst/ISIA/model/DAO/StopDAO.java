package gr.dmst.ISIA.model.DAO;

import java.util.LinkedList;
import java.util.List;

import gr.dmst.ISIA.app.DateManager;
import gr.dmst.ISIA.app.Stop;
import gr.dmst.ISIA.app.User;

/**
 * An interface that defines the way that {@link gr.dmst.ISIA.app.Stop} class
 * communicates with database. Stop class needs data that are stored database
 * for many operation.
 *
 * For example, if a user wants to see information about a stop such as list of
 * its routes etc it is needed all required data to be retrieved from database
 * by executing queries.
 *
 * It is implemented as Data Access Object
 *
 * @author Thodoris Sotiropoulos
 */
public interface StopDAO {
	/**
	 * Get the list of all stops what are stored in database.
	 *
	 * @return List of all stored stops.
	 */
	public List<Stop> getAllStops();

	/**
	 * Gets all required data to be edited by the
	 * {@link gr.dmst.ISIA.app.Stop#getRoutes(gr.dmst.ISIA.app.User)} and
	 * {@link gr.dmst.ISIA.app.Stop#getRoutes(java.lang.String)} methods.
	 *
	 * More specifically, after execution of query it is returned the list of
	 * times that every trip of every route (which pass through the stop) arrives
	 * at the specific stop.
	 *
	 * Then every Route and trip is initialized and the list of trips is added
	 * to the corresponding route.
 	 *
	 * @param stop associated with all information.
	 * @param date day when all information are associated with.
	 */
	public void getRoutesOfStop(Stop stop, String date);

	/**
	 * It is the same as with the method above.
	 *
	 * The only difference is that query which is executed returns if the
	 * specific stop is in the list of user's (specified by the object given as
	 * parameter) favourites. If yes, method returns true, false otherwise.
	 *
	 * @param user whose list of favourite stops is going to be checked.
	 * @param stop associated with all information.
	 * @param date day when all information are associated with.
	 * @return true if stops is in the list of user's favourites, false otherwise.
	 */
	public boolean getRoutesOfStop(User user, Stop stop, DateManager date);

	/**
	 * Returns all stops that begins with the String value which is stored in
	 * the field stopName of stop object given as parameter.
	 *
	 * For example three only stops are stored in database. The first one is named
	 * AAB, the second one is named AAC and the last one is named BBC and the
	 * value of stopName field of parameter object is AA then the AAB and AAC
	 * stops will be returned.
	 *
	 * @param stop which contains the string that stops should begin.
	 * @return List of stops that their name matches with the input given as
	 * parameter.
	 */
	public LinkedList<Stop> guessStop(Stop stop);
}
