package gr.dmst.ISIA.model.DAO;

import org.json.JSONObject;
import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.User;

import java.util.List;

/**
 * An interface that defines the way that {@link gr.dmst.ISIA.app.Route} class
 * communicates with database. Route class needs data that are stored database
 * for many operation.
 *
 * For example, if a user wants to see information about a route such as list of
 * its trips etc it is needed all required data to be retrieved from database
 * by executing queries.
 *
 * It is implemented as Data Access Object
 */
public interface RouteDAO {
	/**
	 * Get the all the route's trips that are stored in the database.
	 */
	public void getTrips(Route route, String day);

	/**
	 * Get the all the route's trips that are stored in the database. The only
	 * difference with the {@link gr.dmst.ISIA.model.DAO.RouteDAO#getTrips(Route route, String day)}
	 * is that check if the specific route is in the list of favourites of a
	 * user defined by the user object given as parameter.
	 *
	 * @return boolean if the route is favourite
	 */
	public boolean getTrips(User user, Route route, String day);

	/**
	 * Get the all the route's information that are stored in the database.
	 */
	public void findRoute(Route route, String routeNumber);
	/**
	 * Get the list of all the matching routes in the database.
	 *
	 * @return List of all stored stops.
	 */
	public List<Route> guessRoute(Route route);

	/**
	 * Get the all the trip's days that are stored in the database.
	 *
	 * @return JSONObject of all available days.
	 */
	public JSONObject getRouteDays(Route route, String direction);
}
