package dev.hv.db.model;

public interface IDUser {

		String getFirstName();

	   Long getID();

	   String getLastName();

	   String getPassword();

	   String getToken();

	   void setFirstName(String firstName);

	   void setID(Long ID);

	   void setLastName(String lastName);

	   void setPassword(String password);

	   void setToken(String token);
}
