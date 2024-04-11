package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DUser implements IDUser{

	@JsonProperty("id")
	@ColumnName("id")
	private Long ID;

	@JsonProperty("firstname")
	@ColumnName("firstname")
	private String firstName;

	@JsonProperty("lastname")
	@ColumnName("lastname")
	private String lastName;

	@JsonProperty("token")
	@ColumnName("token")
	private String token;

	@JsonProperty("password")
	@ColumnName("password")
	private String password;
	
	@ConstructorProperties({ "id", "lastname", "firstname", "token", "password"  })
	public DUser (long ID, String lastName, String firstName, String token, String password) {
		this.ID = ID;
		this.lastName = lastName;
		this.firstName = firstName;
		this.token = token;
		this.password = password;
	}
	
	public DUser() {
		
	}
	
	public DUser(final String lastName, final String firstName, final String token, final String password) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.token = token;
		this.password = password;
	}

	@Override
	public String toString() {
		return "DUser [id=" + ID + ", firstname=" + firstName + ", lastname=" + lastName + ", token=" + token
				+ ", password=" + password + "]";
	}
}
