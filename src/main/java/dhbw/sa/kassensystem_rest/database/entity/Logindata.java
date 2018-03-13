package dhbw.sa.kassensystem_rest.database.entity;

public class Logindata
{
	private int waiterID;
	private String loginname;
	private String passwordHash;

	public Logindata(int waiterID, String loginname, String passwordHash)
	{
		this.waiterID = waiterID;
		this.loginname = loginname;
		this.passwordHash = passwordHash;
	}

	public int getWaiterID()
	{
		return waiterID;
	}

	public String getLoginname()
	{
		return loginname;
	}

	public String getPasswordHash()
	{
		return passwordHash;
	}
}
