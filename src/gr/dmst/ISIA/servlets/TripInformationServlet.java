package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import gr.dmst.ISIA.app.Route;

/**
 * Servlet implementation class TripInformationServlet
 *
 * The Servlet RouteInformationServlet receives route's number, route's direction
 * the day of the trip and the trip's sequence from an http request. Forwards the
 * data to the Route class to handle them and then calls
 * {@link gr.dmst.ISIA.app.Route#getTripInfo(int sequence, String direction)} method. Then,
 * it returns a JSONObject containing trip's information such as the duration of the
 * trip, the stops from which the trip passes and the time that passes, and the
 * coordinate of the stop.
 *
 * @author Konstantinos Karakatsanis
 */
public class TripInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * request, javax.servlet.http.HttpServletResponse response)
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
		int sequence = Integer.parseInt(request.getParameter("sequence"));
		Route route = new Route(routeNumber);
		if (day != null)
			route.setTripDay(day);
		route.initializeTrips(null);
		JSONObject info = route.getTripInfo(sequence, direction);
		out.print(info);
	}
}
