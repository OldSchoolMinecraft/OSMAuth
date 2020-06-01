package me.moderator_man.srv;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import me.moderator_man.srv.pages.CheckServer;
import me.moderator_man.srv.pages.JoinServer;
import me.moderator_man.srv.pages.Login;
import me.moderator_man.srv.pages.Page;
import me.moderator_man.srv.pages.PageManager;
import me.moderator_man.srv.pages.v2.CheckServer2;
import me.moderator_man.srv.pages.v2.JoinServer2;
import me.moderator_man.srv.session.SessionManager;
import me.moderator_man.srv.session.v2.SessionManager2;

public class Server extends NanoHTTPD
{
	//private static final Logger log = Logger.getLogger(Server.class.getName());
	
	private PageManager pm;
	private static SessionManager sm;
	private static SessionManager2 smv2;
	
	public Server()
	{
		super(8080);
		
		pm = new PageManager();
		sm = new SessionManager();
		smv2 = new SessionManager2();
		
		pm.register("/login", new Login());
		pm.register("/joinserver", new JoinServer());
		pm.register("/checkserver", new CheckServer());
		
		pm.register("/v2/joinserver", new JoinServer2());
		pm.register("/v2/checkserver", new CheckServer2());
	}
	
	@Override
	public Response serve(IHTTPSession session)
	{
		return request(session);
	}
	
	@SuppressWarnings("deprecation")
	private Response request(IHTTPSession session)
	{
		Method method = session.getMethod();
		String uri = session.getUri();
		
		if (method != Method.GET)
		{
			JSONObject obj = new JSONObject();
			obj.put("error", "Request method must be GET");
			return NanoHTTPD.newFixedLengthResponse(obj.toString());
		}
		
		Page page = pm.getPage(uri);
		if (page != null)
			return page.exec(session.getParms());
		return NanoHTTPD.newFixedLengthResponse("bad request");
	}
	
	public static SessionManager getSessionManager()
	{
		return sm;
	}
	
	public static SessionManager2 getSessionManager2()
	{
	    return smv2;
	}
}
