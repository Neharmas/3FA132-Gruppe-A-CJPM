package dev.hv.db.model;

public interface IDCustomer {

		String getFirstname();

		Long getId();

		String getLastname();

		void setFirstname(String firstName);

		void setId(Long id);

		void setLastname(String lastName);
	   
}
