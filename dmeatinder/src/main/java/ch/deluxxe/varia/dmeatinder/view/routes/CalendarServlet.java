package ch.deluxxe.varia.dmeatinder.view.routes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.deluxxe.varia.dmeatinder.model.DMEAApiImpl;
import ch.deluxxe.varia.dmeatinder.model.iface.DMEAApi;
import ch.deluxxe.varia.dmeatinder.view.DMEAServlet;

/**
 * Servlet implementation class CalendarServlet
 */
@WebServlet("/api/calendar")
public class CalendarServlet extends DMEAServlet {
	private static final long serialVersionUID = 1L;
	private DMEAApi dmea = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarServlet() {
        super();
        dmea = new DMEAApiImpl();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, String info) throws ServletException, IOException {
		setCORS(response);
		try {
			response.setHeader("Content-Type", "text/calendar");
			String calendar = dmea.getCalendar();
			response.getWriter().append(calendar);
		} catch(Exception e) {
			response.sendError(500, "could not create calendar");
		}
		
		
	}

}
