package gr.dmst.ISIA.model;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

/**
 * This class defines the way that Java program (classes) and Database
 * communicate. A connection to database is essential in order Data Access Objects
 * to retrieve data from database.
 *
 * So, URL and credentials (username and password) have to be known to have
 * authorization to edit, display data. Every time a DAO class have to execute
 * a query to retrieve data, a connection to database must be established as
 * described above.
 *
 * Moreover, this class defines the connector of Java and MySQL server.
 *
 * @author Thodoris Sotiropoulos
 */
public class DatabaseConnection {
	/** URL of database. */
	private final String URL = "//database";

	/** Username to enter database. */
	private final String USERNAME = "//username";

	/** Password to enter database. */
	private final String PASSWORD = "//password";

	/** Object defines connection between Java and database. */
	private Connection connection = null;

	/**
	 * Constructor that calls the connector of Java and MySQL server.
	 */
	public DatabaseConnection() {
		this.findDriver();
	}

	/**
	 * Opens a connection to database with the given credentials and URL.
	 *
	 * @return Object defines connection between Java and database.
	 */
	public Connection connect() {
		try {
			this.connection = DriverManager.getConnection(this.URL, this.USERNAME, 
					this.PASSWORD);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		return this.connection;
	}

	/**
	 * Method which calls the connector of Java and MySQL server.
	 */
	public void findDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.print("ClassNotFoundException: ");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Closes connection between Java and MySQL. Connection is closed every
	 * time when a query is executed and data are retrieved.
	 */
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
