package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Logindata;
import dhbw.sa.kassensystem_rest.database.entity.Waiter;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBService_Waiter
{
	static final String selectAllAttributs = "SELECT waiterID, lastname, prename, employed " +
			"FROM " + DatabaseProperties.getDatabase() + ".waiters ";

	static ArrayList<Waiter> getAllWaiters(Connection connection)
	{
		ArrayList<Waiter> waiters = new ArrayList<>();

		try{
			String query = selectAllAttributs;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				int waiterID = rs.getInt("waiterID");
				String lastname = rs.getString("lastname");
				String prename = rs.getString("prename");
				boolean employed = rs.getBoolean("employed");

				waiters.add(new Waiter(waiterID, lastname, prename, employed));
			}
			return waiters;
		} catch(SQLException e)
		{
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static Waiter getWaiterByID(Connection connection, int waiterID)
	{
		try {
			String query = selectAllAttributs +
					"WHERE waiterID = " + waiterID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				waiterID = rs.getInt("waiterID");
				String lastname = rs.getString("lastname");
				String prename = rs.getString("prename");
				boolean employed = rs.getBoolean("employed");

				return new Waiter(waiterID, lastname, prename, employed);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return null;
	}

	static void addWaiter(Connection connection, Waiter waiter)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() +
					".waiters(waiterID, lastname, prename, employed) " +
					"VALUES(DEFAULT, ?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, waiter.getLastname());
			pst.setString(2, waiter.getPrename());
			pst.setBoolean(3, waiter.isEmployed());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}

	}

	static void updateWaiter(Connection connection, int waiterID,  Waiter waiter)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".waiters " +
					"SET lastname = ?, prename = ?, employed = ? " +
					"WHERE waiterID = " + waiterID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, waiter.getLastname());
			pst.setString(2, waiter.getPrename());
			pst.setBoolean(3, waiter.isEmployed());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsWaiterWithID(Connection connection, int waiterID)
	{
		try {
			String query = "SELECT waiterID from " + DatabaseProperties.getDatabase() + ".waiters " +
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
}
