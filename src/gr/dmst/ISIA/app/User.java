package gr.dmst.ISIA.app;

import gr.dmst.ISIA.model.DAO.UserDAOImp;

import java.util.List;

/**
 * A class that represents user which is registered to the system. Not a guest
 * user.
 *
 * User who is registered to the system he has to give his credentials
 * (username and password) to enter system in order to have potential to use
 * extras operation of system such as search of optimal path betweem two points
 * storing routes and stops in a list.
 *
 * @author Thodoris Sotiropoulos
 */
public class User {
	/** username of user. */
	private String username;

	/** user password to enter system */
	private String password;

	/** email of user. */
	private String email;

	/** first name of user. */
	private String firstName;

	/** surname of user. */
	private String surname;

	/**
	 * Default Constructor.
	 */
	public User() {}

	/**
	 * Initializes user object to be added to system. All attributes of class
	 * are needed in order user registered in system.
	 *
	 * @param username username.
	 * @param password password to enter system.
	 * @param email email of user.
	 * @param firstName first name of user.
	 * @param surname surname of user.
	 */
	public User(String username, String password, String email, 
			String firstName, String surname) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.surname = surname;
	}

	/**
	 * Initializes User to check his email and username.
	 *
	 * @param username username.
	 * @param password password to enter system.
	 * @param email email of user.
	 */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	/**
	 * Initializes user object to check his credentials (username and password)
	 * at the login operation.
	 *
	 * @param username username.
	 * @param password password to enter system.
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Initializes user object with the username.
	 *
	 * @param username username.
	 */
	public User(String username) {
		this.username = username;
	}

	/**
	 * Search database if a user exists as a criteria of input. It uses Data
	 * Access Object to communicate with the database to access data.
	 *
	 * Input might be email. Then checks database if a user exists and returns
	 * true if exists, false otherwise.
	 *
	 * Input might be username. Then checks database if a user exists and returns
	 * true if exists, false otherwise.
	 *
	 * Note: input parameter does not refer to the value of email or username,
	 * but defines if DAO has to search for a user with criteria of email or
	 * username.
	 * @param inputType type to be checked, username or email
	 * @return true if exists, otherwise false
	 */
	public boolean existInput(String inputType) {
		//Initializes object which has access to database
		UserDAOImp dao = new UserDAOImp();
		//Check if input type exists
		return dao.searchUser(this, inputType);
	}

	/**
	 * Add user specified by object of class to the database. If user is added, it
	 * is considered registered to the system and now he can enter system with
	 * the credentials given during his registration.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 */
	public void addUser() {
		//Initializes object which has access to database
		UserDAOImp dao = new UserDAOImp();
		//Add user to database
		dao.addUser(this);
	}

	/**
	 * Checks database if a user with credentials specified in object of class
	 * exists. This method is for the login operation and it tests
	 * the password and username of a user.
	 *
	 * If there is no record in database with username and password which are
	 * the same with the corresponding fields of user objects then false is
	 * returned, true otherwise.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 *
	 * @return true if right credentials, otherwise false.
	 */
	public boolean checkLogin() {
		//Initializes object which has access to database.
		UserDAOImp dao = new UserDAOImp();
		//Check if user has given the right credentials to enter system.
		return dao.checkCredentials(this);
	}

	/**
	 * The list of favourite stops of user is returned.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 *
	 * @return the list of favourite stops of user.
	 */
	public List<Stop> getFavoriteStops() {
		UserDAOImp dao = new UserDAOImp();
		return dao.getFavouriteStops(this);
	}

	/**
	 * The list of favourite routes of user is returned.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 *
	 * @return the list of favourite routes of user.
	 */
	public List<Route> getFavouriteRoutes() {
		UserDAOImp dao = new UserDAOImp();
		return dao.getFavouriteRoutes(this);
	}

	/**
	 * A stop is deleted from the user's list of favourite stops. After that
	 * specific stop will be no longer favourite for this user.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 */
	public void deleteStopFromFavourites(Stop stop) {
		UserDAOImp dao = new UserDAOImp();
		dao.deleteFavouriteStop(stop, this);
	}

	/**
	 * A route is deleted from the user's list of favourite route. After that
	 * specific rout will be no longer favourite for this user.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 */
	public void deleteRouteFromFavourites(Route route) {
		UserDAOImp dao = new UserDAOImp();
		dao.deleteFavouriteRoute(route, this);
	}

	/**
	 * A stop is added to the list of a user's favourites.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 *
	 * @param stop to be added.
	 */
	public void addStopToFavourites(Stop stop) {
		UserDAOImp dao = new UserDAOImp();
		dao.addStopToFavorites(stop, this);
	}

	/**
	 * A route is added to the list of a user's favourites.
	 *
	 * It uses Data Access Object to communicate with the database to access data.
	 *
	 * @param route to be added.
	 */
	public void addRouteToFavourites(Route route) {
		UserDAOImp dao = new UserDAOImp();
		dao.addRouteToFavourites(route, this);
	}

	/**
	 * Get username.
	 *
	 * @return username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get user' s email.
	 *
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get password of user.
	 *
	 * @return password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get user's first name.
	 *
	 * @return first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Get user's surname.
	 *
	 * @return surname.
	 */
	public String getSurname() {
		return surname;
	}
}
