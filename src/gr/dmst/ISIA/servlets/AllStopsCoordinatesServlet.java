package gr.dmst.ISIA.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dmst.ISIA.app.Stop;

/**
 * Servlet implementation class AllStopsCoordinatesServlet
 *
 * The Servlet AllStopsCoordinatesServlet returns the coordinates
 * for all the existing stops {@link Stop#getAllCoordinates()} method.
 *
 * @author Thodoris Sotiropoulos
 */
public class AllStopsCoordinatesServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(Stop.getAllCoordinates());
	}
}
