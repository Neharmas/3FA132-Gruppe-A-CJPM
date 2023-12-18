package dev.hv.db.model;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.Query;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;

public class TEST {

	public static void main(String[] args) {
		Jdbi jdbi = DBConnect.getConnection().getJdbi();
		jdbi.installPlugins();
		
		//DBConnect.getConnection().removeAllTables();
		//DBConnect.getConnection().createAllTables();
		//insertTestData();
		
		//DBConnect.getConnection().removeAllTables();
		DBConnect.getConnection().createAllTables();
		System.out.println("CRATED.");
		Handle handle = jdbi.open();
		final CustomerDAO customer_dao = handle.attach(CustomerDAO.class);
		
		DCustomer test_customer = new DCustomer("Kakaro", "Hannes");
		
		
		customer_dao.insert(test_customer);
		handle.registerRowMapper(ConstructorMapper.factory(DCustomer.class));
		List<DCustomer> cList = customer_dao.getAll();
		for (DCustomer c: cList) {
			System.out.println(c.toString());
		}
		
		//SELECT last_insert_rowid()
		//dao.delete(test2);
		//DCustomer c = cList.get(0);
		//c.setName("Momo");
		//c.setVorname("Hallo");
		//dao.update(7l ,c);
	
		//runQuery("SELECT last_insert_rowid()");
		//runQuery("SELECT * from KUNDEN");
		//DBConnect.getConnection().removeAllTables();
		//DBConnect.getConnection().createAllTables();	
		//DBConnect.getConnection().insertTestData();
		
		/*List<DReading> cList = dao.getAll();
		
		for (DReading c: cList) {
			System.out.println(c.toString());
		}*/
		
		//DUser test = new DUser("Mandl", "Julian", "Bitch", "Cat");
		
		//dao.insert(test);
		//runQuery("SELECT * from reading");
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
	

}
