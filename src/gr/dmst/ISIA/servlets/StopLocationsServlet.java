package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import gr.dmst.ISIA.app.Stop;

/**
 * Servlet implementation class RouteGuessingServlet
 *
 * The Servlet RouteGuessingServlet receives a user's input from an http request. Forwards the data
 * to the Route class to handle them and then calls {@link gr.dmst.ISIA.app.Stop#guessStops()} method. Then
 * it returns a JSONObject containing the names of all the possible stops that user may want
 * to search.
 *
 * @author Thodoris Sotiropoulos
 */
public class StopLocationsServlet extends HttpServlet {

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
		String name = request.getParameter("stopName");
		String stopName = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		Stop stop = new Stop(stopName);
		JSONObject locations = stop.guessStops();
		out.print(locations);
	}
}
