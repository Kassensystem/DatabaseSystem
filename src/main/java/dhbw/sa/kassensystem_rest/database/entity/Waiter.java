package dhbw.sa.kassensystem_rest.database.entity;

/**
 * Model für einen Datensatz der Datenbanktabelle waiters.
 *
 * @author Marvin Mai
 */
public class Waiter
{
	private int waiterID;
	private String lastname;
	private String prename;
	private boolean employed;

	/**
	 * Konstruktor zum Abrufen von bereits bestehenden Waiter-Daten aus der Datenbank
	 * @param waiterID Id der existierenden Waiter-Datensatzes.
	 * @param lastname Nachname der Bedienung.
	 * @param prename Vorname der Bedienung.
	 * @param employed True, wenn die Bedienung aktuell angestellt ist.
	 */
	public Waiter(int waiterID, String lastname, String prename, boolean employed)
	{
		this.waiterID = waiterID;
		this.lastname = lastname;
		this.prename = prename;
		this.employed = employed;
	}

	/**
	 * Konstruktor zum Erstellen eines neuen Waiter-Datensatzes, der anschließend an die DB übertragen werden soll.
	 * @param lastname Nachname der Bedienung.
	 * @param prename Vorname der Bedienung.
	 */
	public Waiter(String lastname, String prename)
	{
		this.lastname = lastname;
		this.prename = prename;
		this.employed = true;
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

	public boolean isEmployed()
	{
		return employed;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public void setPrename(String prename)
	{
		this.prename = prename;
	}

	public void setEmployed(boolean employed)
	{
		this.employed = employed;
	}
}
