package dev.hv.db.model;

public interface IDReading {

   String getComment();

   IDCustomer getCustomer();

   Long getDateofreading();

   Long getId();

   String getKindofmeter();

   Double getMetercount();

   String getMeterid();

   Boolean getSubstitute();

   String printDateofreading();

   void setComment(String comment);

   void setCustomer(IDCustomer customer);

   void setDateofreading(Long dateOfReading);

   void setId(Long id);

   void setKindofmeter(String kindOfMeter);

   void setMetercount(Double meterCount);

   void setMeterid(String meterId);

   void setSubstitute(Boolean substitute);

}
