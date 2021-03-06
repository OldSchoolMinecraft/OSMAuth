package me.moderator_man.srv.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager
{
	private Map<String, Session> sessions;
	
	public SessionManager()
	{
		sessions = new HashMap<String, Session>();
	}
	
	private void register(Session session)
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
			sessions.get(username).serverHash = "";
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
			Session session = getSession(username);
			boolean flag = !sessionId.trim().equalsIgnoreCase(session.sessionId.trim());
			if (flag)
			{
				return false;
			}
				
			session.serverHash = serverHash;
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
			Session session = getSession(username);
			if (session == null)
			{
				return false;
			} else if (session.serverHash == null) {
				return false;
			}
			boolean flag = session.serverHash.equals(serverHash);
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
	
	public Session getSession(String username)
	{
		return sessions.get(username);
	}
	
	public Session createSession(String username)
	{
		if (hasSession(username))
			sessions.remove(username);
		Session session = new Session(username);
		register(session);
		log(String.format("Session was created for player '%s'.", username));
		return session;
	}
	
	private void log(String msg)
	{
		System.out.println("[LEGACY SESSION] " + msg);
	}
}
