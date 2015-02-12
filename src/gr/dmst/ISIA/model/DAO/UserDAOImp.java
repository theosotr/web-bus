package gr.dmst.ISIA.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import gr.dmst.ISIA.model.DatabaseConnection;

import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.Stop;
import gr.dmst.ISIA.app.User;

/**
 * A class that implements {@link gr.dmst.ISIA.model.DAO.UserDAO} interface which defines
 * the way that {@link gr.dmst.ISIA.app.User} class
 * communicates with database. User class needs data that are stored database
 * for many operation.
 *
 * For example, to check if credentials given by user during login operation are
 * right, it is needed to check database if a user matching these credentials
 * exists.
 *
 * It is implemented as Data Access Object.
 *
 * @author Thodoris Sotiropoulos
 */
public class UserDAOImp implements UserDAO {
	/**
	 * Default Constructor.
	 */
	public UserDAOImp() { }

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
	@Override
	public boolean searchUser(User user, String input) {
		boolean exists = false;
		final String query;
		PreparedStatement stm;
		ResultSet rs;
		if (input.equals("username"))
			query = "select username from user where username = ?";
		else
			query = "select username from user where email = ?";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			if (input.equals("username"))
				stm.setString(1, user.getUsername());
			else
				stm.setString(1, user.getEmail());
			rs = stm.executeQuery();
			while(rs.next())
				exists = true;
			rs.close();
			stm.close();
		} catch(SQLException sqlEx) {
			sqlEx.printStackTrace();
			return false;
		} finally {
			connection.closeConnection();
		}
		return exists;
	}

	/**
	 * Add user specified on the parameter to database. If user is added, it
	 * is considered registered to the system and now he can enter system with
	 * the credentials given during his registration.
	 *
	 * @param user User to be added to database.
	 */
	@Override
	public void addUser(User user) {
		PreparedStatement stm;
		final String query = "Insert into user(username, pswrd," 
				+ "first_name, surname, email) values (?, ?, ?, ?, ?)";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, user.getUsername());
			stm.setString(2, user.getPassword());
			stm.setString(3, user.getFirstName());
			stm.setString(4, user.getSurname());
			stm.setString(5, user.getEmail());
			stm.executeUpdate();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

	/**
	 * A stop is added to the list of a user's favourites. Table of database
	 * (which this information is stored) is updated.
	 *
	 * @param stop stop to be added to the list of user's favourites.
	 * @param user who adds stop to his list of favourites.
	 */
	@Override
	public void addStopToFavorites(Stop stop, User user) {
		PreparedStatement stm;
		final String query = "insert into favourite_stops " +
				"select stops.stop_id, user.user_id, ? " +
				"from stops, user " +
				"where username = ? and stop_lat = ? and stop_lon = ?";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			String[] coordinates = stop.getCoordinates();
			stm = con.prepareStatement(query);
			stm.setString(1, stop.getLocation());
			stm.setString(2, user.getUsername());
			stm.setString(3, coordinates[0]);
			stm.setString(4, coordinates[1]);
			stm.executeUpdate();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

	/**
	 * A route is added to the list of a user's favourites. Table of database
	 * (which this information is stored) is updated.
	 *
	 * @param route route to be added to the list of user's favourites.
	 * @param user who adds stop to his list of favourites.
	 */
	@Override
	public void addRouteToFavourites(Route route, User user) {
		PreparedStatement stm;
		final String query = "insert into favourite_routes " +
				"select routes.route_id, user.user_id " +
				"from routes, user " +
				"where username = ? and route_number = ?";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, user.getUsername());
			stm.setString(2, route.getRouteNumber());
			stm.executeUpdate();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

	/**
	 * The list of favourite stops of user (specified by the object given as
	 * parameter) is returned.
	 *
	 * @param user for whom list of favourites stops is retrieved.
	 * @return List of favourite stops of user.
	 */
	@Override
	public List<Stop> getFavouriteStops(User user) {
		PreparedStatement stm;
		List<Stop> favoriteStops = new LinkedList<>();
		final String query = "select stop_name, stop_lat, stop_lon, stop_address from stops, " +
				"favourite_stops, user where user.username = ? and " +
				"user.user_id = favourite_stops.user_id and " +
				"stops.stop_id = favourite_stops.stop_id";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, user.getUsername());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String stopName = rs.getString("stop_name");
				String location = rs.getString("stop_address");
				String[] coordinates = {
						rs.getString("stop_lat"),
						rs.getString("stop_lon")
				};
				Stop stop = new Stop(stopName, location, coordinates);
				favoriteStops.add(stop);
			}
			rs.close();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		return favoriteStops;
	}

	/**
	 * The list of favourite routes of user (specified by the object given as
	 * parameter) is returned.
	 *
	 * @param user for whom list of favourites routes is retrieved.
	 * @return List of favourite routes of user.
	 */
	@Override
	public List<Route> getFavouriteRoutes(User user) {
		PreparedStatement stm;
		List<Route> favoriteRoutes = new LinkedList<>();
		final String query = "select route_number, route_name from routes, " +
				"favourite_routes, user where user.username = ? and " +
				"user.user_id = favourite_routes.user_id and " +
				"routes.route_id = favourite_routes.route_id";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, user.getUsername());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String routeNumber = rs.getString("route_number");
				String routeName = rs.getString("route_name");
				Route route = new Route(routeNumber, routeName);
				favoriteRoutes.add(route);
			}
			rs.close();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		return favoriteRoutes;
	}

	/**
	 * A stop is deleted from the user's (specified by the user object given as
	 * parameter) list of favourite stops.
	 *
	 * After that specific stop will be no longer favourite for this user.
	 *
	 * @param stop to be deleted.
	 * @param user who deletes the specific stop.
	 */
	@Override
	public void deleteFavouriteStop(Stop stop, User user) {
		PreparedStatement stm;
		final String query = "delete from favourite_stops where stop_id in" +
				" (SELECT stop_id FROM stops WHERE stop_lat = ? and stop_lon = ?)";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			String[] coordinates = stop.getCoordinates();
			stm = con.prepareStatement(query);
			stm.setString(1, coordinates[0]);
			stm.setString(2, coordinates[1]);
			stm.executeUpdate();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

	/**
	 * A route is deleted from the user's (specified by the user object given as
	 * parameter) list of favourite routes.
	 *
	 * After that specific route will be no longer favourite for this user.
	 *
	 * @param route to be deleted.
	 * @param user who deletes the specific stop.
	 */
	@Override
	public void deleteFavouriteRoute(Route route, User user) {
		PreparedStatement stm;
		final String query = "delete from favourite_routes where route_id in" +
				" (SELECT route_id FROM routes WHERE route_number = ?)";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			stm = con.prepareStatement(query);
			stm.setString(1, route.getRouteNumber());
			stm.executeUpdate();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

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
	@Override
	public boolean checkCredentials(User user ) {
		PreparedStatement stm;
		ResultSet rs;
		boolean exists = false;
		final String query = "select username from user where username = ?"
				+ " and pswrd = ?";
		DatabaseConnection connect = new DatabaseConnection();
		Connection con = connect.connect();
		if (con == null) return false;
		try {
			stm = con.prepareStatement(query);
			// replacing the first ? with userName
			stm.setString(1, user.getUsername());
			// replacing the second ? with userPassword
			stm.setString(2, user.getPassword());
			// execute query
			rs = stm.executeQuery();
			while (rs.next())
				exists = true;
			rs.close();
			stm.close();

		} catch (SQLException sqlEx) {
			return false;
		} finally {
			connect.closeConnection();
		}
		return exists;
	}
}
