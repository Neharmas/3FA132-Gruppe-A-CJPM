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
		final Handle handle = jdbi.open();
		//create table KUNDEN
		handle.execute("CREATE TABLE IF NOT EXISTS Customer ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "lastname TEXT NOT NULL,"
				+ "firstName TEXT NOT NULL);");
		//create table Nutzer
		handle.execute("CREATE TABLE IF NOT EXISTS User ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "lastname TEXT NOT NULL,"
				+ "firstname TEXT NOT NULL,"
				+ "token TEXT NOT NULL,"
				+ "password TEXT NOT NULL);");
		//create table Lesen
		handle.execute("CREATE TABLE IF NOT EXISTS Lesen ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "kommentar TEXT NOT NULL,"
				+ "kunde INTEGER NOT NULL,"
				+ "Lesedatum TEXT NOT NULL,"
				+ "messart TEXT NOT NULL,"
				+ "messId TEXT NOT NULL,"
				+ "untertitle BOOLEAN NOT NULL,"
				+ "datum TEXT NOT NULL,"
				+ "Foreign key (kunde) references kunden (id)"
				+ ");");
		
		handle.close();
	}
	
	public void insertTestData() {
		final Handle handle = jdbi.open();
	
		handle.execute("INSERT INTO CUSTOMER (lastname, firstname) VALUES (\"Hasan\", \"Mouawia\"), (\"Maile\", \"Paul\"), (\"Mandl\", \"Julian\"), (\"Ajomale\", \"Christopher\");");
		
		handle.execute("INSERT INTO USER (lastname, firstname, Token, Password) "
				+ "VALUES ('Hitler', 'Adolf', '12345', '123456'), ('Frank', 'Anne', '88420', '66699'), ('Gandhi', 'Mahatma', '112', '42'), ('Moses', 'Hannes', '777', '100');");
		
		handle.close();
		
	}

	@Override
	public Jdbi getJdbi(String uri, String user, String pw) {
		if (this.jdbi == null) {
	        final String dburl = uri; //DBURL
			final String dbuser = user; //DBUSER
			final String dbpw = pw; //DBW
			
			jdbi = Jdbi.create(dburl, dbuser, dbpw);
	      }
	      return jdbi;
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

	            jdbi = Jdbi.create(dburl, dbuser, dbpw);
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
		handle.close();
	}

}
