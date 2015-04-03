package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dmst.ISIA.app.Route;

/**
 * Servlet implementation class ServiceDaysServlet
 *
 * The Servlet ServiceDaysServlet receives route's number and route's direction
 * from an http request. Forwards the data to the Route class to handle them and
 * then calls {@link gr.dmst.ISIA.app.Route#getRouteDays(java.lang.String direction)} method.
 * Then it returns a JSONObject containing the days in which a route performs trips.
 *
 * @author Konstantinos Karakatsanis
 */
public class ServiceDaysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String number = request.getParameter("routeNumber");
		String routeNumber = new String(number.getBytes("ISO-8859-1"), "UTF-8");
		String direction = request.getParameter("direction");
		Route route = new Route(routeNumber);
		route.initializeTrips(null);
		out.print(route.getRouteDays(direction));
	}
}
