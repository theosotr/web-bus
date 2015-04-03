package gr.dmst.ISIA.servlets;

import gr.dmst.ISIA.app.User;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CheckLoginServlet
 *
 * The Servlet CheckLoginServlet receives client's data (username and password)
 * from an http request. Then forwards the data to the User class
 * {@link gr.dmst.ISIA.app.User#checkLogin()} method to handle them. If data already
 * exist, shows error message, otherwise it redirects to the mainpage.
 *
 * @author Konstantinos Karakatsanis
 */
public class CheckLoginServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 *
	 * if CheckLoginServlet is called directly (ex: from url) it redirects
	 * to index page.
	 */
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String username = new String(request.getParameter("username")
				.getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(request.getParameter("password")
				.getBytes("ISO-8859-1"), "UTF-8");
		User user = new User(username, password);
		HttpSession session = request.getSession(true);
		if (user.checkLogin()) {
			session.setAttribute("user", user.getUsername());
			response.sendRedirect("mainpage.jsp");
		} else {
			session.setAttribute("errorMessage", "Λάθος κωδικός πρόσβασης");
			response.sendRedirect("index.jsp");
		}
	}
}
