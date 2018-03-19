package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.OrderedItem;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBService_OrderedItem
{
	static final String selectAllAttributs =
			"SELECT orderedItemID, orderID, itemID, itemPaid, itemProduced, comment " +
			"FROM " + DatabaseProperties.getDatabase() + ".ordereditems ";

	static ArrayList<OrderedItem> getAllOrderedItems(Connection connection, boolean onlyUnproduced)
	{
		ArrayList<OrderedItem> orderedItems = new ArrayList<>();

		try {
			String query = selectAllAttributs;
			if(onlyUnproduced)
				query += "WHERE itemProduced = FALSE";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each orderedItem from DB
				int orderedItemID = rs.getInt("orderedItemID");
				int orderID = rs.getInt("orderID");
				int itemID = rs.getInt("itemID");
				boolean itemPaid = rs.getBoolean("itemPaid");
				boolean itemProduced = rs.getBoolean("itemProduced");
				String comment = rs.getString("comment");
				orderedItems.add(
						new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced, comment)
				);
			}
			return orderedItems;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static ArrayList<OrderedItem> getOrderedItemsByOrderId(Connection connection, int orderID)
	{
		ArrayList<OrderedItem> orderedItems = new ArrayList<>();

		try {
			String query = selectAllAttributs +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each orderedItem from DB
				int orderedItemID = rs.getInt("orderedItemID");
				orderID = rs.getInt("orderID");
				int itemID = rs.getInt("itemID");
				boolean itemPaid = rs.getBoolean("itemPaid");
				boolean itemProduced = rs.getBoolean("itemProduced");
				String comment = rs.getString("comment");
				orderedItems.add(
						new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced, comment)
				);
			}
			return orderedItems;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static ArrayList<OrderedItem> getOrderedItemsByItemId(Connection connection, int itemID)
	{
		ArrayList<OrderedItem> orderedItems = new ArrayList<>();

		try {
			String query = selectAllAttributs +
					"WHERE itemID = " + itemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each orderedItem from DB
				int orderedItemID = rs.getInt("orderedItemID");
				int orderID = rs.getInt("orderID");
				itemID = rs.getInt("itemID");
				boolean itemPaid = rs.getBoolean("itemPaid");
				boolean itemProduced = rs.getBoolean("itemProduced");
				String comment = rs.getString("comment");
				orderedItems.add(
						new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced, comment)
				);
			}
			return orderedItems;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static OrderedItem getOrderedItemById(Connection connection, int orderedItemID)
	{
		try {
			String query = selectAllAttributs +
					"WHERE orderedItemID = " + orderedItemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get orderedItem from DB
				orderedItemID = rs.getInt("orderedItemID");
				int orderID = rs.getInt("orderID");
				int itemID = rs.getInt("itemID");
				boolean itemPaid = rs.getBoolean("itemPaid");
				boolean itemProduced = rs.getBoolean("itemProduced");
				String comment = rs.getString("comment");
				return new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced, comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return null;
	}

	static void addOrderedItem(Connection connection, OrderedItem orderedItem)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() +
					".orderedItems(orderedItemId, orderID, itemID, itemPaid, itemProduced, comment) " +
					"VALUES(DEFAULT, ?, ?, DEFAULT, DEFAULT, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, orderedItem.getOrderID());
			pst.setInt(2, orderedItem.getItemID());
			pst.setString(3, orderedItem.getComment());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void updateOrderedItem(Connection connection, OrderedItem orderedItem, int orderedItemID)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"SET orderID = ?, itemID = ?, itemPaid = ?, itemProduced = ?, comment = ? " +
					"WHERE orderedItemID = " + orderedItemID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, orderedItem.getOrderID());
			pst.setInt(2, orderedItem.getItemID());
			pst.setBoolean(3, orderedItem.isItemPaid());
			pst.setBoolean(4, orderedItem.isItemProduced());
			pst.setString(5, orderedItem.getComment());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void deleteOrderedItem(Connection connection, int orderedItemID)
	{
		try {
			String query =  "DELETE FROM " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"WHERE orderedItemID = " + orderedItemID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void deleteOrderedItemByOrderId(Connection connection, int orderID)
	{
		try {
			String query =  "DELETE FROM " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsOrderedItemWithID(Connection connection, int orderedItemID)
	{
		try {
			String query = "SELECT orderedItemID from " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"WHERE orderedItemID = " + orderedItemID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("orderedItemID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}
}
