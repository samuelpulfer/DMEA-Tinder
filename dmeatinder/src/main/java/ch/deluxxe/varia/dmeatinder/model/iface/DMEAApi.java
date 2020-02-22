package ch.deluxxe.varia.dmeatinder.model.iface;

import org.json.JSONObject;

public interface DMEAApi {
	
	public JSONObject getList();
	public String getCalendar();
	public JSONObject setLike(int id);
	public JSONObject setSuperLike(int id);
	public JSONObject setDislike(int id);
	public JSONObject clear();
	public String getUserId(String userid);

}
