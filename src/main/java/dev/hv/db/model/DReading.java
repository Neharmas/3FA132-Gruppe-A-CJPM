package dev.hv.db.model;

public class DReading implements IDReading{
	private String comment;
	private IDCustomer customer;
	private Long dateOfReading;
	private Long id;
	private String kindOfMeter;
	private String meterId;
	private Boolean substitute;
	private String date;
		
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public IDCustomer getCustomer() {
		return customer;
	}
	public void setCustomer(IDCustomer customer) {
		this.customer = customer;
	}
	
	@Override
	public Long getDateofreading() {
		return dateOfReading;
	}
	
	@Override
	public void setDateofreading(Long dateOfReading) {
		this.dateOfReading = dateOfReading;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String getKindofmeter() {
		return kindOfMeter;
	}

	@Override 
	public void setKindofmeter(String kindOfMeter) {
		this.kindOfMeter = kindOfMeter;
	}
	
	@Override
	public String getMeterid() {
		return meterId;
	}
	
	@Override
	public void setMeterid(String meterId) {
		this.meterId = meterId;
	}
	public Boolean getSubstitute() {
		return substitute;
	}
	public void setSubstitute(Boolean substitute) {
		this.substitute = substitute;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public Double getMetercount() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String printDateofreading() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setMetercount(Double meterCount) {
		// TODO Auto-generated method stub
		
	}


	
	

}
