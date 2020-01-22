package me.moderator_man.srv.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database
{
	private JDBCManager man;
	
	public Database(String database, String username, String password, String hostname)
	{
		man = new JDBCManager(database, username, password, hostname);
	}
	
	public boolean execute(String statement) throws SQLException, ClassNotFoundException
	{
		if (!man.connected())
			connect();
		
		Statement stmt = man.connection().createStatement();
		return stmt.execute(statement);
	}
	
	public Connection grabConnection()
	{
		return man.connection();
	}
	
	@Deprecated
	public ResultSet query(String statement, Object...parameters) throws SQLException, ClassNotFoundException
	{
		try
		{
			if (!man.connected())
				man.connect();
			
			PreparedStatement stmt = man.connection().prepareStatement(statement);
			
			return stmt.executeQuery(statement);
		} catch (Exception ex) {
			man.close();
			man.connect();
			Statement stmt = man.connection().createStatement();
			return stmt.executeQuery(statement);
		}
	}
	
	public void connect() throws SQLException, ClassNotFoundException
	{
		if (man.connected())
			return;
		
		man.connect();
	}
	
	public void close()
	{
		try
		{
			man.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
