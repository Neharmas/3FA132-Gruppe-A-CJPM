package dev.hv.rest.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface IRReading {

   String getComment();

   IRCustomer getCustomer();

   Long getDateofreading();

   Integer getId();

   String getKindofmeter();

   Double getMetercount();

   String getMeterid();

   Boolean getSubstitute();

   String printDateofreading();

   void setComment(String comment);

   void setCustomer(IRCustomer customer);

   void setDateofreading(Long dateOfReading);

   void setId(Integer id);

   void setKindofmeter(String kindOfMeter);

   void setMetercount(Double meterCount);

   void setMeterid(String meterId);

   void setSubstitute(Boolean substitute);

}
