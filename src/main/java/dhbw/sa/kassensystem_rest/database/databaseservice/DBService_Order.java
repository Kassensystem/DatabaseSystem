package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBService_Order
{
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
}