package me.moderator_man.srv.session;

import me.moderator_man.srv.Server;

public class ThreadInvalidateSession extends Thread
{
	private String username;
	private boolean justServerHash;
	private int seconds_timeout;
	
	public ThreadInvalidateSession(String username, boolean justServerHash, int seconds_timeout)
	{
		this.username = username;
		this.justServerHash = justServerHash;
		this.seconds_timeout = seconds_timeout;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep(seconds_timeout * 1000);
			Server.getSessionManager().invalidateSession(username, justServerHash);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
