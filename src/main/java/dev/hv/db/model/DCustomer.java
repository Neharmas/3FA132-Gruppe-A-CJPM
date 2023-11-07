package dev.hv.db.model;

public class DCustomer implements IDCustomer{
	private String firstName;
	private long id;
	private String lastName;
	
	@Override
	public String getFirstname() {
		return this.firstName;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getLastname() {
		return lastName;
	}

	@Override
	public void setFirstname(String firstName) {
		this.firstName = firstName;
		
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	@Override
	public void setLastname(String lastName) {
		this.lastName = lastName;
		
	}

}
