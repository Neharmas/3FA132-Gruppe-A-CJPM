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
				+ "firstname TEXT NOT NULL);");
		//create table Nutzer
		handle.execute("CREATE TABLE IF NOT EXISTS User ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "lastname TEXT NOT NULL,"
				+ "firstname TEXT NOT NULL,"
				+ "token TEXT NOT NULL,"
				+ "password TEXT NOT NULL);");
		//create table Lesen
		handle.execute("CREATE TABLE IF NOT EXISTS Reading ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "comment TEXT NOT NULL,"
				+ "customer INTEGER NOT NULL,"
				+ "dateofreading TEXT NOT NULL,"
				+ "kindofmeter TEXT NOT NULL,"
				+ "meterid TEXT NOT NULL,"
				+ "metercount INTEGER NOT NULL,"
				+ "substitute BOOLEAN NOT NULL,"
				+ "FOREIGN KEY (customer) REFERENCES Customer (id)"
				+ ");");
		
		handle.close();
	}
	
	public void insertTestData() {
		final Handle handle = jdbi.open();
	
		handle.execute("INSERT INTO CUSTOMER (lastname, firstname) VALUES (\"Hasan\", \"Mouawia\"), (\"Maile\", \"Paul\"), (\"Mandl\", \"Julian\"), (\"Ajomale\", \"Christopher\");");
		
		handle.execute("INSERT INTO USER (lastname, firstname, Token, Password) "
				+ "VALUES ('Fittler', 'Arnold', '12345', '123456'), ('Franziska', 'Anne', '88420', '66699'), ('Gandhi', 'Mahatma', '112', '42'), ('Moses', 'Hannes', '777', '100');");
		
		handle.execute("INSERT INTO READING (comment, customer, kindofmeter, metercount, meterid, substitute, dateofreading)"
				+ "VALUES ('test1', 1, 'pla', '22ss', 222, false, 12345),"
				+ "('test2', 2, 'dlp', '1s1', 234, true, 8888);");
		
		handle.close();
		
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
		handle.close();
	}

}
