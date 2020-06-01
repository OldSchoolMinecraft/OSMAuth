package me.moderator_man.srv.pages.v2;

import java.util.Map;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import me.moderator_man.srv.Server;
import me.moderator_man.srv.pages.Page;
import me.moderator_man.srv.session.ThreadInvalidateSession;

public class JoinServer2 extends Page
{
    public Response exec(Map<String, String> params)
    {
        if (!params.containsKey("username") || !params.containsKey("sessionId") || !params.containsKey("ip"))
        {
            JSONObject obj = new JSONObject();
            obj.put("error", "Missing parameters");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        }
        
        String username = params.get("username");
        String sessionId = params.get("sessionId");
        String ip = params.get("ip");
        
        if (Server.getSessionManager().joinServer(username, sessionId, ip))
        {
            // soft invalidation after 3 seconds
            new ThreadInvalidateSession(username, true, 3).start();
            
            JSONObject obj = new JSONObject();
            obj.put("response", "OK");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        } else {
            JSONObject obj = new JSONObject();
            obj.put("error", "Invalid session");
            return NanoHTTPD.newFixedLengthResponse(obj.toString());
        }
    }
}
