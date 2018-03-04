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

	public static ArrayList<OrderedItem> getAllOrderedItems(Connection connection)
	{
		ArrayList<OrderedItem> orderedItems = new ArrayList<>();

		try {
			String query = "SELECT orderedItemID, orderID, itemID, itemPaid, itemProduced " +
					"FROM " + DatabaseProperties.getDatabase() + ".ordereditems";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each orderedItem from DB
				int orderedItemID = rs.getInt("orderedItemID");
				int orderID = rs.getInt("orderID");
				int itemID = rs.getInt("itemID");
				boolean itemPaid = rs.getBoolean("itemPaid");
				boolean itemProduced = rs.getBoolean("itemProduced");
				orderedItems.add(new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced));
			}
			return orderedItems;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	public static ArrayList<OrderedItem> getOrderedItemsByOrderId(Connection connection, int orderID)
	{
		ArrayList<OrderedItem> orderedItems = new ArrayList<>();

		try {
			String query = "SELECT orderedItemID, orderID, itemID, itemPaid, itemProduced " +
					"FROM " + DatabaseProperties.getDatabase() + ".ordereditems " +
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
				orderedItems.add(new OrderedItem(orderedItemID, orderID, itemID, itemPaid, itemProduced));
			}
			return orderedItems;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	public static void addOrderedItem(Connection connection, OrderedItem orderedItem)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() +
					".orderedItems(orderedItemId, orderID, itemID, itemPaid, itemProduced) " +
					"VALUES(DEFAULT, ?, ?, DEFAULT, DEFAULT)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setInt(1, orderedItem.getOrderID());
			pst.setInt(2, orderedItem.getItemID());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}
}
