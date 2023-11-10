package dev.hv.db.model;

public class DUser implements IDUser{
	private String firstName, lastName;
	private Long id;
	private String password;
	private String token;
	
	@Override
	public String getFirstname() {
		return this.firstName;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getLastname() {
		return this.lastName;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getToken() {
		return this.token;
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

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
		
	}

}
