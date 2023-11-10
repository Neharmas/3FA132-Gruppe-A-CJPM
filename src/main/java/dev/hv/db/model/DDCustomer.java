package dev.hv.db.model;

import java.beans.ConstructorProperties;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class DCustomer implements IDCustomer, Comparable<DCustomer>{
	@Override
	public String toString() {
		return "DCustomer [firstName=" + vorname + ", id=" + id + ", lastName=" + name + "]";
	}

	@ColumnName("vorname")
	private String vorname;
	
	@ColumnName("id")
	private long id;
	
	@ColumnName("name")
	private String name;
	
	@Override
	public String getVorname() {
		return this.vorname;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setVorname(String firstName) {
		this.vorname = firstName;
		
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}

	@Override
	public void setName(String lastName) {
		this.name = lastName;
	}

	@Override
	public int compareTo(DCustomer o) {
		if (o.id == this.id) return 0;
		else if (o.id > this.id) return 1;
		else return -1; 
	}
	
	@ConstructorProperties({ "id", "name", "vorname" })
	public DCustomer(long id, String name, String firstName) {
		this.id = id;
		this.name = name;
		this.vorname = firstName;
	}
	
	public DCustomer() {
		
	}
	
	public DCustomer(final String name, final String firstName) {
		this.name = name;
		this.vorname = firstName;
	}

}
