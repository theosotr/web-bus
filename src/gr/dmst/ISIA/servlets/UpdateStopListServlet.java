package gr.dmst.ISIA.servlets;

import gr.dmst.ISIA.app.Stop;
import gr.dmst.ISIA.app.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * Servlet implementation class UpdateStopListServlet
 *
 * The Servlet UpdateStopListServlet receives a stop's coordinates and the action
 * that should be done, from an http request. It creates a user through the User
 * class. Calls the {@link gr.dmst.ISIA.app.User#addStopToFavourites(gr.dmst.ISIA.app.Stop stop)}
 * or {@link gr.dmst.ISIA.app.User#deleteStopFromFavourites(gr.dmst.ISIA.app.Stop stop)} method and
 * add/deletes the stop to/from favourites depend on the action given.
 *
 * @author Thodoris Sotiropoulos
 */
public class UpdateStopListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String latitude = new String(request.getParameter("latitude")
                .getBytes("ISO-8859-1"), "UTF-8");
        String longitude = new String(request.getParameter("longitude")
                .getBytes("ISO-8859-1"), "UTF-8");
        String action = request.getParameter("action");
        String[] coordinates = {
                latitude,
                longitude
        };
        HttpSession session = request.getSession(true);
        User user = new User(session.getAttribute("user").toString());
        if (action.equals("deletion")) {
            Stop stop = new Stop(coordinates);
            user.deleteStopFromFavourites(stop);
        } else {
            String location = new String(request.getParameter("address")
                .getBytes("ISO-8859-1"), "UTF-8");
            Stop stop = new Stop(coordinates, location);
            user.addStopToFavourites(stop);
        }
    }
}
