package dhbw.sa.kassensystem_rest.database.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Diese Klasse ist ein Modell eines Eintrages in der Datenbanktabelle "Logindata".
 * Sie wird verwendet, um neue Logindata-Einträge zu erzeugen oder diese aus der Datenbank abzurufen.
 * Zum sicheren Speichern des Passwortes wird dieses als 256-bit-Hash verschlüsselt.
 * Beim Erzeugen eines Logindata darf dem Konstruktor nur das Passwort im Klartext übergeben werden.
 */
public class Logindata
{
	private int waiterID;
	private String loginname;
	private String passwordHash;

	/**
	 * Konstruktor für das Abrufen von bereits bestehenden Login-Daten aus der Datenbank.
	 * @param waiterID ID der Bedienung, die diesen Login-Datensatz verwenden soll.
	 * @param loginname Login-Name, der in der App zum Login verwendet werden soll.
	 * @param encryptedPasswordhash Das Passwort verschlüsselt als 256bit-Hash.
	 */
	public Logindata(int waiterID, String loginname, String encryptedPasswordhash)
	{
		this.waiterID = waiterID;
		this.loginname = loginname;
		this.passwordHash = encryptedPasswordhash;
	}

	/**
	 * Konstruktor für das Anlegen eines neuen Login-Datensatzes.
	 * @param loginname Der Name, der in der App zum Einloggen verwendet wird.
	 *
	 */
	public Logindata(int waiterID, String loginname)
	{
		this.waiterID = waiterID;
		this.loginname = loginname;
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

	/**
	 * Verschlüsselt das Password in einen 256-bit-Hash und setzt dieses als Passwordhash.
	 * @param password Das Passwort in Klartext. Beim Speichern des Passwortes in der Instanz wird
	 *                 dieses automatisch in einen 256bit-Hash verschlüsselt.
	 */
	public void setAndEncryptPassword(String password)
	{
		this.passwordHash = this.encryptPassword(password);
	}

	/**
	 * Verschlüsselt ein Password in eineen 256-bit-Hash.
	 * @param password Das Passwort in Klartext.
	 * @return Den verschlüsselten Hash-String.
	 */
	public static String encryptPassword(String password)
	{
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			return new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
