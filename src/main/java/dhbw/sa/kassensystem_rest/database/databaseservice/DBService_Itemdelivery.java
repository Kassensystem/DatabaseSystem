package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Itemdelivery;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBService_Itemdelivery
{
	static ArrayList<Itemdelivery> getAllItemdeliveries(Connection connection)
	{
		ArrayList<Itemdelivery> itemdeliveries = new ArrayList<>();

		try {
			String query = "SELECT itemdeliveryID, itemID, quantity " +
					"FROM " + DatabaseProperties.getDatabase() + ".itemdeliveries";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each Itemdelivery from DB
				int itemdeliveryID = rs.getInt("itemdeliveryID");
				int itemID = rs.getInt("itemID");
				int quantity = rs.getInt("quantity");
				itemdeliveries.add(new Itemdelivery(itemdeliveryID, itemID, quantity));
			}
			return itemdeliveries;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static Itemdelivery getItemdeliveryByID(Connection connection, int itemdeliveryID)
	{
		Itemdelivery itemdelivery = null;
		try {
			String query = "SELECT itemdeliveryID, itemID, quantity " +
					"FROM " + DatabaseProperties.getDatabase() + ".itemdeliveries " +
					"WHERE itemdeliveryID = " + itemdeliveryID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each table from DB
				itemdeliveryID = rs.getInt("itemdeliveryID");
				int itemID = rs.getInt("itemID");
				int quantity = rs.getInt("quantit");

				itemdelivery = new Itemdelivery(itemdeliveryID, itemID, quantity);
			}
			return itemdelivery;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void addItemdelivery(Connection connection, Itemdelivery itemdelivery)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() + ".itemdeliveries(itemdeliveryID, itemID, quantity) " +
					"VALUES(DEFAULT, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, itemdelivery.getItemID());
			pst.setInt(2, itemdelivery.getQuantity());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void deleteItemdelivery(Connection connection, int itemdeliveryID)
	{
		try {
			String query =  "DELETE FROM " + DatabaseProperties.getDatabase() + ".itemdeliveries " +
					"WHERE itemdeliveryID = " + itemdeliveryID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void updateItemdelivery(Connection connection, Itemdelivery itemdelivery, int itemdeliveryID)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".itemdeliveries " +
					"SET itemID = ?, quantity = ? " +
					"WHERE itemdeliveryID = " + itemdeliveryID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, itemdelivery.getItemID());
			pst.setInt(2, itemdelivery.getQuantity());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsItemdeliveryWithID(Connection connection, int itemdeliveryID)
	{
		try {
			String query = "SELECT itemdeliveryID from " + DatabaseProperties.getDatabase() + ".itemdeliveries " +
					"WHERE itemdeliveryID = " + itemdeliveryID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("itemdeliveryID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}
}
