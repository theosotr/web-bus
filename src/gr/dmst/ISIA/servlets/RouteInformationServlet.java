package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import gr.dmst.ISIA.app.Route;

/**
 * Servlet implementation class RouteInformationServlet
 *
 * The Servlet RouteInformationServlet receives route's number, route's direction
 * and the day of the trip from an http request. Forwards the data to the Route
 * class to handle them and then calls the
 * {@link gr.dmst.ISIA.app.Route#getRouteTrips(java.lang.String direction)},
 * {@link gr.dmst.ISIA.app.Route#findNextTrip(java.lang.String direction)}, {@link gr.dmst.ISIA.app.Route#getNextTrip()}
 * methods. Then, it returns a JSONArray containing route's trips with their departure
 * times and the position of the next trip if exists.
 *
 * @author Konstantinos Karakatsanis
 */
public class RouteInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String number = request.getParameter("routeNumber");
		String routeNumber = new String(number.getBytes("ISO-8859-1"), "UTF-8");
		String direction = request.getParameter("direction");
		String day = request.getParameter("day");
		Boolean nextTrip = Boolean.parseBoolean(request.getParameter("nextTrip"));
		Route route = new Route(routeNumber);
		if (day != null) route.setTripDay(day);
		route.initializeTrips(null);
		JSONArray departureTimes = route.getRouteTrips(direction);
		if (departureTimes.length() == 0)
			out.print(departureTimes);
		else {
			if (nextTrip) route.findNextTrip(direction);
			departureTimes.put(route.getNextTrip());
			departureTimes.put(direction);
			out.print(departureTimes);
		}
	}
}
