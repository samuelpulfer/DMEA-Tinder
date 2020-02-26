package ch.deluxxe.varia.dmeatinder.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.deluxxe.varia.dmeatinder.model.iface.DMEAApi;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

public class DMEAApiImpl implements DMEAApi {
	
private DataSource ds;
	
	/**
	 * Initials DataSource
	 */
	public DMEAApiImpl() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/dmeadb");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public JSONArray getList() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONArray ja = new JSONArray();
		
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT * FROM event WHERE deleted IS NULL");
			rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject event = new JSONObject();
				event.put("id", rs.getInt("id"));
				event.put("name", rs.getString("name"));
				event.put("description", rs.getString("description"));
				ZonedDateTime zdt = ZonedDateTime.of(rs.getTimestamp("starttime").toLocalDateTime(), ZoneId.of("UTC"));
				
				event.put("startTime", zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));
				zdt = ZonedDateTime.of(rs.getTimestamp("endtime").toLocalDateTime(), ZoneId.of("UTC"));
				event.put("endTime", zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));
				event.put("status", rs.getString("status"));
				event.put("externalLink", rs.getString("externallink"));
				event.put("place", rs.getString("place"));
				ja.put(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		return ja;
	}

	@Override
	public String getCalendar(String userid) {
		System.out.println("calendar");
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT * FROM calendar WHERE userid=?");
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while(rs.next()) {
				// Create event
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("message", "deleted");
		//return jo;
		return "This should be an calendar...";
		
	}

	@Override
	public JSONObject setLike(int id, String userid) {
		System.out.println("like");
		return updatePreference(id,userid,1);
	}

	@Override
	public JSONObject setSuperLike(int id, String userid) {
		System.out.println("superlike");
		return updatePreference(id,userid,2);
	}

	@Override
	public JSONObject setDislike(int id, String userid) {
		System.out.println("dislike");
		return updatePreference(id,userid,0);
	}

	@Override
	public JSONObject clear(String userid) {
		System.out.println("clear");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("UPDATE preference SET deleted=CURRENT_TIMESTAMP WHERE userid=? AND deleted IS NULL");
			ps.setString(1, userid);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("message", "deleted");
		return jo;
	}

	@Override
	public String getUserId(String userid) {
		String user = "not available";
		if(userid != null) {
			user = userid;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT id FROM users WHERE id=?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			if(rs.next()) {
				/*
				rs.close();
				ps.close();
				ps = conn.prepareStatement("UPDATE users SET modified=CURRENT_TIMESTAMP WHERE id=?");
				ps.setString(1, user);
				ps.executeUpdate();
				*/
			} else {
				user = UUID.randomUUID().toString();
				rs.close();
				ps.close();
				ps = conn.prepareStatement("INSERT INTO users (id,created,modified) VALUES (?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");
				ps.setString(1, user);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		
		return user;
	}
	
	private JSONObject notYetImplemented() {
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("message", "not yet implemented...");
		return jo;
	}

	@Override
	public JSONObject setList(JSONArray ja) {
		if(ja == null || ja.length() == 0) {
			JSONObject jo = new JSONObject();
			jo.put("code", 1);
			jo.put("message", "empty json array...");
			return jo;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			for(Object o:ja) {
				JSONObject jo = (JSONObject) o;
				ps = conn.prepareStatement("SELECT id FROM event WHERE id=?");
				ps.setInt(1, jo.getInt("id"));
				rs = ps.executeQuery();
				if(rs.next()) {
					rs.close();
					ps.close();
					ps = conn.prepareStatement("UPDATE event SET name=?, description=?, starttime=?,endtime=?, status=?, externallink=?, place=?, modified=CURRENT_TIMESTAMP, deleted=? WHERE id=?");
					if(jo.optString("name").equals("")) {
						ps.setNull(1, Types.VARCHAR);
					} else {
						ps.setString(1, jo.optString("name"));
					}
					if(jo.optString("description").equals("")) {
						ps.setNull(2, Types.VARCHAR);
					} else {
						ps.setString(2, jo.optString("description"));
					}
					if(jo.optString("startTime").equals("")) {
						ps.setNull(3, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("startTime"));
						ps.setTimestamp(3, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					if(jo.optString("endTime").equals("")) {
						ps.setNull(4, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("endTime"));
						ps.setTimestamp(4, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					if(jo.optString("status").equals("")) {
						ps.setNull(5, Types.VARCHAR);
					} else {
						ps.setString(5, jo.optString("status"));
					}
					if(jo.optString("externalLink").equals("")) {
						ps.setNull(6, Types.VARCHAR);
					} else {
						ps.setString(6, jo.optString("externalLink"));
					}
					if(jo.optString("place").equals("")) {
						ps.setNull(7, Types.VARCHAR);
					} else {
						ps.setString(7, jo.optString("place"));
					}
					if(jo.optString("deleted").equals("")) {
						ps.setNull(8, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("deleted"));
						ps.setTimestamp(8, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					ps.setInt(9, jo.getInt("id"));
					ps.executeUpdate();
				} else {
					rs.close();
					ps.close();
					ps = conn.prepareStatement("INSERT INTO event (id,name,description,starttime,endtime,status,externallink,place,created,modified,deleted) VALUES (?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,?)");
					ps.setInt(1, jo.getInt("id"));
					if(jo.optString("name").equals("")) {
						ps.setNull(2, Types.VARCHAR);
					} else {
						ps.setString(2, jo.optString("name"));
					}
					if(jo.optString("description").equals("")) {
						ps.setNull(3, Types.VARCHAR);
					} else {
						ps.setString(3, jo.optString("description"));
					}
					if(jo.optString("startTime").equals("")) {
						ps.setNull(4, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("startTime"));
						ps.setTimestamp(4, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					if(jo.optString("endTime").equals("")) {
						ps.setNull(5, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("endTime"));
						ps.setTimestamp(5, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					if(jo.optString("status").equals("")) {
						ps.setNull(6, Types.VARCHAR);
					} else {
						ps.setString(6, jo.optString("status"));
					}
					if(jo.optString("externalLink").equals("")) {
						ps.setNull(7, Types.VARCHAR);
					} else {
						ps.setString(7, jo.optString("externalLink"));
					}
					if(jo.optString("place").equals("")) {
						ps.setNull(8, Types.VARCHAR);
					} else {
						ps.setString(8, jo.optString("place"));
					}
					if(jo.optString("deleted").equals("")) {
						ps.setNull(9, Types.TIMESTAMP_WITH_TIMEZONE);
					} else {
						ZonedDateTime zdt = ZonedDateTime.parse(jo.optString("deleted"));
						ps.setTimestamp(9, Timestamp.valueOf(zdt.toLocalDateTime()));
					}
					ps.executeUpdate();
					ps.close();
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject jo = new JSONObject();
			jo.put("code", 1);
			jo.put("message", e.getMessage());
			return jo;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		jo.put("message", "events successfully updated");
		return jo;
	}

	private JSONObject updatePreference(int id, String userid, int pref) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT id FROM preference WHERE userid=? AND event=? AND deleted IS NULL");
			ps.setString(1, userid);
			ps.setInt(2, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				int pk = rs.getInt("id");
				rs.close();
				ps.close();
				ps = conn.prepareStatement("UPDATE preference SET deleted=CURRENT_TIMESTAMP WHERE id=?");
				ps.setInt(1, pk);
				ps.executeUpdate();
			} else {
				rs.close();
			}
			ps.close();
			ps = conn.prepareStatement("INSERT INTO preference (userid,event,state,created,modified) VALUES (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");
			ps.setString(1, userid);
			ps.setInt(2, id);
			ps.setInt(3, pref);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("code", 0);
		if(pref == 0) {
			jo.put("message", "disliked");
		} else if(pref == 1) {
			jo.put("message", "liked");
		} else if(pref == 2) {
			jo.put("message", "superliked");
		} else {
			jo.put("message", "some unknown event...");
		}
		return jo;
	}
}
