package ch.deluxxe.varia.dmeatinder.model;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class Helper {
	
	public static JSONObject requestToJSON(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			return null;
		}
		return new JSONObject(jb.toString());
	}
	
	public static JSONArray requestToJSONArray(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			return null;
		}
		return new JSONArray(jb.toString());
	}
}
