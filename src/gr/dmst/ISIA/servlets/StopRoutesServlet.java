package gr.dmst.ISIA.servlets;

import gr.dmst.ISIA.app.Stop;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class StopRoutesServlet
 *
 * The Servlet StopRoutesServlet receives stop's coordinates (latitude
 * and longitude) from an http request. Forwards the data to the Stop
 * class to handle them and then calls {@link gr.dmst.ISIA.app.Stop#getRoutes(java.lang.String serviceDay)}
 * method.
 *
 * Then returns in JSON format the list of routes which pass through the stop.
 *
 * @author Thodoris Sotiropoulos
 */
public class StopRoutesServlet extends HttpServlet {

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
        String serviceDay = new String(request.getParameter("serviceDay")
                .getBytes("ISO-8859-1"), "UTF-8");
        String[] coordinates = {
                latitude,
                longitude
        };
        Stop stop = new Stop(coordinates);
        out.print(stop.getRoutes(serviceDay));
    }
}
