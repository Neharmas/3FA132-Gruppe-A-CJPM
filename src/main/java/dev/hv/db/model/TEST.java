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
		Jdbi jdbi = new DBConnect().getJdbi();
		
		final Handle handle = jdbi.open();
		
		final List<Map<String, Object>> results = handle
		.createQuery("SELECT * FROM KUNDEN;")
		.mapToMap()
		.list();
		
		System.out.println("######################");
		results.stream().forEach(e -> System.out.println(e));
		
	}

}
