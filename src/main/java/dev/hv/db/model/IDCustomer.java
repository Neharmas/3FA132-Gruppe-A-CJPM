package dev.hv.db.model;

public interface IDCustomer {

		String getFirstname();

		Long getId();

		String getLastname();

		void setFirstname(String firstname);

		void setId(Long id);

		void setLastname(String lastname);
	   
}
