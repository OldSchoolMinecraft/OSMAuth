package me.moderator_man.srv;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import me.moderator_man.srv.pages.CheckServer;
import me.moderator_man.srv.pages.JoinServer;
import me.moderator_man.srv.pages.Login;
import me.moderator_man.srv.pages.Page;
import me.moderator_man.srv.pages.PageManager;
import me.moderator_man.srv.session.SessionManager;

public class Server extends NanoHTTPD
{
	//private static final Logger log = Logger.getLogger(Server.class.getName());
	
	private PageManager pm;
	private static SessionManager sm;
	
	public Server()
	{
		super(8080);
		
		pm = new PageManager();
		sm = new SessionManager();
		
		pm.register("/login", new Login());
		pm.register("/joinserver", new JoinServer());
		pm.register("/checkserver", new CheckServer());
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
		//log.info(String.format("%s '%s'", method, uri));
		
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
}
