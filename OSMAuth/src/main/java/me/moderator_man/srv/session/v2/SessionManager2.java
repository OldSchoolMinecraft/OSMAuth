package me.moderator_man.srv.session.v2;

import java.util.HashMap;
import java.util.Map;

public class SessionManager2
{
	private Map<String, Session2> sessions;
	
	public SessionManager2()
	{
		sessions = new HashMap<String, Session2>();
	}
	
	private void register(Session2 session)
	{
		if (!isSessionIdValid(session.sessionId))
			return;
		sessions.put(session.username, session);
	}
	
	public void invalidateSession(String username, boolean justServerHash)
	{
		if (!hasSession(username))
			return;
		if (!isSessionIdValid(sessions.get(username).sessionId))
			return;
		if (justServerHash)
			sessions.get(username).ip = "";
		else
			sessions.remove(username);
	}
	
	public boolean joinServer(String username, String sessionId, String serverHash)
	{
		try
		{
			if (!hasSession(username))
				return false;
			if (!isSessionIdValid(sessionId))
				return false;
			Session2 session = getSession(username);
			boolean flag = !sessionId.trim().equalsIgnoreCase(session.sessionId.trim());
			if (flag)
			{
				return false;
			}
				
			session.ip = serverHash;
			return true;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean checkServer(String username, String serverHash)
	{
		try
		{
			if (!hasSession(username))
				return false;
			if (!isSessionIdValid(sessions.get(username).sessionId))
				return false;
			Session2 session = getSession(username);
			if (session == null)
			{
				return false;
			} else if (session.ip == null) {
				return false;
			}
			boolean flag = session.ip.equals(serverHash);
			return flag;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean hasSession(String username)
	{
		boolean flag = sessions.get(username) != null;
		return flag;
	}
	
	public boolean isSessionIdValid(String sessionId)
	{
		boolean flag = sessionId.length() == 32;
		log(String.format("Checked session ID '%s', valid: %s", sessionId, flag));
		return flag;
	}
	
	public Session2 getSession(String username)
	{
		return sessions.get(username);
	}
	
	public Session2 createSession(String username)
	{
		if (hasSession(username))
			sessions.remove(username);
		Session2 session = new Session2(username);
		register(session);
		log(String.format("Session was created for player '%s'.", username));
		return session;
	}
	
	private void log(String msg)
	{
		System.out.println("[SESSION v2] " + msg);
	}
}
