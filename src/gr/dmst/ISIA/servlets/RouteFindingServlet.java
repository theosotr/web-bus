package gr.dmst.ISIA.servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.User;

/**
 * Servlet implementation class RouteFindingServlet
 *
 * The Servlet RouteFindingServlet receives session's attribute and the route's number from an http
 * request. If the session's attribute is a username it creates a user through the User class.
 * Forwards the data to the Route class to find the route's info through the
 * {@link gr.dmst.ISIA.app.Route#getRouteNumber()},
 * {@link gr.dmst.ISIA.app.Route#showRouteName()},
 * {@link gr.dmst.ISIA.app.Route#reverseRouteName()},
 * {@link gr.dmst.ISIA.app.Route#getTrips()},
 * {@link gr.dmst.ISIA.app.Route#getRouteType()},
 * {@link gr.dmst.ISIA.app.Route#isCircular()} methods. Then it returns a JSONArray
 * containing route's number, route's name, route's reversed name, route's type,
 * route's number of stops and if the route is circular and/if the route is
 * favourite.
 *
 * @author Konstantinos Karakatsanis
 */
public class RouteFindingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String number = request.getParameter("routeNumber");
		String routeNumber = new String(number.getBytes("ISO-8859-1"), "UTF-8");
		HttpSession session = request.getSession(true);
		Route route = new Route(routeNumber);
		boolean isFavourite = false;
		if (session.getAttribute("user") == null) route.initializeTrips(null);
		else {
			User user = new User(session.getAttribute("user").toString());
			isFavourite = route.initializeTrips(user);
		}
		JSONArray info = new JSONArray();
		info.put(route.getRouteNumber());
		info.put(route.showRouteName());
		info.put(route.reverseRouteName());
		info.put(route.getTrips()[0].getSequenceOfStops2().length);
		info.put(route.getRouteType());
		info.put(route.isCircular());
		if (session.getAttribute("user") != null) info.put(isFavourite);
		out.print(info);
	}
}
