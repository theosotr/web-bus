package gr.dmst.ISIA.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dmst.ISIA.app.User;

/**
 * Servlet implementation class AccountCreationServlet
 *
 * The Servlet AccountCreationServlet receives client's data 
 * (name, last name, username, e-mail, password) from an
 * http request.
 * Then forwards the data to the User class to create an account
 * for this client (with the data given).
 *
 * @author Thodoris Sotiropoulos
 */
public class AccountCreationServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		String firstName = new String(request.getParameter("name")
				.getBytes("ISO-8859-1"), "UTF-8");
		String surname = new String(request.getParameter("lastName")
				.getBytes("ISO-8859-1"), "UTF-8");
		String username = new String(request.getParameter("username")
				.getBytes("ISO-8859-1"), "UTF-8");
		String email = new String(request.getParameter("email")
				.getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(request.getParameter("password")
				.getBytes("ISO-8859-1"), "UTF-8");
		User newUser = new User(username, password, email, firstName, surname);
		newUser.addUser();
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("Η εγγραγή σας πραγματοποιήθηκε με επιτυχία");
	}
}
