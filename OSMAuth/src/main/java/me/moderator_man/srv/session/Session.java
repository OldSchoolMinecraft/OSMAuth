package me.moderator_man.srv.session;

import java.util.UUID;

public class Session
{
	public String username;
	public String sessionId;
	public String serverHash;
	
	public Session(String username)
	{
		this.username = username;
		sessionId = newUniqueId();
	}
	
	public static String newUniqueId()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
