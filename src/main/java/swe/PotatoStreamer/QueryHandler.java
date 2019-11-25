package swe.PotatoStreamer;

public class QueryHandler {

	private String idToCheck;
	private String pwdToCheck;
	
	public void setIdToCheck(String idToCheck)
	{
		this.idToCheck = idToCheck;
	}
	
	public void setPwdToCheck(String pwdToCheck)
	{
		this.pwdToCheck = pwdToCheck;
	}
	
	public String getIdToCheck()
	{
		return idToCheck;
	}
	
	public String getPwdToCheck() {
		return pwdToCheck;
	}
	
	// Do stuff with mySQL
	public User validateUser() 
	{
		User userConfirm = new User();
		return userConfirm;
	}
}
