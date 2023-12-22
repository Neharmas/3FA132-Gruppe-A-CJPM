package dev.hv.rest.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface IRCustomer {

   String getFirstname();

   Integer getId();

   String getLastname();

   void setFirstname(String firstName);

   void setId(Integer id);

   void setLastname(String lastName);
}
