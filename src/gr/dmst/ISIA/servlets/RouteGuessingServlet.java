package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import gr.dmst.ISIA.app.Route;

/**
 * Servlet implementation class RouteGuessingServlet
 *
 * The Servlet RouteGuessingServlet receives a user's input from an http request.
 * Forwards the data to the Route class to handle them and then calls
 * {@link gr.dmst.ISIA.app.Route#guessRoutes()} method. Then it returns a JSONObject
 * containing the names and numbers of all the possible routes that
 * user may want to search.
 *
 * @author Konstantinos Karakatsanis
 */
public class RouteGuessingServlet extends HttpServlet {
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
		String name = request.getParameter("routeName");
		String routeName = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		Route route = new Route(routeName, routeName);
		JSONObject guess = route.guessRoutes();
		out.print(guess);
	}
}
