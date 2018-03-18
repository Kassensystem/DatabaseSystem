package dhbw.sa.kassensystem_rest.database.databaseservice;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import dhbw.sa.kassensystem_rest.database.entity.Logindata;
import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import dhbw.sa.kassensystem_rest.exceptions.NotAuthentificatedException;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBService_LoginData
{
	static ArrayList<Logindata> getAllLogindata(Connection connection)
	{
		ArrayList<Logindata> logindata = new ArrayList<>();

		try{
			String query = "SELECT waiterID, loginname, passwordhash " +
					"FROM " + DatabaseProperties.getDatabase() + ".logindata";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				int waiterID = rs.getInt("waiterID");
				String loginname = rs.getString("loginname");
				String passwordhash = rs.getString("passwordhash");

				logindata.add(new Logindata(waiterID, loginname, passwordhash));
			}
			return logindata;
		} catch(SQLException e)
		{
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

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

	static void addLogindata(Connection connection, Logindata logindata)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() +
					".logindata(waiterID, loginname, passwordhash) " +
					"VALUES(?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, logindata.getWaiterID());
			pst.setString(2, logindata.getLoginname());
			pst.setString(3, logindata.getPasswordHash());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void updateLogindata(Connection connection, Logindata logindata)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".logindata " +
					"SET waiterID = ?, loginname = ?, passwordhash = ? " +
					"WHERE waiterID = " + logindata.getWaiterID();
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, logindata.getWaiterID());
			pst.setString(2, logindata.getLoginname());
			pst.setString(3, logindata.getPasswordHash());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsLogindataWithWaiterID(Connection connection, int waiterID)
	{
		try {
			String query = "SELECT waiterID from " + DatabaseProperties.getDatabase() + ".logindata " +
					"WHERE waiterID = " + waiterID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("waiterID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}

	static boolean existsLogindataWithLoginname(Connection connection, String loginname)
	{
		try {
			String query = "SELECT waiterID from " + DatabaseProperties.getDatabase() + ".logindata " +
					"WHERE loginname = '" + loginname + "'";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next())
			{
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}

	static void deleteLogindata(Connection connection, int waiterID)
	{
		try {
			String query =  "DELETE FROM " + DatabaseProperties.getDatabase() + ".logindata " +
					"WHERE waiterID = " + waiterID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static int getWaiterIdByLogindata(Connection connection, String loginname, String passwordhash)
	{
		try
		{
			String query = "SELECT waiterID " +
					"FROM " + DatabaseProperties.getDatabase() + ".logindata " +
					"WHERE loginname = '" + loginname + "' " +
					"AND  passwordhash = '" + passwordhash + "'";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next())
			{
				return rs.getInt("waiterID");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		throw new NotAuthentificatedException("Mit den Logindaten scheint keine Bedienung zu existieren!");
	}
}
