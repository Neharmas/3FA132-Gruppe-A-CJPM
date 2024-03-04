package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DUser implements IDUser{

	@JsonProperty("id")
	@ColumnName("id")
	private Long id;

	@JsonProperty("firstname")
	@ColumnName("firstname")
	private String firstname;

	@JsonProperty("lastname")
	@ColumnName("lastname")
	private String lastname;

	@JsonProperty("token")
	@ColumnName("token")
	private String token;

	@JsonProperty("password")
	@ColumnName("password")
	private String password;
	
	@ConstructorProperties({ "id", "lastname", "firstname", "token", "password"  })
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
	public String getFirstname() {
		return this.firstname;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getLastname() {
		return this.lastname;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public void setFirstname(String firstName) {
		this.firstname = firstName;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setLastname(String lastName) {
		this.lastname = lastName;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
		
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}
}
