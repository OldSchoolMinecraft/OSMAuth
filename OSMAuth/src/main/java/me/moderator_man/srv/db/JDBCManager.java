package me.moderator_man.srv.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class JDBCManager
{
	private MysqlDataSource dataSource;
	
	public JDBCManager(String database, String username, String password, String hostname)
	{
		dataSource = new MysqlDataSource();
		
		dataSource.setDatabaseName(database);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setServerName(hostname);
		
		try
		{
			dataSource.getConnection();
		} catch (Exception ex) {
			System.out.println("Failed to connect to database!");
		}
	}
	
	public boolean connected()
	{
		return connection() != null;
	}
	
	public Connection connection()
	{
		try
		{
			return dataSource.getConnection();
		} catch (Exception ex) {
			System.out.println("Oops! There was a problem connecting to the database!");
			return null;
		}
	}
	
	public void connect() throws SQLException
	{
		if (connected())
			return;
	}
	
	public void close() throws SQLException
	{
		//TODO: nothing :/
	}
}
