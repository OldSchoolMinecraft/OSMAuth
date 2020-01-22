package me.moderator_man.srv.pages;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.session.ThreadInvalidateSession;

public class JoinServer extends Page
{
	@Override
	public Response exec(Map<String, String> params)
	{
		if (!params.containsKey("username") || !params.containsKey("sessionId") || !params.containsKey("serverHash"))
		{
			JSONObject obj = new JSONObject();
			obj.put("error", "Missing parameters");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		String username = params.get("username");
		String sessionId = params.get("sessionId");
		String serverHash = params.get("serverHash");
		
		if (Server.getSessionManager().joinServer(username, sessionId, serverHash))
		{
			// soft invalidation after 3 seconds
			new ThreadInvalidateSession(username, true, 3).start();
			
			JSONObject obj = new JSONObject();
			obj.put("response", "ok");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", "Invalid session");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
	}
}
