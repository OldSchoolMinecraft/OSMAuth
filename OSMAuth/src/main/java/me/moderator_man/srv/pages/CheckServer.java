package me.moderator_man.srv.pages;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;

public class CheckServer extends Page
{
	@Override
	public Response exec(Map<String, String> params)
	{
		if (!params.containsKey("username") || !params.containsKey("serverHash"))
		{
			JSONObject obj = new JSONObject();
			obj.put("error", "Missing parameters");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		String username = params.get("username");
		String serverHash = params.get("serverHash");

		JSONObject obj = new JSONObject();

		if (Server.getSessionManager().checkServer(username, serverHash))
		{
			obj.put("response", "YES");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		} else {
			obj.put("response", "NO");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
	}
}
