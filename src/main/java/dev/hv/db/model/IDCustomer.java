package dev.hv.db.model;

public interface IDCustomer {

   String getFirstname();

   long getId();

   String getLastname();

   void setVorname(String firstName);

   void setId(long id);

   void setName(String lastName);
}
