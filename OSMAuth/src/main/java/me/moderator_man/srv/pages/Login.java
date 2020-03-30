package me.moderator_man.srv.pages;

import java.sql.PreparedStatement;
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
			return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", obj.toString());
		}
		
		String username = params.get("username");
		String password = params.get("password");
		
		try
		{
			PreparedStatement stmt = Main.database.grabConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
			stmt.setString(1, username);
			ResultSet res = stmt.executeQuery();
			
			//ResultSet res = Main.database.query("SELECT * FROM user WHERE username = ?", username);
			
			if (res == null)
			{
				JSONObject obj = new JSONObject();
				obj.put("error", "No database connection");
				return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", obj.toString());
			}
			
			if (res.next())
			{
				String dbu = res.getString("username");
				String dbp = res.getString("password");
				boolean banned = res.getBoolean("banned");
				
				if (banned)
				{
					log(String.format("Player '%s' tried to login, but was banned", username));
					
					JSONObject obj = new JSONObject();
					obj.put("error", "Your account is banned!");
					return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN, "application/json", obj.toString());
				}

				if (username.equalsIgnoreCase(dbu) && password.equalsIgnoreCase(dbp))
				{
					log(String.format("Player '%s' successfully logged in", username));
					
					JSONObject obj = new JSONObject();
					Session session = Server.getSessionManager().createSession(username);
					obj.put("username", session.username);
					obj.put("sessionId", session.sessionId);
					return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "application/json", obj.toString());
				} else {
					log(String.format("Player '%s' tried to login, but used an invalid password", username));
					
					JSONObject obj = new JSONObject();
					obj.put("error", "Invalid username or password");
					return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", obj.toString());
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("error", "Invalid username or password!");
				return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", obj.toString());
			}
		} catch (Exception ex) {
			JSONObject obj = new JSONObject();
			obj.put("error", "An unknown error occurred!");
			ex.printStackTrace();
			return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", obj.toString());
		}
	}
	
	private void log(String msg)
	{
		System.out.println("[AUTH] " + msg);
	}
}
