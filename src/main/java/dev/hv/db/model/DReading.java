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
	private Long id;

	@JsonProperty("kindofmeter")
	@ColumnName("kindofmeter")
	private String kindofmeter;

	@JsonProperty("metercount")
	@ColumnName("metercount")
	private Double metercount;
	
	@JsonProperty("meterid")
	@ColumnName("meterid")
	private String meterid;

	@JsonProperty("substitute")
	@ColumnName("substitute")
	private Boolean substitute;
	
	@JsonProperty("dateofreading")
	@ColumnName("dateofreading")
	private Long dateofreading;
	
	@ConstructorProperties({ "id", "comment", "customer", "kindofmeter", "metercount", "meterid", "substitute", "dateofreading" })
	public DReading (long id, String comment, DCustomer customer, String kindofmeter, double metercount, String meterid, boolean substitute, long dateofreading) {
		this.id = id;
		this.comment = comment;
		this.customer = customer;
		this.kindofmeter = kindofmeter;
		this.metercount = metercount;
		this.meterid = meterid;
		this.substitute = substitute;
		this.dateofreading = dateofreading;
	}

	@Override
	public String toString() {
		return "DReading [id=" + id + ", comment=" + comment + ", customer=" + customer + ", kindofmeter=" + metercount +
				", meterid=" + meterid + ", substitute=" + substitute + ", dateodreading=" + dateofreading +"]";
	}
	
	public DReading () {
		
	}

	public DReading(String comment, DCustomer customer, String kindofmeter, double metercount, String meterid, boolean substitute, long dateofreading) {
		this.comment = comment;
		this.customer = customer;
		this.kindofmeter = kindofmeter;
		this.metercount = metercount;
		this.meterid = meterid;
		this.substitute = substitute;
		this.dateofreading = dateofreading;
	}
	
	@Override
	public String printDateofreading() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean equals(DReading o) {
		return this.toString().equals(o.toString());
	}

}