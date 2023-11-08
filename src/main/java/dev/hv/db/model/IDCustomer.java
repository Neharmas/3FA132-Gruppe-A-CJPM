package dev.hv.db.model;

public interface IDCustomer {

   String getFirstname();

   long getId();

   String getLastname();

   void setFirstname(String firstName);

   void setId(long id);

   void setLastname(String lastName);
}
