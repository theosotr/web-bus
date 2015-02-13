package gr.dmst.ISIA.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gr.dmst.ISIA.model.DatabaseConnection;

import org.json.JSONObject;
import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.StopInTrip;
import gr.dmst.ISIA.app.Trip;
import gr.dmst.ISIA.app.User;

/**
 * A class that implements the {@link gr.dmst.ISIA.model.DAO.RouteDAO} interface which defines
 * the way that {@link gr.dmst.ISIA.app.Route} class communicates with database.
 */
public class RouteDAOImp implements RouteDAO {
	/**
	 * Default Constructor
	 */
	public RouteDAOImp() { }

	@Override
	public void getTrips(Route route, final String day) {
		List<Trip> trips = new ArrayList<>();
		final String query = "select trips.trip_id, direction, stop_name, "
				+ "stop_lat, stop_lon, stop_sequence, departure_time "
				+ "from calendar, stops, routes, trips, stop_times "
				+ "where calendar.service_id = trips.service_id and "
				+ "routes.route_id = trips.route_id and "
				+ "trips.trip_id = stop_times.trip_id and "
				+ "stop_times.stop_id = stops.stop_id and "
				+ "(route_number = ? or route_name = ?) and "
				+ day + " = 1 "
				+ "order by trips.trip_id, stop_sequence";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			PreparedStatement stm = con.prepareStatement(query);
			stm.setString(1, route.getRouteNumber());
			stm.setString(2, route.getRouteName());
			ResultSet rs = stm.executeQuery();
			String temp = null;
			int counter = -1;
			while (rs.next()) {
				if (!rs.getString("trip_id").equals(temp)) {
					counter++;
					temp = rs.getString("trip_id");
					trips.add(new Trip(route, rs.getString("direction"), temp));
					StopInTrip stop = new StopInTrip(rs.getString("stop_name"),
							rs.getString("stop_lon"), rs.getString("stop_lat"),
							rs.getInt("stop_sequence"),
							rs.getString("departure_time"));
					trips.get(counter).addStopInTrip(stop);
				} else {
					StopInTrip stop = new StopInTrip(rs.getString("stop_name"),
							rs.getString("stop_lon"), rs.getString("stop_lat"),
							rs.getInt("stop_sequence"),
							rs.getString("departure_time"));
					trips.get(counter).addStopInTrip(stop);
				}
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		route.setTrips(trips);
	}

	@Override
	public boolean getTrips(final User user, Route route, String day) {
		List<Trip> trips = new ArrayList<>();
		boolean isFavourite = false;
		final String query = "select trips.trip_id, direction, stop_name, "
				+ "stop_lat, stop_lon, stop_sequence, departure_time, "
				+ "user_routes.isFavourite from calendar, stops, routes, trips, "
				+ "stop_times, (select count(f.route_id) as isFavourite from routes as r, "
				+ "user as u, favourite_routes as f where r.route_number = ?"
				+ " and u.username = ? and "
				+ "r.route_id = f.route_id and u.user_id = f.user_id) as "
				+ "user_routes where calendar.service_id = trips.service_id and "
				+ "routes.route_id = trips.route_id and trips.trip_id = "
				+ "stop_times.trip_id and stop_times.stop_id = stops.stop_id and "
				+ "route_number = ? and " + day + " = 1 order by "
				+ "trips.trip_id, stop_sequence";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			PreparedStatement stm = con.prepareStatement(query);
			stm.setString(1, route.getRouteNumber());
			stm.setString(2, user.getUsername());
			stm.setString(3, route.getRouteNumber());
			ResultSet rs = stm.executeQuery();
			String temp = null;
			int counter = -1;
			while (rs.next()) {
				if (!rs.getString("trip_id").equals(temp)) {
					counter++;
					final int NOT_FAVOURITE = 0;
					isFavourite = rs.getInt("user_routes.isFavourite") !=
							NOT_FAVOURITE;
					temp = rs.getString("trip_id");
					trips.add(new Trip(route, rs.getString("direction"), temp));
					StopInTrip stop = new StopInTrip(rs.getString("stop_name"),
							rs.getString("stop_lon"), rs.getString("stop_lat"),
							rs.getInt("stop_sequence"),
							rs.getString("departure_time"));
					trips.get(counter).addStopInTrip(stop);
				} else {
					StopInTrip stop = new StopInTrip(rs.getString("stop_name"),
							rs.getString("stop_lon"), rs.getString("stop_lat"),
							rs.getInt("stop_sequence"),
							rs.getString("departure_time"));
					trips.get(counter).addStopInTrip(stop);
				}
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		route.setTrips(trips);
		return isFavourite;
	}

	@Override
	public void findRoute(Route route, String routeNumber) {
		final String query = "select distinct route_number, route_name, route_type "
				+ "from routes "
				+ "where route_number = ? ";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();

		try {
			PreparedStatement stm = con.prepareStatement(query);
			stm.setString(1, routeNumber);
			ResultSet rs = stm.executeQuery();
			final int TROLLEY_CODE = 800;
			final int METRO_CODE = 1;
			final int SUBURBAN_RAILWAY_CODE = 2;
			final int BUS_CODE = 3;
			if (rs.next()) {
				route.setRouteName(rs.getString("route_name"));
				route.setRouteNumber(rs.getString("route_number"));
				if (rs.getString("route_name").contains("ΚΥΚΛΙΚΗ"))
					route.setCircular(true);
				if (rs.getInt("route_type") == METRO_CODE) {
					route.setRouteType("Μετρό");
				} else if (rs.getInt("route_type") == SUBURBAN_RAILWAY_CODE) {
					route.setRouteType("Προαστιακός");
				} else if (rs.getInt("route_type") == BUS_CODE) {
					route.setRouteType("Λεωφορείο");
				} else if (rs.getInt("route_type") == TROLLEY_CODE) {
					route.setRouteType("Τρόλεϋ");
				} else {
					route.setRouteType("Τραμ");
				}
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
	}

	@Override
	public List<Route> guessRoute(Route route) {
		List<Route> routes = new ArrayList<Route>();
		String query = "select route_name, route_number "
				+ "from routes "
				+ "where route_name like ? or route_number like ?";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();
		try {
			PreparedStatement stm = con.prepareStatement(query);
			stm.setString(1, "%" + route.getRouteName() + "%");
			stm.setString(2, "%" + route.getRouteNumber() + "%");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				routes.add(new Route(rs.getString("route_number"),
						rs.getString("route_name")));
			}
			rs.close();
			stm.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		return routes;
	}

	@Override
	public JSONObject getRouteDays(Route route, String direction) {
		JSONObject json = new JSONObject();
		boolean daily = false;
		boolean friday = false;
		boolean saturday = false;
		boolean sunday = false;
		final String query = "select distinct monday, tuesday, "
				+ "wednesday, thursday, friday, saturday, sunday "
				+ "from routes, trips, calendar "
				+ "where route_number = ? and "
				+ "routes.route_id = trips.route_id and "
				+ "trips.service_id = calendar.service_id and "
				+ "direction = ? and "
				+ "(monday = 1 or tuesday = 1 or wednesday = 1 "
				+ "or thursday = 1 or friday = 1 or saturday = 1 "
				+ "or sunday = 1) "
				+ "order by sunday";
		DatabaseConnection connection = new DatabaseConnection();
		Connection con = connection.connect();

		try {
			PreparedStatement stm = con.prepareStatement(query);
			stm.setString(1, route.getRouteNumber());
			stm.setString(2, direction);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				for (String day : route.getDays()) {
					if (rs.getInt(day) == 1) {
						if (day.equalsIgnoreCase("friday"))
							friday = true;
						else if (day.equalsIgnoreCase("saturday"))
							saturday = true;
						else if (day.equalsIgnoreCase("sunday"))
							sunday = true;
						else
							daily = true;
					}
				}
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			connection.closeConnection();
		}
		json.put("daily", daily);
		json.put("friday", friday);
		json.put("saturday", saturday);
		json.put("sunday", sunday);
		json.put("direction", direction);
		return json;
	}
}
