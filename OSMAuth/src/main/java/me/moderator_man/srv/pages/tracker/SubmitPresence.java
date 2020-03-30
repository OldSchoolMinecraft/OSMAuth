package me.moderator_man.srv.pages.tracker;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.config.Config;
import me.moderator_man.srv.pages.Page;

public class SubmitPresence extends Page
{
	public Response exec(Map<String, String> params)
	{
		if (!params.containsKey("username") || !params.containsKey("sessionId") || !params.containsKey("key"))
		{
			JSONObject obj = new JSONObject();
			obj.put("response", "Missing parameters");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		if (!params.get("key").equals(Config.instance.tracker_key))
		{
			JSONObject obj = new JSONObject();
			obj.put("response", "Invalid key");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		boolean sub = Server.getPlayerTracker().submit(params.get("username"), params.get("sessionId"));
		
		if (!sub)
		{
			JSONObject obj = new JSONObject();
			obj.put("response", "Failed to submit");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		JSONObject obj = new JSONObject();
		obj.put("response", "OK");
		return NanoHTTPD.newFixedLengthResponse(obj.toString());
	}
}
