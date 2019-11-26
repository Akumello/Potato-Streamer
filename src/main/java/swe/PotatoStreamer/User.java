package swe.PotatoStreamer;

public class User {
	private String username;
	private String id;
	private String pwd;
	
	public User(String username, String id, String pwd) {
		this.username = username;
		this.id = id;
		this.pwd = pwd;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setUsername(String user) {
		username = user;
	}
	public String getUsername() {
		return username;
	}
	
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	
	public String getPwd()
	{
		return pwd;
	}
}
