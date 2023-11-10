package dev.hv.db.init;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class DBConnect implements IDbConnect{
	private static DBConnect instance = null;
	
	private DBConnect() {
		
	}
	
	public static DBConnect getConnection() {
		if (instance == null) {
			instance = new DBConnect();
		}
		return instance;
	}
	
	private Jdbi jdbi = null;

	@Override
	public void createAllTables() {
		Jdbi jdbi = getJdbi();
		
		final Handle handle = jdbi.open();
		handle.execute("CREATE TABLE Kunden (\r\n"
				+ "	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n"
				+ "	Name TEXT NOT NULL,\r\n"
				+ "	Vorname TEXT NOT NULL\r\n"
				+ ");");
	}

	@Override
	public Jdbi getJdbi(String uri, String user, String pw) {
	        final String dburl = uri; //DBURL
			final String dbuser = user; //DBUSER
			final String dbpw = pw; //DBW
			
			return Jdbi.create(dburl, dbuser, dbpw);
	}

	@Override
	public Jdbi getJdbi() {
		if (this.jdbi == null) {
			 try {
	            final Properties prop = new Properties();
	            prop.load(new FileReader("hausverwaltung.properties"));
	            final String dburl = prop.getProperty("DBURL"); //DBURL
	            final String dbuser = prop.getProperty("DBUSER"); //DBUSER
	            String dbpw = prop.getProperty("DBPW"); //DBW
	            
	            if (dbpw == null) dbpw = "";

	            jdbi = getJdbi(dburl, dbuser, dbpw);
	         } catch (IOException e) {
	            throw new RuntimeException(e);
	         }
		}
		return this.jdbi;
	}

	@Override
	public void removeAllTables() {
		Jdbi jdbi = getJdbi();
		
		final Handle handle = jdbi.open();
		handle.execute("DROP TABLE Kunden");
	}

}
