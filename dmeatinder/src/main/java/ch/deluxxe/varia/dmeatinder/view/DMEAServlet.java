package ch.deluxxe.varia.dmeatinder.view;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.deluxxe.varia.dmeatinder.model.DMEAApiImpl;
import ch.deluxxe.varia.dmeatinder.model.iface.DMEAApi;

public abstract class DMEAServlet extends HttpServlet {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	private static final String METHOD_DELETE = "DELETE";
	private static final String METHOD_HEAD = "HEAD";
	private static final String METHOD_GET = "GET";
	private static final String METHOD_OPTIONS = "OPTIONS";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_TRACE = "TRACE";

	private boolean ALLOW_GET = false;
	private boolean ALLOW_HEAD = false;
	private boolean ALLOW_POST = false;
	private boolean ALLOW_PUT = false;
	private boolean ALLOW_DELETE = false;
	private boolean ALLOW_TRACE = true;
	private boolean ALLOW_OPTIONS = true;

	private String allowedMethods = null;
	private DMEAApi dmea = null;

	public DMEAServlet() {
		super();
		getAllDeclaredMethods(this.getClass());
		dmea = new DMEAApiImpl();
	}

	/**
	 * Gets all declared Methods of class c.
	 * 
	 * @param c The class which extends OAuthServlet.
	 */
	private void getAllDeclaredMethods(Class<? extends DMEAServlet> c) {

		Class<?> clazz = c;
		Method[] allMethods = null;

		while (!clazz.equals(DMEAServlet.class)) {
			Method[] thisMethods = clazz.getDeclaredMethods();
			if (allMethods != null && allMethods.length > 0) {
				Method[] subClassMethods = allMethods;
				allMethods = new Method[thisMethods.length + subClassMethods.length];
				System.arraycopy(thisMethods, 0, allMethods, 0, thisMethods.length);
				System.arraycopy(subClassMethods, 0, allMethods, thisMethods.length, subClassMethods.length);
			} else {
				allMethods = thisMethods;
			}

			clazz = clazz.getSuperclass();
		}

		if (allMethods == null) {
			return;
		}
		for (int i = 0; i < allMethods.length; i++) {
			String methodName = allMethods[i].getName();

			if (methodName.equals("doGet")) {
				ALLOW_GET = true;
				ALLOW_HEAD = true;
			} else if (methodName.equals("doPost")) {
				ALLOW_POST = true;
			} else if (methodName.equals("doPut")) {
				ALLOW_PUT = true;
			} else if (methodName.equals("doDelete")) {
				ALLOW_DELETE = true;
			}

		}

		// we know "allow" is not null as ALLOW_OPTIONS = true
		// when this method is invoked
		StringBuilder allow = new StringBuilder();
		if (ALLOW_GET) {
			allow.append(METHOD_GET);
		}
		if (ALLOW_HEAD) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_HEAD);
		}
		if (ALLOW_POST) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_POST);
		}
		if (ALLOW_PUT) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_PUT);
		}
		if (ALLOW_DELETE) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_DELETE);
		}
		if (ALLOW_TRACE) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_TRACE);
		}
		if (ALLOW_OPTIONS) {
			if (allow.length() > 0) {
				allow.append(", ");
			}
			allow.append(METHOD_OPTIONS);
		}
		this.allowedMethods = allow.toString();
	}

	/**
	 * Sets allowed origin for CORS to *.
	 * 
	 * @param resp
	 */
	protected void setCORS(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Allow", this.allowedMethods);
		setCORS(resp);
		resp.setHeader("Access-Control-Allow-Methods", this.allowedMethods);
		resp.setHeader("Access-Control-Allow-Headers", "Accept, Authorization, Content-Type");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCORS(resp);
		if (!ALLOW_GET) {
			super.doGet(req, resp);
		}
		String userid = cookiemonster(req, resp);
		doGet(req, resp, userid);
	}

	/**
	 * Will be called after a doGet(HttpServletRequest req, HttpServletResponse
	 * resp) ONLY if doDelete(HttpServletRequest req, HttpServletResponse resp) was
	 * not overwritten! Override this function to handle GET request.
	 * 
	 * @param req  The request.
	 * @param resp The response.
	 * @param info The OAuthInfo about the request.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp, String userid)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCORS(resp);
		if (!ALLOW_POST) {
			super.doPost(req, resp);
		}
		String userid = cookiemonster(req, resp);
		doPost(req, resp, userid);
	}

	/**
	 * Will be called after a doPost(HttpServletRequest req, HttpServletResponse
	 * resp) ONLY if doDelete(HttpServletRequest req, HttpServletResponse resp) was
	 * not overwritten! Override this function to handle POST request.
	 * 
	 * @param req  The request.
	 * @param resp The request.
	 * @param info The OAuthInfo about the request.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp, String userid)
			throws ServletException, IOException {

	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCORS(resp);
		if (!ALLOW_PUT) {
			super.doPut(req, resp);
		}
		String userid = cookiemonster(req, resp);
		doPut(req, resp, userid);
	}

	/**
	 * Will be called after a doPut(HttpServletRequest req, HttpServletResponse
	 * resp) ONLY if doDelete(HttpServletRequest req, HttpServletResponse resp) was
	 * not overwritten! Override this function to handle PUT request.
	 * 
	 * @param req  The request.
	 * @param resp The request.
	 * @param info The OAuthInfo about the request.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp, String userid)
			throws ServletException, IOException {

	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCORS(resp);
		if (!ALLOW_DELETE) {
			super.doPut(req, resp);
		}
		String userid = cookiemonster(req, resp);
		doDelete(req, resp, userid);
	}

	/**
	 * Will be called after a doDelete(HttpServletRequest req, HttpServletResponse
	 * resp) ONLY if doDelete(HttpServletRequest req, HttpServletResponse resp) was
	 * not overwritten! Override this function to handle DELETE request.
	 * 
	 * @param req  The request.
	 * @param resp The request.
	 * @param info The OAuthInfo about the request.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp, String userid)
			throws ServletException, IOException {

	}

	protected String getURIValue(HttpServletRequest req) {
		String[] res = req.getRequestURI().split("/");
		return res[res.length - 1];
	}

	private String cookiemonster(HttpServletRequest req, HttpServletResponse resp) {
		String user = null;
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("user_session")) {
					user = c.getValue();
					//System.out.println("Cookier found with value: " + user);
				}
			}
		}
		String userid = dmea.getUserId(user);

		Cookie cookie = new Cookie("user_session", userid);
		cookie.setDomain("dmea.deluxxe.ch");
		cookie.setPath("/");
		cookie.setMaxAge(365 * 24 * 60 * 60);
		resp.addCookie(cookie);

		Cookie cookie2 = new Cookie("user_session", userid);
		cookie2.setDomain("localhost");
		cookie.setPath("/");
		cookie2.setMaxAge(365 * 24 * 60 * 60);
		resp.addCookie(cookie2);

		return userid;
	}
}
