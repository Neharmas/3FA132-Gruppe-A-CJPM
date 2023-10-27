package dev.hv.db.init;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.jdbi.v3.core.Jdbi;

public class DBConnect implements IDbConnect{
	
	private Jdbi jdbi = null;

	@Override
	public void createAllTables() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Jdbi getJdbi() {
		if (jdbi == null) {
	         try {
	            final Properties prop = new Properties();
	            prop.load(new FileReader("gm3.properties"));
	            final String dburl = prop.getProperty("DBURL");
	            final String dbuser = prop.getProperty("DBUSER");
	            final String dbpw = prop.getProperty("DBPW");

	            jdbi = Jdbi.create(dburl, dbuser, "");
	         } catch (IOException e) {
	            throw new RuntimeException(e);
	         }
	      }
	      return jdbi;
	}

	@Override
	public Jdbi getJdbi(String uri, String user, String pw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAllTables() {
		// TODO Auto-generated method stub
		
	}

}
