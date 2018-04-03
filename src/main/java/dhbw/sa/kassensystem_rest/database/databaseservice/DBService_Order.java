package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService.convertJodaDateTimeToSqlTimestamp;

/**
 * Klasse mit Methoden zum Einsehen und Bearbeiten der Order-Tabelle.
 *
 * @auther Marvin Mai
 */
public class DBService_Order
{
	static ArrayList<Order> getAllOrders(Connection connection)
	{
		ArrayList<Order> orders = new ArrayList<>();

		try {
			String query = "SELECT orderID, date, tableID, waiterID " +
					"FROM " + DatabaseProperties.getDatabase() + ".orders";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				int orderID = rs.getInt("orderID");
				int tableID = rs.getInt("tableID");
				int waiterID = rs.getInt("waiterID");
				double price = DBService_Order.getPrice(connection, orderID);
				price = DatabaseService.round(price);
				DateTime dateTime = DatabaseService.convertSqlTimestampToJodaDateTime(rs.getTimestamp("date"));
				boolean paid = isOrderPaid(connection, orderID);

				orders.add(new Order(orderID, tableID, price, dateTime, paid, waiterID));
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static Order getOrderByID(Connection connection, int orderID)
	{
		try {
			String query = "SELECT orderID, date, tableID, waiterID " +
					"FROM " + DatabaseProperties.getDatabase() + ".orders " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				orderID = rs.getInt("orderID");
				int tableID = rs.getInt("tableID");
				int waiterID = rs.getInt("waiterID");
				double price = DBService_Order.getPrice(connection, orderID);
				price = DatabaseService.round(price);
				DateTime dateTime = DatabaseService.convertSqlTimestampToJodaDateTime(rs.getTimestamp("date"));
				boolean paid = isOrderPaid(connection, orderID);

				return new Order(orderID, tableID, price, dateTime, paid, waiterID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return null;
	}

	static int addOrder(Connection connection, Order order)
	{
		// Ermitteln ob für den Tisch der Order bereits eine order in der DB existiert
		int orderID = getOrderWithTableID(connection, order.getTable());

		if (orderID == 0)
		{
			try {
				String query =  "INSERT INTO " + DatabaseProperties.getDatabase() +
						".orders(orderID, date, tableID, waiterID)" +
						"VALUES(DEFAULT, ?, ?, ?)";
				PreparedStatement pst = connection.prepareStatement(query);

				pst.setObject(1, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
				pst.setInt(2, order.getTable());
				pst.setInt(3, order.getWaiterID());
				pst.executeUpdate();

				//Ermitteln der nun belegten orderID

				query = "SELECT * FROM " + DatabaseProperties.getDatabase() +
						".orders ORDER BY orderID DESC LIMIT 1";
				pst = connection.prepareStatement(query);
				ResultSet rs = pst.executeQuery();

				while(rs.next()) {
					return rs.getInt("orderID");
				}

			} catch(SQLException e) {
				e.printStackTrace();
				DatabaseService_Interface.connect();
				throw new MySQLServerConnectionException();
			}
		}
		else
			return orderID;

		return 0;
	}

	static void updateOrder(Connection connection, Order order, int orderID)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".orders " +
					"SET date = ?, tableID = ?, waiterID = ? " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setObject(1, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
			pst.setInt(2, order.getTable());
			pst.setInt(3, order.getWaiterID());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void deleteOrder(Connection connection, int orderID)
	{
		/*
          Loeschen einer Order loescht diese unwiederruflich aus der Datenbank.
         */
		try {
			String query =  "DELETE FROM " + DatabaseProperties.getDatabase() + ".orders " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}

		DBService_OrderedItem.deleteOrderedItemByOrderId(connection, orderID);
	}

	static boolean isOrderPaid(Connection connection, int orderID)
	{
		try {
			String query = "SELECT itemPaid from " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next())
			{
				if(!rs.getBoolean("itemPaid"))
					return false;
			}
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static float getPrice(Connection connection, int orderID)
	{
		try {
			// Ermitteln aller itemIDs die zu einer orderID gehören
			String query = "SELECT itemID " +
					"FROM " + DatabaseProperties.getDatabase() + ".orderedItems " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			float price = 0;
			int itemID;

			while(rs.next())
			{
				itemID = rs.getInt("itemID");

				price += DBService_Item.getRetailprice(connection, itemID);
			}
			return price;
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	public static boolean existsOrderWithID(Connection connection, int orderID)
	{
		try {
			String query = "SELECT orderID from " + DatabaseProperties.getDatabase() + ".orders " +
					"WHERE orderID = " + orderID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("orderID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}

	public static int getOrderWithTableID(Connection connection, int tableID)
	{
		try {
			String query = "SELECT orderID from " + DatabaseProperties.getDatabase() + ".orders " +
					"WHERE tableID = " + tableID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next())
			{
				int orderID = rs.getInt("orderID");

				if(!DBService_Order.isOrderPaid(connection, orderID) && orderID != 0)
					return rs.getInt("orderID");
			}

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return 0;
	}
}