package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DCustomer implements IDCustomer, Comparable<DCustomer>{

	@JsonProperty("id")
	@ColumnName("id")
	private Long id;

	@JsonProperty("firstname")
	@ColumnName("firstname")
	private String firstname;
	
	@JsonProperty("lastname")
	@ColumnName("lastname")
	private String lastname;

	//@ConstructorProperties({ "id", "lastname", "firstname" })
	public DCustomer(long id, String lastname, String firstName) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstName;
	}

	public DCustomer() {

	}
	@ConstructorProperties({ "lastname", "firstname" })
	public DCustomer(final String lastname, final String firstName) {
		this.lastname = lastname;
		this.firstname = firstName;
	}

	@Override
	public String toString() {
		return "DCustomer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + "]";
	}
	@Override
	public int compareTo(DCustomer o) {
		return o.id.compareTo(this.id);
	}
	
	public boolean isEqualTo(DCustomer o) {
		return this.toString().equals(o.toString());
	}
}
