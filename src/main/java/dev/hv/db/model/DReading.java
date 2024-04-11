package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class DReading implements IDReading{

	@JsonProperty("comment")
	@ColumnName("comment")
	private String comment;

	@ColumnName("customer")
	@JsonProperty("customer")
	@Nested

	private DCustomer customer;

	@JsonProperty("id")
	@ColumnName("id")
	private Long ID;

	@JsonProperty("kindofmeter")
	@ColumnName("kindofmeter")
	private String kindOfMeter;

	@JsonProperty("metercount")
	@ColumnName("metercount")
	private Double meterCount;
	
	@JsonProperty("meterid")
	@ColumnName("meterid")
	private String meterID;

	@JsonProperty("substitute")
	@ColumnName("substitute")
	private Boolean substitute;
	
	@JsonProperty("dateofreading")
	@ColumnName("dateofreading")
	private Long dateOfReading;
	
	@ConstructorProperties({ "id", "comment", "customer", "kindofmeter", "metercount", "meterid", "substitute", "dateofreading" })
	public DReading (long ID, String comment, DCustomer customer, String kindOfMeter, double meterCount, String meterID, boolean substitute, long dateOfReading) {
		this.ID = ID;
		this.comment = comment;
		this.customer = customer;
		this.kindOfMeter = kindOfMeter;
		this.meterCount = meterCount;
		this.meterID = meterID;
		this.substitute = substitute;
		this.dateOfReading = dateOfReading;
	}

	@Override
	public String toString() {
		return "DReading [id=" + ID + ", comment=" + comment + ", customer=" + customer + ", kindofmeter=" + meterCount +
				", meterid=" + meterID + ", substitute=" + substitute + ", dateodreading=" + dateOfReading +"]";
	}
	
	public DReading () {
		
	}


	public DReading(String comment, DCustomer customer, String kindOfMeter, double meterCount, String meterID, boolean substitute, long dateOfReading) {
		this.comment = comment;
		this.customer = customer;
		this.kindOfMeter = kindOfMeter;
		this.meterCount = meterCount;
		this.meterID = meterID;
		this.substitute = substitute;
		this.dateOfReading = dateOfReading;
	}
	
	@Override
	public String printDateOfReading() {
		return String.valueOf(dateOfReading); //TODO this implementation feels wrong
	}
}