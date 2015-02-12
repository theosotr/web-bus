package gr.dmst.ISIA.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import gr.dmst.ISIA.model.DatabaseConnection;

import gr.dmst.ISIA.app.*;

/**
 * A class that implements the {@link gr.dmst.ISIA.model.DAO.StopDAO} interface which defines
 * the way that {@link gr.dmst.ISIA.app.Stop} class communicates with database.
 * Stop class needs data that are stored database for many operation.
 *
 * For example, if a user wants to see information about a stop such as list of
 * its routes etc it is needed all required data to be retrieved from database
 * by executing queries.
 *
 * It is implemented as Data Access Object.
 *
 * @author Thodoris Sotiropoulos
 */
public class StopDAOImp implements StopDAO {

    /**
     * Default Constructor
     */
    public StopDAOImp() {}

    /**
     * Returns all data from table stops of database and initializes Stop objects
     * accordingly.
     *
     * @return List of Stop objects
     */
    @Override
    public List<Stop> getAllStops() {
        PreparedStatement stm;
        ResultSet rs;
        List<Stop> stops = new LinkedList<>();
        final String query = "select stop_name, stop_lat, stop_lon from stops";
        // create a DatabaseConnection object
        DatabaseConnection connection = new DatabaseConnection();
        // Get connection to database
        Connection con = connection.connect();
        try {
            stm = con.prepareStatement(query);
            rs = stm.executeQuery();

            while (rs.next()) {
                Stop stop = new Stop(rs.getString("stop_name"),
                        rs.getString("stop_lon"), rs.getString("stop_lat"));
                stops.add(stop);
            }

            rs.next();
            stm.close();

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();

        } finally {
            // close connection
            connection.closeConnection();
        }
        return stops;
    }

