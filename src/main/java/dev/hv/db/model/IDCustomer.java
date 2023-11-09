package dev.hv.db.model;

public interface IDCustomer {

   String getVorname();

   long getId();

   String getName();

   void setVorname(String firstName);

   void setId(long id);

   void setName(String lastName);
}
