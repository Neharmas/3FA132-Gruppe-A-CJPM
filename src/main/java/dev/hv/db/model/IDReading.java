package dev.hv.db.model;

public interface IDReading {

   String getKommentar();

   IDCustomer getKunde();

   Long getLesedatum();

   Long getId();

   String getMessart();

   Double getMetercount();

   String getMeterid();

   Boolean getSubstitute();

   String printDateofreading();

   void setKommentar(String Kommentar);

   void setCustomer(IDCustomer kunde);

   void setDateofreading(Long dateOfReading);

   void setId(Long id);

   void setKindofmeter(String kindOfMeter);

   void setMetercount(Double meterCount);

   void setMeterid(String meterId);

   void setSubstitute(Boolean substitute);

}