    /**
     * Gets data from database about every route which passes from a stop, every
     * trip of routes, and every stop which route passes from this trip with the
     * departure time. It initializes objects accordingly and sets list of routes
     * for the specific stop.
     *
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
    public void getRoutesOfStop(Stop stop, String date) {

        PreparedStatement stm;
        ResultSet rs;
        LinkedList<Route> routes = new LinkedList<>();
        String previousRoute = "";
        final String query = "select trips.trip_id, route_number, route_name," +
                " direction, "
                + "departure_time from calendar, stops, routes, trips, "
                + "stop_times where(" + date + " = 1 and "
                + "calendar.service_id = trips.service_id and "
                + "routes.route_id = trips.route_id and trips.trip_id = "
                + "stop_times.trip_id and stop_times.stop_id = stops.stop_id "
                + "and stop_lon = ? and stop_lat = ?) order by route_number";
        // Initializes DatabaseConnection object
        DatabaseConnection connection = new DatabaseConnection();
        // Get connection to database
        Connection con = connection.connect();
        Route route = null;
        try {
            stm = con.prepareStatement(query);
            stm.setString(1, stop.getCoordinates()[1]);
            stm.setString(2, stop.getCoordinates()[0]);
            rs = stm.executeQuery();
            while (rs.next()) {
                String number = rs.getString("route_number");
                if(previousRoute.equals("")) {
                    route = new Route(number, rs.getString("route_name"));
                } else {
                    if(!previousRoute.equals(number)) {
                        routes.add(route);
                        route = new Route(rs.getString("route_number"),
                                rs.getString("route_name"));
                    }
                }
                previousRoute = number;
                Trip trip = new Trip(route, rs.getString("direction"),
                        rs.getString("trips.trip_id"));
                // add stop to that trip object
                trip.addStop(stop, rs.getString("departure_time"));
                if (route != null)
                    //add trip to that route object
                    route.addTrip(trip);
            }
            rs.close();
            stm.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(route != null)
                routes.add(route);
            // set List of Routes
            stop.setRoutes(routes);
            // close connection
            connection.closeConnection();
        }
    }

    /**
     * It is the same as with method above.
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
    @Override
    public boolean getRoutesOfStop(User user, Stop stop, DateManager date) {
        PreparedStatement stm;
        ResultSet rs;
        LinkedList<Route> routes = new LinkedList<>();
        String previousRoute = "";
        boolean isFavourite  = false;
        final String query = "select trips.trip_id, route_number, route_name," +
                " direction, departure_time, user_stops.isFavourite  from " +
                "calendar, stops, routes, trips, stop_times, (select count(f" +
                ".stop_id) as isFavourite from stops as s, user as u, " +
                "favourite_stops as f where s.stop_lat = ? " +
                "and s.stop_lon = ? and u.username = ?" +
                " and s.stop_id = f.stop_id and u.user_id = f.user_id ) as " +
                "user_stops where(calendar.monday = 1 and  calendar" +
                ".service_id = trips.service_id and routes.route_id = trips" +
                ".route_id and trips.trip_id = stop_times.trip_id and " +
                "stop_times.stop_id = stops.stop_id and stop_lat = ?"  +
                "and stop_lon = ?) group by " +
                "trips.trip_id, route_number, route_name, direction, " +
                "departure_time order by route_number";
        // Initializes DatabaseConnection object
        DatabaseConnection connection = new DatabaseConnection();
        // Get connection to database
        Connection con = connection.connect();
        Route route = null;
        try {
            stm = con.prepareStatement(query);
            stm.setString(1, stop.getCoordinates()[0]);
            stm.setString(2, stop.getCoordinates()[1]);
            stm.setString(3, user.getUsername());
            stm.setString(4, stop.getCoordinates()[0]);
            stm.setString(5, stop.getCoordinates()[1]);
            rs = stm.executeQuery();
            while (rs.next()) {
                String number = rs.getString("route_number");
                final int IS_FAVOURITE = 1;
                isFavourite = rs.getInt("user_stops.isFavourite") ==
                        IS_FAVOURITE;
                if (previousRoute.equals("")) {
                    route = new Route(number, rs.getString("route_name"));
                } else {
                    if(!previousRoute.equals(number)) {
                        routes.add(route);
                        route = new Route(rs.getString("route_number"),
                                rs.getString("route_name"));
                    }
                }
                previousRoute = number;
                Trip trip = new Trip(route, rs.getString("direction"),
                        rs.getString("trips.trip_id"));
                // add stop to that trip object
                trip.addStop(stop, rs.getString("departure_time"));
                if (route != null)
                    //add trip to that route object
                    route.addTrip(trip);
            }
            rs.close();
            stm.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(route != null)
                routes.add(route);
            // set List of Routes
            stop.setRoutes(routes);
            // close connection
            connection.closeConnection();
        }
        return isFavourite;
    }

    /**
     * Gets all stop which matches with the characters given by user from database
     * initializes Stop objects accordingly and returns List of Stop Objects
     *
     * More specifically, returns all stops that begins with the String value
     * which is stored in the field stopName of stop object given as parameter.
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
    public LinkedList<Stop> guessStop(Stop stop) {
        PreparedStatement stm;
        ResultSet rs;
        LinkedList<Stop> stops = new LinkedList<>();
        final String query = "select stop_name, stop_desc, stop_lat, stop_lon" +
                " from stops where stop_name like ? order by stop_name, stop_desc";
        //Initializes DatabaseConnection object
        DatabaseConnection connection = new DatabaseConnection();
        //Get connection to database
        Connection con = connection.connect();
        String previousLocation = "";
        String previousStop = "";
        try {
            stm = con.prepareStatement(query);
            stm.setString(1, stop.getStopName() + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                String currentLocation = rs.getString("stop_desc");
                if (currentLocation != null &&
                        (!previousLocation.equals(currentLocation) ||
                        !previousStop.equals(rs.getString("stop_name")))) {
                    previousLocation = currentLocation;
                    previousStop = rs.getString("stop_name");
                    currentLocation = currentLocation + " A";
                } else
                    currentLocation = currentLocation + " B";
                if (rs.getString("stop_desc")!= null) {
                    String[] coordinates = {
                            rs.getString("stop_lat"),
                            rs.getString("stop_lon")
                    };
                    Stop guessedStop = new Stop(rs.getString("stop_name"),
                            currentLocation, coordinates);
                    stops.add(guessedStop);
                }
            }
            rs.close();
            stm.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();

        } finally {
            // close connection
            connection.closeConnection();
        }
        return stops;
    }
}
