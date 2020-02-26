package ch.deluxxe.varia.dmeatinder.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONObject;

import ch.deluxxe.varia.dmeatinder.model.iface.DMEAApi;

public class DMEAApiImpl implements DMEAApi {
	
private DataSource ds;
	
	/**
	 * Initials DataSource
	 */
	public DMEAApiImpl() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oauthdb");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject getList() {
		return notYetImplemented();
	}

	@Override
	public String getCalendar() {
		// TODO Auto-generated method stub
		return "This should be an calendar...";
		
	}

	@Override
	public JSONObject setLike(int id) {
		return notYetImplemented();
	}

	@Override
	public JSONObject setSuperLike(int id) {
		return notYetImplemented();
	}

	@Override
	public JSONObject setDislike(int id) {
		return notYetImplemented();
	}

	@Override
	public JSONObject clear() {
		return notYetImplemented();
	}

	@Override
	public String getUserId(String userid) {
		return "123456";
	}
	
	private JSONObject notYetImplemented() {
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("message", "not yet implemented...");
		return jo;
	}

}
