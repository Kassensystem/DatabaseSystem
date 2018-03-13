import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Logindata_Test
{
	public static void main(String [] args)
	{
		DatabaseService databaseService = new DatabaseService();

		if (databaseService
			.authentificate("hansi","j���*\u0011�r\u001D\u0015B�"))
		{
			System.out.println("Login erfolgreich!");
		}
	}
}
