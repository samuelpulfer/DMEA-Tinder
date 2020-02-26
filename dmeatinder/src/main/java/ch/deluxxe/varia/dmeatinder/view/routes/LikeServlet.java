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
 * Servlet implementation class LikeServlet
 */
@WebServlet("/api/like/*")
public class LikeServlet extends DMEAServlet {
	private static final long serialVersionUID = 1L;
	private DMEAApi dmea = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeServlet() {
        super();
        dmea = new DMEAApiImpl();
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, String info) throws ServletException, IOException {
		try {
			String[] res = request.getRequestURI().split("/");
			Integer id = Integer.valueOf(res[res.length -1]);
			dmea.setLike(id, info);
			response.sendError(200, "event liked");
		} catch(Exception e) {
			response.sendError(400, "invalid input, object invalid");
		}
	}
	
	

}
