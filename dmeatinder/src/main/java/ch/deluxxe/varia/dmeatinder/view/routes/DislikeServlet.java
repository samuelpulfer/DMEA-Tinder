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
 * Servlet implementation class DislikeServlet
 */
@WebServlet("/api/dislike/*")
public class DislikeServlet extends DMEAServlet {
	private static final long serialVersionUID = 1L;
	private DMEAApi dmea = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DislikeServlet() {
        super();
        dmea = new DMEAApiImpl();
    }

    @Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response, String info) throws ServletException, IOException {
		System.out.println(super.getURIValue(request));
		try {
			String[] res = request.getRequestURI().split("/");
			Integer id = Integer.valueOf(res[res.length -1]);
			dmea.setDislike(id, info);
			response.sendError(200, "event disliked");
		} catch(Exception e) {
			response.sendError(400, "invalid input, object invalid");
		}
	}

}
