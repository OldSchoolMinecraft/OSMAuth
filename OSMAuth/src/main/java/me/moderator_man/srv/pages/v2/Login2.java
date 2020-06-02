package me.moderator_man.srv.pages.v2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Main;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.Util;
import me.moderator_man.srv.pages.Page;
import me.moderator_man.srv.session.v2.Session2;

public class Login2 extends Page
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
		    if (!existsv2(username))
		    {
		        // hack to make sure the legacy conversion goes through before the main check
		        if (handleLegacy(username, password))
		            return handle(username, password);
		        return handle(username, password);
		    } else {
		        return handle(username, password);
		    }
		} catch (Exception ex) {
			JSONObject obj = new JSONObject();
			obj.put("error", "An unknown error occurred!");
			ex.printStackTrace();
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
	}
	
	private Response handle(String username, String password)
	{
	    try
	    {
	        PreparedStatement stmt = Main.database.grabConnection().prepareStatement("SELECT * FROM usersv2 WHERE username = ?");
	        stmt.setString(1, username);
	        ResultSet res = stmt.executeQuery();
	        
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
	                log(String.format("Player '%s' tried to login, but was banned", username));
	                
	                JSONObject obj = new JSONObject();
	                obj.put("error", "Your account is banned!");
	                return NanoHTTPD.newFixedLengthResponse(obj.toString());
	            }
	            
	            String[] chunks = dbp.split(":");
	            String salt = chunks[0];
	            String hash = Util.hash(password, salt);
	            if (dbu.equals(username) && chunks[1].equals(hash))
	            {
	                log(String.format("Player '%s' successfully logged in", username));
	                
	                JSONObject obj = new JSONObject();
	                Session2 session = Server.getSessionManager2().createSession(username);
	                obj.put("username", session.username);
	                obj.put("sessionId", session.sessionId);
	                return NanoHTTPD.newFixedLengthResponse(obj.toString());
	            } else {
	                log(String.format("Player '%s' tried to login, but used an invalid password", username));
	                
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
	
	private boolean handleLegacy(String username, String password)
	{
        try
        {
            PreparedStatement stmt = Main.database.grabConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            
            if (res == null)
                return false;
            
            if (res.next())
            {
                String dbe = res.getString("email");
                String dbu = res.getString("username");
                String dbp = res.getString("password");
                boolean dbv = res.getBoolean("verified");
                boolean banned = res.getBoolean("banned");
                
                if (banned)
                {
                    PreparedStatement stmt2 = Main.database.grabConnection().prepareStatement("DELETE FROM user WHERE username = ?");
                    stmt2.setString(1, username);
                    stmt2.execute();
                    return false;
                }

                if (username.equalsIgnoreCase(dbu) && Util.sha256(password).equalsIgnoreCase(dbp))
                {
                    PreparedStatement stmt2 = Main.database.grabConnection().prepareStatement("INSERT INTO usersv2 (email, username, password, verified) VALUES (?, ?, ?, ?)");
                    stmt2.setString(1, dbe);
                    stmt2.setString(2, dbu);
                    String[] hash = Util.hash(password);
                    stmt2.setString(3, String.format("%s:%s", hash[1], hash[0]));
                    stmt2.setBoolean(4, dbv);
                    stmt2.execute();
                    log(String.format("Successfully converted user record for '%s'!", dbu));
                    return true;
                } else {
                    // they got the credentials wrong
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
	}
	
	private boolean existsv1(String username)
    {
        try
        {
            PreparedStatement stmt = Main.database.grabConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
	
	private boolean existsv2(String username)
    {
        try
        {
            PreparedStatement stmt = Main.database.grabConnection().prepareStatement("SELECT * FROM usersv2 WHERE username = ?");
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
	
	private void log(String msg)
	{
		System.out.println("[AUTH v2] " + msg);
	}
}
