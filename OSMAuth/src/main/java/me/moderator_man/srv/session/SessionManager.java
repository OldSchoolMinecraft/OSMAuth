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
		//System.out.println(String.format("Invalidated session: username = %s, justServerHash = %b", username, justServerHash));
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
				//System.out.println(String.format("User failed to join server: sUsername = %s, sSessionId = %s, sServerHash = %s, username = %s, sessionId = %s, serverHash = %s", session.username, session.sessionId, session.serverHash, username, sessionId, serverHash));
				return false;
			}
				
			session.serverHash = serverHash;
			//System.out.println(String.format("User joined server: username = %s, sessionId = %s, serverHash = %s", username, sessionId, serverHash));
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
				//System.out.println(String.format("Server check received null session: username = %s", username));
				return false;
			} else if (session.serverHash == null) {
				//System.out.println(String.format("Server check received null serverHash in session: username = %s", username));
				return false;
			}
			boolean flag = session.serverHash.equals(serverHash);
			//System.out.println(String.format("Server checked session: username = %s, serverHash = %s", username, serverHash));
			return flag;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean hasSession(String username)
	{
		boolean flag = sessions.get(username) != null;
		//System.out.println(String.format("Checking if user has session: username = %s, exists = %s", username, flag));
		return flag;
	}
	
	public boolean isSessionIdValid(String sessionId)
	{
		boolean flag = sessionId.length() == 32;
		log(String.format("Checked session ID '%s', valid: %s", sessionId, flag));
		//System.out.println(String.format("Checked session: sessionId = %s, valid = %s", sessionId, flag));
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
		//System.out.println(String.format("Session was created: username = %s, sessionId = %s", username, session.sessionId));
		return session;
	}
	
	private void log(String msg)
	{
		System.out.println("[SESSION] " + msg);
	}
}
