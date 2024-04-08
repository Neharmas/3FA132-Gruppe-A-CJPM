package dev.hv.db.init;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;
import org.json.JSONObject;

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
		createCustomerTable();
		createUserTable();
		createReadingTable();
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
				InputStream inputStream = DBConnect.class.getClassLoader().getResourceAsStream("hausverwaltung.properties");
	            prop.load(inputStream);
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
		handle.execute("DROP TABLE IF EXISTS CUSTOMER");
		handle.execute("DROP TABLE IF EXISTS READING");
		handle.execute("DROP TABLE IF EXISTS USER");
		handle.close();
	}
	
	// Generic Methods for use in Console 
	// ToDo: Could be in need for more JUnit Tests
	private String getValueType(Object value) {
		try {
			return value.getClass().getSimpleName();
		} catch (Exception e) {
			return "TEXT";
		}
	}

	private String determineColumnType(Object value) {
		// Used for Imports
		switch(getValueType(value)) {
			case "Float": return "FLOAT";
			case "Integer": return "INT";
			case "Boolean": return "BOOL";
			default: return "TEXT";
		}
	}
	
	public void createCustomerTable() {
		final Handle handle = jdbi.open();
		
		handle.execute("CREATE TABLE IF NOT EXISTS Customer ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "lastname TEXT NOT NULL,"
				+ "firstname TEXT NOT NULL);");

		handle.close();
	}
	
	public void createUserTable() {
		final Handle handle = jdbi.open();
		
		handle.execute("CREATE TABLE IF NOT EXISTS User ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "lastname TEXT NOT NULL,"
				+ "firstname TEXT NOT NULL,"
				+ "token TEXT NOT NULL,"
				+ "password TEXT NOT NULL);");

		handle.close();
	}
	
	public void createReadingTable() {
		final Handle handle = jdbi.open();
		
		handle.execute("CREATE TABLE IF NOT EXISTS Reading ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "comment TEXT NOT NULL,"
				+ "customer INTEGER NOT NULL,"
				+ "dateofreading TEXT NOT NULL,"
				+ "kindofmeter TEXT NOT NULL,"
				+ "meterid TEXT NOT NULL,"
				+ "metercount INTEGER NOT NULL,"
				+ "substitute BOOLEAN NOT NULL,"
				//dependent on existing customer table
				+ "FOREIGN KEY (customer) REFERENCES Customer (id)" 
				+ ");");

		handle.close();
	}
	
	private String buildColumnsSpecification(String[] columnNames, String[] columnType, String[] constrains) {
		String columnSpecification = " (";
		for (int i = 0; i < columnNames.length; i++) {
			columnSpecification += " " + columnNames[i] + " " + columnType[i] + /** " NOT NULL "+*/ constrains[i] + " ,";
		}
		columnSpecification.replaceFirst(".$", ");");
		
		return columnSpecification;
	}
	
	public void createSpecifiedTable(String tablename, String[] columnNames, String[] columnType, String[] constrains) {
		String columnSpecification = buildColumnsSpecification(columnNames, columnType, constrains);

		final Handle handle = jdbi.open();
		handle.execute("CREATE TABLE IF NOT EXISTS " + tablename + columnSpecification );
		
		handle.close();
	}

	public List<Map<String, Object>> readTable(String tablename) {
		final Handle handle = jdbi.open();
		List<Map<String, Object>> res = handle.select("SELECT * FROM " + tablename + ";").mapToMap().list();
		handle.close();
		
		return res;
	}
	
	public String readTableAsMap(String tablename) {
		final Handle handle = jdbi.open();
		//Map<String, Object> res = handle.select("SELECT * FROM " + tablename + ";").toString();
		String res = handle.select("SELECT * FROM " + tablename + ";").toString();
		handle.close();
		
		return res;
	}
	/*
	public static void main(String[] args) {

		DBConnect db = new DBConnect();
		Jdbi jdbi = db.getJdbi();
		db.createAllTables();
		db.insertTestData();

		List<Map<String, Object>> customers = db.readTable("Customer");
		//List<Map<String, Object>> users = db.readTable("User");
		//List<Map<String, Object>> readings = db.readTable("Reading");
		//List<Map<String, Object>> fictional = db.readTable("fictional");
		//System.out.println(customers);
		System.out.println(customers);
		//String customerCutted = customers.toString().substring(1, customers.toString().length() - 1);
		
		String customerWithKey = "{Customer: " + customers;

		
		JSONObject json = new JSONObject(customerWithKey);
		
		System.out.println(json);
		/** [{id=1, lastname=ln1, firstname=fn1}, {id=3, lastname=Baggins, firstname=Frodo}, {id=4, lastname=Baggins, firstname=Frodo}, {id=5, lastname=Baggins, firstname=Frodo}, {id=6, lastname=Baggins, firstname=Frodo}, {id=7, lastname=Hasan, firstname=Mouawia}, {id=8, lastname=Maile, firstname=Paul}, {id=9, lastname=Mandl, firstname=Julian}, {id=10, lastname=Ajomale, firstname=Christopher}, {id=11, lastname=Hasan, firstname=Mouawia}, {id=12, lastname=Maile, firstname=Paul}, {id=13, lastname=Mandl, firstname=Julian}, {id=14, lastname=Ajomale, firstname=Christopher}, {id=15, lastname=Hasan, firstname=Mouawia}, {id=16, lastname=Maile, firstname=Paul}, {id=17, lastname=Mandl, firstname=Julian}, {id=18, lastname=Ajomale, firstname=Christopher}, {id=19, lastname=Hasan, firstname=Mouawia}, {id=20, lastname=Maile, firstname=Paul}, {id=21, lastname=Mandl, firstname=Julian}, {id=22, lastname=Ajomale, firstname=Christopher}, {id=23, lastname=Hasan, firstname=Mouawia}, {id=24, lastname=Maile, firstname=Paul}, {id=25, lastname=Mandl, firstname=Julian}, {id=26, lastname=Ajomale, firstname=Christopher}, {id=27, lastname=Hasan, firstname=Mouawia}, {id=28, lastname=Maile, firstname=Paul}, {id=29, lastname=Mandl, firstname=Julian}, {id=30, lastname=Ajomale, firstname=Christopher}, {id=31, lastname=Hasan, firstname=Mouawia}, {id=32, lastname=Maile, firstname=Paul}, {id=33, lastname=Mandl, firstname=Julian}, {id=34, lastname=Ajomale, firstname=Christopher}, {id=35, lastname=Hasan, firstname=Mouawia}, {id=36, lastname=Maile, firstname=Paul}, {id=37, lastname=Mandl, firstname=Julian}, {id=38, lastname=Ajomale, firstname=Christopher}, {id=39, lastname=Hasan, firstname=Mouawia}, {id=40, lastname=Maile, firstname=Paul}, {id=41, lastname=Mandl, firstname=Julian}, {id=42, lastname=Ajomale, firstname=Christopher}]
			[{id=1, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=2, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=3, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=4, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=5, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=6, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=7, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=8, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=9, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=10, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=11, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=12, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=13, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=14, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=15, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=16, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=17, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=18, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=19, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=20, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=21, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=22, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=23, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=24, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=25, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=26, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=27, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=28, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=29, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=30, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=31, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=32, lastname=Moses, firstname=Hannes, token=777, password=100}, {id=33, lastname=Fittler, firstname=Arnold, token=12345, password=123456}, {id=34, lastname=Franziska, firstname=Anne, token=88420, password=66699}, {id=35, lastname=Gandhi, firstname=Mahatma, token=112, password=42}, {id=36, lastname=Moses, firstname=Hannes, token=777, password=100}]
			[{id=1, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=2, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=3, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=4, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=5, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=6, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=7, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=8, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=9, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=10, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=11, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=12, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=13, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=14, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=15, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=16, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}, {id=17, comment=test1, customer=1, dateofreading=12345, kindofmeter=pla, meterid=222, metercount=22ss, substitute=0}, {id=18, comment=test2, customer=2, dateofreading=8888, kindofmeter=dlp, meterid=234, metercount=1s1, substitute=1}] **/
		//System.out.println(fictional);

		
	//}
	
}
