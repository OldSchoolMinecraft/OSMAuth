package me.moderator_man.srv.pages.v2;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.pages.Page;

public class CheckServer2 extends Page
{
    public Response exec(Map<String, String> params)
    {
        if (!params.containsKey("username") || !params.containsKey("ip"))
        {
            JSONObject obj = new JSONObject();
            obj.put("error", "Missing parameters");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        }
        
        String username = params.get("username");
        String ip = params.get("ip");

        JSONObject obj = new JSONObject();

        if (Server.getSessionManager().checkServer(username, ip))
        {
            obj.put("response", "YES");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        } else {
            obj.put("response", "NO");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        }
    }
}
