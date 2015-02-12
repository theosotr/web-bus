package gr.dmst.ISIA.model.DAO;

import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.Stop;
import gr.dmst.ISIA.app.User;

import java.util.List;

/**
 * An interface that defines the way that {@link gr.dmst.ISIA.app.User} class
 * communicates with database. User class needs data that are stored database
 * for many operation.
 *
 * For example, to check if credentials given by user during login operation are
 * right, it is needed to check database if a user matching these credentials
 * exists.
 *
 * It is implemented as Data Access Object
 *
 * @author Thodoris Sotiropoulos
 */
public interface UserDAO {
	/**
	 * Search database if a user exists as a criteria of input.
	 *
	 * Input might be email. Then checks database if a user exists with the given
	 * email stored in {@link gr.dmst.ISIA.app.User} class object given as parameter and
	 * returns true if exists, false otherwise.
	 *
	 * Input might be username. Then checks database if a user exists with the
	 * given stored in {@link gr.dmst.ISIA.app.User} class object given as parameter  and
	 * returns true if exists, false otherwise.
	 *
	 * Note: input parameter does not refer to the value of email or username,
	 * but defines if DAO has to search for a user with criteria of email or
	 * username.
	 *
	 * @param user user of system to look for
	 * @param input Defines the criteria which search will be based on. It can
	 *    			be either username or email.
	 * @return true if user exists, false otherwise.
	 */
	public boolean searchUser(User user, String input);

	/**
	 * Add user specified on the parameter to database. If user is added, it
	 * is considered registered to the system and now he can enter system with
	 * the credentials given during his registration.
	 *
	 * @param user User to be added to database.
	 */
	public void addUser(User user);

	/**
	 * A stop is added to the list of a user's favourites. Table of database
	 * (which this information is stored) is updated.
	 *
	 * @param stop stop to be added to the list of user's favourites.
	 * @param user who adds stop to his list of favourites.
	 */
	public void addStopToFavorites(Stop stop, User user);

	/**
	 * A route is added to the list of a user's favourites. Table of database
	 * (which this information is stored) is updated.
	 *
	 * @param route route to be added to the list of user's favourites.
	 * @param user who adds stop to his list of favourites.
	 */
	public void addRouteToFavourites(Route route, User user);

	/**
	 * The list of favourite stops of user (specified by the object given as
	 * parameter) is returned.
	 *
	 * @param user for whom list of favourites stops is retrieved.
	 * @return List of favourite stops of user.
	 */
	public List<Stop> getFavouriteStops(User user);

	/**
	 * The list of favourite routes of user (specified by the object given as
	 * parameter) is returned.
	 *
	 * @param user for whom list of favourites routes is retrieved.
	 * @return List of favourite routes of user.
	 */
	public List<Route> getFavouriteRoutes(User user);

	/**
	 * A stop is deleted from the user's (specified by the user object given as
	 * parameter) list of favourite stops.
	 *
	 * After that specific stop will be no longer favourite for this user.
	 *
	 * @param stop to be deleted.
	 * @param user who deletes the specific stop.
	 */
	public void deleteFavouriteStop(Stop stop, User user);

	/**
	 * A route is deleted from the user's (specified by the user object given as
	 * parameter) list of favourite routes.
	 *
	 * After that specific route will be no longer favourite for this user.
	 *
	 * @param route to be deleted.
	 * @param user who deletes the specific stop.
	 */
	public void deleteFavouriteRoute(Route route, User user);

	/**
	 * Checks database if a user with credentials specified in object given as
	 * parameter exists. This method is for the login operation and it tests
	 * the password and username of a user.
	 *
	 * If there is no record in database with username and password which are
	 * the same with the corresponding fields of user objects then false is
	 * returned, true otherwise.
	 *
	 * @param user whose credentials are checked.
	 * @return true if user's credentials exists in the database, false otherwise.
	 */
	public boolean checkCredentials(User user);
}
