package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Item;
import dhbw.sa.kassensystem_rest.database.entity.Itemdelivery;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBService_Item
{
	static ArrayList<Item> getAllItems(Connection connection)
	{
		ArrayList<Item> items = new ArrayList<>();

		try {
			String query = "SELECT itemID, name, retailprice, available " +
					"FROM " + DatabaseProperties.getDatabase() + ".items";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each Item from DB
				int itemID = rs.getInt("itemID");
				String name = rs.getString("name");
				double retailprice = rs.getFloat("retailprice");
				retailprice = DatabaseService.round(retailprice);

				int quantity = getItemQuantity(connection, itemID);

				boolean available = rs.getBoolean("available");
				items.add(new Item(itemID, name, retailprice, quantity, available));
			}
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static Item getItemByID(Connection connection, int itemID)
	{
		try {
			String query = "SELECT itemID, name, retailprice, available " +
					"FROM " + DatabaseProperties.getDatabase() + ".items" +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				itemID = rs.getInt("itemID");
				String name = rs.getString("name");
				double retailprice = rs.getFloat("retailprice");
				retailprice = DatabaseService.round(retailprice);

				int quantity = getItemQuantity(connection, itemID);

				boolean available = rs.getBoolean("available");
				return new Item(itemID, name, retailprice, quantity, available);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return null;
	}

	static float getRetailprice(Connection connection, int itemID)
	{
		try
		{
			String query = "SELECT retailprice " +
					"FROM " + DatabaseProperties.getDatabase() + ".items " +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				return rs.getFloat("retailprice");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	static void addItem(Connection connection, Item item)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() + ".items(itemID, name, retailprice, available) " +
					"VALUES(DEFAULT, ?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, item.getName());
			pst.setDouble(2, item.getRetailprice());
			pst.setBoolean(3, item.isAvailable());
			pst.executeUpdate();

			//ID des neu erzeugten Items ermitteln, um anschließend hierfuer einen neuen Wareneingang anzulegen
			query = "SELECT * FROM " + DatabaseProperties.getDatabase() +
					".items ORDER BY itemID DESC LIMIT 1";
			pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			int itemID = 0;
			while(rs.next()) {
				itemID = rs.getInt("itemID");
			}

			DBService_Itemdelivery.addItemdelivery(connection, new Itemdelivery(itemID, item.getQuantity()));

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void updateItem(Connection connection, Item item, int itemID)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".items " +
					"SET name = ?, retailprice = ?, available = ? " +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, item.getName());
			pst.setDouble(2, item.getRetailprice());
			pst.setBoolean(3, item.isAvailable());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsItemWithID(Connection connection, int itemID)
	{
		try {
			String query = "SELECT itemID from " + DatabaseProperties.getDatabase() + ".items " +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("itemID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}

	/**
	 * Ermittelt die aktuelle Verfuegbarkeit eines Items anhand der Wareneingaenge und Warenausgaenge.
	 * @param itemID des items, dessen Haeufigkeit ermittelt werden soll.
	 * @return aktuelle Verfuegbarkeit des items.
	 */
	private static int getItemQuantity(Connection connection, int itemID)
	{
		//Ermitteln der Wareneingänge
		int itemDeliveries = 0;

		try {
			String query = "SELECT quantity AS \"Quantity of Itemdeliveries\"" +
					"FROM " + DatabaseProperties.getDatabase() + ".itemdeliveries " +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				itemDeliveries += rs.getInt("Quantity of Itemdeliveries");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}

		//Ermitteln der Warenausgänge
		int itemOrders = 0;

		try {
			String query = "SELECT COUNT(*) AS \"Quantity of Orders\"" +
					"FROM " + DatabaseProperties.getDatabase() + ".ordereditems " +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				itemOrders = rs.getInt("Quantity of Orders");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}

		return itemDeliveries - itemOrders;
	}
}
