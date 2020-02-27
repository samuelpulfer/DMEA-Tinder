package ch.deluxxe.varia.dmeatinder.view.routes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.deluxxe.varia.dmeatinder.model.DMEAApiImpl;
import ch.deluxxe.varia.dmeatinder.model.Helper;
import ch.deluxxe.varia.dmeatinder.model.iface.DMEAApi;
import ch.deluxxe.varia.dmeatinder.view.DMEAServlet;

/**
 * Servlet implementation class ListServlet
 */
@WebServlet("/api/list")
public class ListServlet extends DMEAServlet {
	private static final long serialVersionUID = 1L;
	private DMEAApi dmea = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListServlet() {
        super();
        dmea = new DMEAApiImpl();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, String info) throws ServletException, IOException {
	
		response.setHeader("Content-Type", "application/json");
		response.getWriter().append(dmea.getList().toString());
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response, String info) throws ServletException, IOException {
    	response.setHeader("Content-Type", "application/json");
		response.getWriter().append(dmea.setList(Helper.requestToJSONArray(request)).toString());
    }


	
	

}
