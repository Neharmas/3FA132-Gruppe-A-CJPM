package dev.hv.db.model;

public interface IDReading { //swhy isnt this comparable??

	String getComment();
	
	IDCustomer getCustomer();
	
	Long getDateOfReading();
	
	Long getID();
	
	String getKindOfMeter();
	
	Double getMeterCount();
	
	String getMeterID();
	
	Boolean getSubstitute();
	
	String printDateOfReading();
	
	void setComment(String comment);
	
	void setCustomer(DCustomer customer);
	
	void setDateOfReading(Long dateOfReading);
	
	void setID(Long ID);
	
	void setKindOfMeter(String kindOfMeter);
	
	void setMeterCount(Double meterCount);
	
	void setMeterID(String meterID);
	
	void setSubstitute(Boolean substitute);

}
