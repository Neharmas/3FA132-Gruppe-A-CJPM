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
		return String.valueOf(dateofreading); //TODO this implementation feels wrong
	}

	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public IDCustomer getCustomer() {
		return this.customer;
	}

	@Override
	public Long getDateofreading() {
		return this.dateofreading;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getKindofmeter() {
		return this.kindofmeter;
	}

	@Override
	public Double getMetercount() {
		return this.metercount;
	}

	@Override
	public String getMeterid() {
		return this.meterid;
	}

	@Override
	public Boolean getSubstitute() {
		return this.substitute;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void setCustomer(DCustomer customer) {
		this.customer = customer;
	}

	@Override
	public void setDateofreading(Long dateOfReading) {
		this.dateofreading = dateOfReading; //TODO: 3 ways to write one word...
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	@Override
	public void setKindofmeter(String kindOfMeter) {
		this.kindofmeter = kindOfMeter;
		
	}

	@Override
	public void setMetercount(Double meterCount) {
		this.metercount = meterCount;
	}

	@Override
	public void setMeterid(String meterId) {
		this.meterid = meterId;
	}

	@Override
	public void setSubstitute(Boolean substitute) {
		this.substitute = substitute;
	}

}