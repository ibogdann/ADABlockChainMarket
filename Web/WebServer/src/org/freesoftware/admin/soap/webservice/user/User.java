package org.freesoftware.admin.soap.webservice.user;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 3297488473647473961L;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String username;
	private String password;
	
	public User(String username, String password)
	{
		this.username=username;
		this.password=password;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public boolean passwordMatch(String pass)
	{
		return password.equals(pass);
	}
	
}
