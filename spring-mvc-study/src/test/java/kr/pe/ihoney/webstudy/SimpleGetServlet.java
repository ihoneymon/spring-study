package kr.pe.ihoney.webstudy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleGetServlet extends HttpServlet {

	private static final long serialVersionUID = -1073918119932833101L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String name = req.getParameter("name");
		
		res.getWriter().print("<html><body>");
		res.getWriter().print("Hello " + name);
		res.getWriter().print("</body></html>");
	}

}
