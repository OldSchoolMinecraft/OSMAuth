package me.moderator_man.srv.session.v2;

import java.util.UUID;

public class Session2
{
	public String username;
	public String sessionId;
	public String ip;
	
	public Session2(String username)
	{
		this.username = username;
		sessionId = newUniqueId();
	}
	
	public static String newUniqueId()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
