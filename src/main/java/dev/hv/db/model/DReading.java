package dev.hv.db.model;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class DReading implements IDReading{
	@JsonProperty("comment")
	@ColumnName("comment")
	private String comment;
	
	@ColumnName("customer")
	@JsonProperty("customer")
	@Nested
	private IDCustomer customer;

	@JsonProperty("id")
	@ColumnName("id")
	private Long id;

	@JsonProperty("kindofmeter")
	@ColumnName("kindofmeter")
	private String kindofmeter;

	@JsonProperty("metercount")
	@ColumnName("metercount")
	private double metercount;

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
	public DReading (long id, String comment, IDCustomer customer, String kindofmeter, double metercount, String meterid, boolean substitute, long dateofreading) {
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
		return "DCustomer [id=" + id + ", comment=" + comment + ", customer=" + customer + ", kindofmeter=" + metercount +
				", meterid=" + meterid + ", substitute=" + substitute + ", dateodreading=" + dateofreading +"]";
	}
	
	public DReading () {
		
	}
	@ConstructorProperties({ "comment", "customer", "kindofmeter", "metercount", "meterid", "substitute", "dateofreading" })
	public DReading(String comment, IDCustomer customer, String kindofmeter, double metercount, String meterid, boolean substitute, long dateofreading) {
		this.comment = comment;
		this.customer = customer;
		this.kindofmeter = kindofmeter;
		this.metercount = metercount;
		this.meterid = meterid;
		this.substitute = substitute;
		this.dateofreading = dateofreading;
	}
	
	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public IDCustomer getCustomer() {
		return customer;
	}

	@Override	
	public void setCustomer(IDCustomer customer) {
		this.customer = customer;
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
	public String getKindofmeter() {
		return kindofmeter;
	}

	@Override
	public void setKindofmeter(String kindofmeter) {
		this.kindofmeter = kindofmeter;
	}

	@Override
	public Double getMetercount() {
		return metercount;
	}

	@Override
	public void setMetercount(Double metercount) {
		this.metercount = metercount;
	}

	@Override
	public String getMeterid() {
		return meterid;
	}

	@Override
	public void setMeterid(String meterid) {
		this.meterid = meterid;
	}

	@Override
	public Boolean getSubstitute() {
		return substitute;
	}

	@Override
	public void setSubstitute(Boolean substitute) {
		this.substitute = substitute;
	}

	@Override
	public Long getDateofreading() {
		return dateofreading;
	}
	
	@Override
	public void setDateofreading(Long dateofreading) {
		this.dateofreading = dateofreading;
	}

	@Override
	public String printDateofreading() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEqualTo(DReading o) {
		return this.toString().equals(o.toString());
	}

}