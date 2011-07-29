package fi.arcusys.koku.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3070370281372795521L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String param1 = (String) req.getAttribute("test1");
		HttpSession session = req.getSession();
		session.setAttribute("param1", param1);
		System.out.println("rocky serverlt" + param1);
		res.sendRedirect("/portal/auth/portal/default/Message");
	}
}
