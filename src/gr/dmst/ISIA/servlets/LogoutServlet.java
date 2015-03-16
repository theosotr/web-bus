package gr.dmst.ISIA.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class LogoutServlet
 *
 * The Servlet LogoutServlet finishes a session, when called, and then
 * redirects to mainpage page.
 *
 * @author Thodoris Sotiropoulos
 */
public class LogoutServlet extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        if(session.getAttribute("user") != null)
            session.removeAttribute("user");
        response.sendRedirect("/ismgroup26/artifacts/" +
                "WebBus_war_exploded/");
    }
}
