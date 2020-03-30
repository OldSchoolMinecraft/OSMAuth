package me.moderator_man.srv.tracking;

import me.moderator_man.srv.Server;

public class ThreadUntrackPlayer extends Thread
{
	private String username;
	private long timeout_millis;
	
	public ThreadUntrackPlayer(String username, long timeout_millis)
	{
		this.username = username;
		this.timeout_millis = timeout_millis;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep(timeout_millis);
			Server.getPlayerTracker().untrack(username);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
