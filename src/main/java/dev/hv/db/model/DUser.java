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
}
