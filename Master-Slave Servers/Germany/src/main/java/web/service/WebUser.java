package web.service;

public class WebUser {
	
	private String username;
	private String password;
	
	public WebUser(String username, String password)
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
