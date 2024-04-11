package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DCustomer implements IDCustomer{
	@JsonProperty("id")
	@ColumnName("id")
	private Long ID;

	@JsonProperty("firstname")
	@ColumnName("firstname")
	private String firstName;
	
	@JsonProperty("lastname")
	@ColumnName("lastname")
	private String lastName;

	@ConstructorProperties({ "id", "lastname", "firstname" })
	public DCustomer(long ID, String lastName, String firstName) {
		this.ID = ID;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public DCustomer() {

	}

	public DCustomer(final String lastName, final String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "DCustomer [id=" + ID + ", firstname=" + firstName + ", lastname=" + lastName + "]";
	}
}
