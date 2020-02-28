package ch.deluxxe.varia.dmeatinder.model.iface;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DMEAApi {
	
	public JSONArray getList(String userid);
	public JSONObject setList(JSONArray ja);
	public String getCalendar(String userid);
	public JSONObject setLike(int id, String userid);
	public JSONObject setSuperLike(int id, String userid);
	public JSONObject setDislike(int id, String userid);
	public JSONObject clear(String userid);
	public String getUserId(String userid);

}
