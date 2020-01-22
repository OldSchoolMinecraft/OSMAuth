package me.moderator_man.srv.pages;

import java.sql.ResultSet;
import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Main;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.session.Session;

public class Login extends Page
{
	@Override
	public Response exec(Map<String, String> params)
	{
		if (!params.containsKey("username") || !params.containsKey("password"))
		{
			JSONObject obj = new JSONObject();
			obj.put("error", "Missing parameters");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		String username = params.get("username");
		String password = params.get("password");
		
		try
		{
			ResultSet res = Main.database.query(String.format("SELECT * FROM user WHERE username = '%s'", username));
			
			if (res == null)
			{
				JSONObject obj = new JSONObject();
				obj.put("error", "No database connection");
				return NanoHTTPD.newFixedLengthResponse(obj.toString());
			}
			
			if (res.next())
			{
				String dbu = res.getString("username");
				String dbp = res.getString("password");
				boolean banned = res.getBoolean("banned");
				
				if (banned)
				{
					JSONObject obj = new JSONObject();
					obj.put("error", "Your account is banned!");
					return NanoHTTPD.newFixedLengthResponse(obj.toString());
				}
				
				if (username.equalsIgnoreCase(dbu) && password.equalsIgnoreCase(dbp))
				{
					JSONObject obj = new JSONObject();
					Session session = Server.getSessionManager().createSession(username);
					obj.put("username", session.username);
					obj.put("sessionId", session.sessionId);
					return NanoHTTPD.newFixedLengthResponse(obj.toString());
				} else {
					JSONObject obj = new JSONObject();
					obj.put("error", "Invalid username or password");
					return NanoHTTPD.newFixedLengthResponse(obj.toString());
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "Invalid username or password!");
				return NanoHTTPD.newFixedLengthResponse(obj.toString());
			}
		} catch (Exception ex) {
			JSONObject obj = new JSONObject();
			obj.put("error", "An unknown error occurred!");
			ex.printStackTrace();
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
	}
}
