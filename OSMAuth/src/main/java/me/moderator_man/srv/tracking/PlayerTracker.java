package me.moderator_man.srv.tracking;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.moderator_man.srv.Server;
import me.moderator_man.srv.session.SessionManager;

public class PlayerTracker
{
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
	
	private HashMap<String, Timestamp> players = new HashMap<String, Timestamp>();
	private SessionManager sm;
	
	private long second = 1000;
	private long minute = second * 60;
	
	public PlayerTracker()
	{
		this.sm = Server.getSessionManager();
		
		scheduledExecutorService.scheduleWithFixedDelay(() -> checkTimestamps(), minute * 2, minute * 2, TimeUnit.MILLISECONDS);
	}
	
	public boolean submit(String username, String sessionId)
	{
		return submit(username, sessionId, (60 * 1000) * 2); // 2min timeout
	}
	
	public boolean submit(String username, String sessionId, long timeout_millis)
	{
		if (!sm.hasSession(username))
			return false;
		if (!sm.isSessionIdValid(sessionId))
			return false;
		
		if (players.containsKey(username))
		{
			players.replace(username, new Timestamp(System.currentTimeMillis()));
			log(String.format("Timeout was reset for player '%s'.", username));
			return true;
		}
		
		players.put(username, new Timestamp(System.currentTimeMillis()));
		log(String.format("Player '%s' was added to the tracker.", username));
		
		return true;
	}
	
	public void untrack(String username)
	{
		players.remove(username);
		log(String.format("Removing player '%s' from tracker (timed out)", username));
	}
	
	private void checkTimestamps()
	{
		log("Checking timestamps...");
		for (String username : players.keySet())
		{
			Timestamp ts = players.get(username);
			if ((System.currentTimeMillis() - ts.timestamp) >= (minute * 2))
				untrack(username);
		}
		log("Finished checking timestamps.");
	}
	
	public HashMap<String, Timestamp> getTrackedPlayers()
	{
		return players;
	}
	
	private void log(String msg)
	{
		System.out.println("[TRACKER] " + msg);
	}
}
