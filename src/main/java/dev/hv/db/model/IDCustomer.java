package dev.hv.db.model;

public interface IDCustomer {

		String getFirstName();

		Long getID();

		String getLastName();

		void setFirstName(String firstName);

		void setID(Long ID);

		void setLastName(String lastName);
	   
}
