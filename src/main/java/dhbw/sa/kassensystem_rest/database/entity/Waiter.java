package dhbw.sa.kassensystem_rest.database.entity;

public class Waiter
{
	private int waiterID;
	private String lastname;
	private String prename;

	public Waiter(int waiterID, String lastname, String prename)
	{
		this.waiterID = waiterID;
		this.lastname = lastname;
		this.prename = lastname;
	}

	public Waiter(String lastname, String prename)
	{
		this.lastname = lastname;
		this.prename = prename;
		this.waiterID = 0;
	}

	public int getWaiterID()
	{
		return waiterID;
	}

	public String getLastname()
	{
		return lastname;
	}

	public String getPrename()
	{
		return prename;
	}
}
