package me.moderator_man.srv.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Config
{
	public static Config instance = new Config();
	
	public String hostname;
	public int port;
	public String username;
	public String password;
	public String database;
	public String tracker_key;
	
	private Properties properties;
	
	public Config()
	{
		try
		{
			File cfg = new File("osmauth.cfg");
			if (!cfg.exists())
				cfg.createNewFile();
			
			properties = new Properties();
			properties.load(new FileInputStream("osmauth.cfg"));
			
			hostname = properties.getProperty("hostname", "localhost");
			port = Integer.parseInt(properties.getProperty("port", "8080"));
			username = properties.getProperty("username", "gronk");
			password = properties.getProperty("password", "boink");
			database = properties.getProperty("database", "yeet");
			tracker_key = properties.getProperty("tracker_key", "yote");
		} catch (NumberFormatException ex) {
			System.out.println("port must be set to an integer in the config!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
