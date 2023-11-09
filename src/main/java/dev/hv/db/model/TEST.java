package dev.hv.db.model;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.Query;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.init.DBConnect;

public class TEST {

	public static void main(String[] args) {
		Jdbi jdbi = DBConnect.getConnection().getJdbi();
		jdbi.installPlugins();
		
		//DBConnect.getConnection().removeAllTables();
		//DBConnect.getConnection().createAllTables();
		//insertTestData();
		
		Handle handle = jdbi.open();
		final CustomerDAO dao = handle.attach(CustomerDAO.class);
		
		//DCustomer test = new DCustomer("5555", "hannes");
		
		//dao.createTable();
		
		//dao.insert(test);
		handle.registerRowMapper(ConstructorMapper.factory(DCustomer.class));
		DCustomer test2 = dao.getAll().get(2);
		System.out.println(test2.toString());
		//SELECT last_insert_rowid()
		//dao.delete(test2);
	
		runQuery("SELECT last_insert_rowid()");
		runQuery("SELECT * from KUNDEN");
		//DBConnect.getConnection().removeAllTables();
		
	}
	
	public static void runQuery(String q) {
		Handle handle = DBConnect.getConnection().getJdbi().open();
		List<Map<String, Object>> results = handle
			.createQuery(q)
			.mapToMap()
			.list();
			
			System.out.println("######################");
			results.stream().forEach(e -> System.out.println(e));
			
			handle.close();
	}
	
	public static void insertTestData() {
		DBConnect.getConnection().createAllTables();
		
		final Handle handle = DBConnect.getConnection().getJdbi().open();
	
		handle.execute("INSERT INTO KUNDEN (Name, Vorname) VALUES (\"Hasan\", \"Mouawia\"), (\"Maile\", \"Paul\"), (\"Mandl\", \"Julian\"), (\"Ajomale\", \"Christopher\");");
		
		
		handle.close();
		
	}

}
