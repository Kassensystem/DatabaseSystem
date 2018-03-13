package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.exceptions.NotAuthentificatedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBService_LoginData
{
	static boolean authentificate(Connection connection, String loginname, String passwordHash)
			throws NotAuthentificatedException
	{
		try
		{
			String query = "SELECT passwordhash " +
					"FROM " + DatabaseProperties.getDatabase() + ".logindata " +
					"WHERE loginname = '" + loginname + "'";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next())
			{
				String dbPasswordHash = rs.getString("passwordhash");
				if(dbPasswordHash.equals(passwordHash))
					return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		throw new NotAuthentificatedException("Login fehlgeschlagen!");
	}
}
