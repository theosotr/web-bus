package gr.dmst.ISIA.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gr.dmst.ISIA.app.User;

/**
 * Servlet implementation class CheckInputServlet
 *
 * The Servlet CheckInputServlet receives client's data (inputType and value)
 * from an http request. Data refers to the kind of input (email or username)
 * and its value Then forwards the data to the User class
 * {@link gr.dmst.ISIA.app.User#existInput(String inputType)} method to handle them. If data already
 * exist, shows error message, otherwise it redirects to the mainpage.
 *
 * @author Thodoris Sotiropoulos
 */
public class CheckInputServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		User user;
		String message;
		String inputType = request.getParameter("inputType");
		String value = request.getParameter("value");
		if (inputType.equals("username")) {
			user = new User(value, "", "");
			if (user.existInput(inputType))
				message = "Το " + inputType + " που δώσατε" + " υπάρχει ήδη";
			else
				message = "Το " + inputType + " σας είναι" + " αποδεκτό";
		} else {
			user = new User("", "", value);
			if (user.existInput(inputType))
				message = "Το " + inputType + " που δώσατε" + " υπάρχει ήδη";
			else
				message = "Το " + inputType + " σας είναι" + " αποδεκτό";
		}
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(message);
	}
}
