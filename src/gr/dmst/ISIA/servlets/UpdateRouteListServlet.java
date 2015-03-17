package gr.dmst.ISIA.servlets;

import gr.dmst.ISIA.app.Route;
import gr.dmst.ISIA.app.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * Servlet implementation class UpdateRouteListServlet
 *
 * The Servlet UpdateRouteListServlet receives a route's name and number and the
 * action that should be done, from an http request. It creates a user through the User
 * class. Calls the {@link gr.dmst.ISIA.app.User#addRouteToFavourites(gr.dmst.ISIA.app.Route route)}
 * or {@link gr.dmst.ISIA.app.User#deleteRouteFromFavourites(gr.dmst.ISIA.app.Route route)} method and
 * add/deletes the route to/from favourites depend on the action given.
 *
 * @author Thodoris Sotiropoulos
 */
public class UpdateRouteListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String routeNumber = new String(request.getParameter("routeNumber")
                .getBytes("ISO-8859-1"), "UTF-8");
        String routeName = new String(request.getParameter("routeName")
                .getBytes("ISO-8859-1"), "UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        User user = new User(session.getAttribute("user").toString());
        Route route = new Route(routeNumber, routeName);
        if (action.equals("deletion")) user.deleteRouteFromFavourites(route);
        else user.addRouteToFavourites(route);
    }
}
