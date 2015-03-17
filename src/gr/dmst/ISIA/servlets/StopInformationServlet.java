package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import gr.dmst.ISIA.app.Stop;
import gr.dmst.ISIA.app.User;

/**
 * Servlet implementation class StopInformationServlet
 *
 * The Servlet StopInformationServlet receives stop's coordinates (latitude
 * and longitude) from an http request. Forwards the data to the Stop
 * class to handle them and then calls {@link gr.dmst.ISIA.app.Stop#getRoutes(gr.dmst.ISIA.app.User user)}
 * method.
 *
 * It returns all esential information in order client side code to represent
 * all information about stop such as list of routes which pass through the stop,
 * time when first and last trip of each route arrives at stop, next arrivals,
 * coordinates to mark stop on map, location of stop.
 *
 * @author Thodoris Sotiropoulos
 */
public class StopInformationServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String latitude = new String(request.getParameter("latitude")
				.getBytes("ISO-8859-1"), "UTF-8");
		String longitude = new String(request.getParameter("longitude")
				.getBytes("ISO-8859-1"), "UTF-8");
		String[] coordinates = {
				latitude,
				longitude
		};
		LinkedList<JSONObject> routes;
		Stop stop = new Stop(coordinates);
		HttpSession session = request.getSession(true);
		if (session.getAttribute("user") == null) {
			User user = new User();
			routes = stop.getRoutes(user);
		} else  {
			User user = new User(session.getAttribute("user").toString());
			routes = stop.getRoutes(user);
		}
		JSONArray json = new JSONArray();
		if(routes.isEmpty())
			out.print(json);
		else {
			json.put(routes);
			out.print(json);
		}
	}
}
