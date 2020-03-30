package me.moderator_man.srv;

import java.sql.SQLException;

import fi.iki.elonen.NanoHTTPD;
import me.moderator_man.srv.config.Config;
import me.moderator_man.srv.db.Database;

public class Main
{
	public static Database database;
	
	public static void main(String[] args)
	{
		try
		{
			database = new Database(Config.instance.database, Config.instance.username, Config.instance.password, Config.instance.hostname);
			
			try
			{
				database.connect();
				log("Connected to database successfully!");
			} catch (SQLException ex) {
				log("Database failed to connect!");
				System.exit(1);
				return;
			} catch (ClassNotFoundException ex) {
				log("Database driver not found!");
				System.exit(1);
				return;
			}
			
			Server server = new Server();
			server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
			
			log("Server successfully started!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void log(String msg)
	{
		System.out.println("[SYSTEM] " + msg);
	}
}