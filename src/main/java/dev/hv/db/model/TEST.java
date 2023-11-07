package dev.hv.db.model;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import dev.hv.db.init.DBConnect;

public class TEST {

	public static void main(String[] args) {
		Jdbi jdbi = DBConnect.getConnection().getJdbi();
		
		DBConnect.getConnection().createAllTables();
		
		final Handle handle = jdbi.open();
		
		final List<Map<String, Object>> results = handle
		.createQuery("SELECT * from KUNDEN")
		.mapToMap()
		.list();
		
		System.out.println("######################");
		results.stream().forEach(e -> System.out.println(e));
		
		handle.close();
		
		//DBConnect.getConnection().removeAllTables();
		
	}

}
