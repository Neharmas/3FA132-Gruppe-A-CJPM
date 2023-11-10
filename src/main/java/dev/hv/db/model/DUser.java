package dev.hv.db.model;

import java.beans.ConstructorProperties;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class DUser implements IDUser{

	@ColumnName("id")
	private Long id;
	
	@ColumnName("firstname")
	private String firstname;
	
	@ColumnName("lastname")
	private String lastname;
	
	@ColumnName("token")
	private String token;
	
	@ColumnName("password")
	private String password;
	
	@ConstructorProperties({ "id", "lastname", "firstname", "password", "token" })
	public DUser (long id, String lastname, String firstname, String token, String password) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.token = token;
		this.password = password;
	}
	
	public DUser() {
		
	}
	
	public DUser(final String lastname, final String firstname, final String token, final String password) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.token = token;
		this.password = password;
	}

	@Override
	public String toString() {
		return "DUser [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", token=" + token
				+ ", password=" + password + "]";
	}
	
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getFirstname() {
		return firstname;
	}
	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	@Override
	public String getLastname() {
		return lastname;
	}
	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	@Override
	public String getToken() {
		return token;
	}
	@Override
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
}
