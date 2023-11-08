package dev.hv.db.model;

import java.beans.ConstructorProperties;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class DCustomer implements IDCustomer, Comparable<DCustomer>{
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
	public void setFirstname(String firstName) {
		this.firstName = firstName;
		
	}

	@Override
	public void setId(long id) {
		this.id = id;
		
	}

	@Override
	public void setLastname(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public int compareTo(DCustomer o) {
		return (o.id == this.id) ? 1 : 0;
	}
	
	@ConstructorProperties({ "id", "name", "vorname" })
	public DCustomer(final long id, final String name, final String firstName) {
		this.id = id;
		this.lastName = name;
		this.firstName = firstName;
	}
	
	public DCustomer(final String name, final String firstName) {
		this.lastName = name;
		this.firstName = firstName;
	}

}
