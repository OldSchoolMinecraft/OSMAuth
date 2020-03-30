package me.moderator_man.srv.pages.tracker;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.config.Config;
import me.moderator_man.srv.pages.Page;
import me.moderator_man.srv.tracking.Timestamp;

public class TrackedPlayers extends Page
{
	public Response exec(Map<String, String> params)
	{
		if (!params.containsKey("key"))
		{
			JSONObject obj = new JSONObject();
			obj.put("response", "Missing parameters");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		if (!params.get("key").equals(Config.instance.tracker_key))
		{
			System.out.println(String.format("Someone tried using invalid key (used=%s, real=%s)", params.get("key"), Config.instance.tracker_key));
			JSONObject obj = new JSONObject();
			obj.put("response", "Invalid key");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		HashMap<String, Timestamp> tracked_players = Server.getPlayerTracker().getTrackedPlayers();
		JSONObject obj = new JSONObject();
		obj.put("players", tracked_players.keySet());
		return NanoHTTPD.newFixedLengthResponse(obj.toString());
	}
}
