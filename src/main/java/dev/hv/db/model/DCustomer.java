package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DCustomer implements IDCustomer{

	@JsonProperty("id")
	@ColumnName("id")
	private Long id;

	@JsonProperty("firstname")
	@ColumnName("firstname")
	private String firstname;
	
	@JsonProperty("lastname")
	@ColumnName("lastname")
	private String lastname;

	@ConstructorProperties({ "id", "lastname", "firstname" })
	public DCustomer(long id, String lastname, String firstName) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstName;
	}

	public DCustomer() {

	}

	public DCustomer(final String lastname, final String firstName) {
		this.lastname = lastname;
		this.firstname = firstName;
	}

	@Override
	public String toString() {
		return "DCustomer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + "]";
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
	public void setFirstname(String firstname) {
		this.firstname = firstname;
		
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
		
	}
}
