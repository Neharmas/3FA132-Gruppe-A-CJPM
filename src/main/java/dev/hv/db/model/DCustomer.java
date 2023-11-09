package dev.hv.db.model;

import java.beans.ConstructorProperties;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class DCustomer implements IDCustomer, Comparable<DCustomer>{
	@Override
	public String toString() {
		return "DCustomer [firstName=" + firstName + ", id=" + id + ", lastName=" + lastName + "]";
	}

	@ColumnName("vorname")
	private String firstName;
	
	@ColumnName("id")
	private long id;
	
	@ColumnName("name")
	private String lastName;
	
	@Override
	public String getFirstname() {
		return this.firstName;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getLastname() {
		return lastName;
	}

	@Override
	public void setVorname(String firstName) {
		this.firstName = firstName;
		
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}

	@Override
	public void setName(String lastName) {
		this.lastName = lastName;
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
		this.lastName = name;
		this.firstName = firstName;
	}
	
	public DCustomer() {
		
	}
	
	public DCustomer(final String name, final String firstName) {
		this.lastName = name;
		this.firstName = firstName;
	}

}
