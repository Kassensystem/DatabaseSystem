package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.*;
import dhbw.sa.kassensystem_rest.database.printer.PrinterService;
import dhbw.sa.kassensystem_rest.exceptions.DataException;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import dhbw.sa.kassensystem_rest.exceptions.NotAuthentificatedException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.log;
import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logErr;
import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logInf;

/**
 * {@inheritDoc}
 *
 * Implementierung des DatabaseService_Interfaces.
 * Für die Dokumentation siehe Interface {@Link DatabaseService_Interface}
 *
 * @author Marvin Mai
 */

@Service
public class DatabaseService implements DatabaseService_Interface
{
    private Connection connection;

    public DatabaseService()
	{
        connection = DatabaseService_Interface.connect();
    }

    @Override
    public void disconnect()
	{
        if(connection != null) {
            try {
                connection.close();
                connection = null;
                logInf("Database disconnected!");
            } catch(SQLException e)  { e.printStackTrace();}
        }
    }

	private void checkConnection()
	{
		try {
			if(connection.isClosed())
				DatabaseService_Interface.connect();
		} catch (SQLException e) {}
	}

	//region Getting Table-Data from the database
	// Items
    @Override
    public ArrayList<Item> getAllItems() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Items from MySQL-Database.");

        return DBService_Item.getAllItems(connection, false);
    }

	@Override
	public Item getItemById(int itemID) throws NullPointerException
	{
		if(itemID == 0) {
			logErr("Item-ID may not be null.");
			throw new NullPointerException("No Item-ID given.");
		}

		//logInf("Getting Item with ID " + itemID + ".");

		Item item = DBService_Item.getItemByID(connection, itemID);
		if(item != null)
			return item;

		logErr("Item with ID " + itemID + " doesn't exist in the database.");
		throw new NullPointerException("Item-ID " + itemID + " not found.");
	}

	@Override
    public ArrayList<Item> getAllAvailableItems() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting all available Items from MySQL-Database.");

		return DBService_Item.getAllItems(connection, true);
	}

	// Tables
    @Override
    public ArrayList<Table> getAllTables() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting Tables from MySQL-Database.");

		return DBService_Table.getAllTables(connection, false);
    }

	@Override
	public Table getTableById(int tableID) throws NullPointerException
	{
		if(tableID == 0) {
			logErr("Table-ID may not be null.");
			throw new NullPointerException("No Table-ID given.");
		}

		logInf("Getting Table with ID " + tableID + ".");

		Table table = DBService_Table.getTableById(connection, tableID);
		if(table != null)
			return table;

		logErr("Table with ID " + tableID + " doesn't exist in the database.");
		throw new NullPointerException("Table-ID " + tableID + " not found.");
	}

	@Override
    public ArrayList<Table> getAllAvailableTables() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting all available Tables from MySQL-Database.");

		return DBService_Table.getAllTables(connection, true);
	}

	// Orders
    @Override
    public ArrayList<Order> getAllOrders() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Orders from MySQL-Database.");

        return DBService_Order.getAllOrders(connection);
    }
	@Override
	public Order getOrderById(int orderID) throws NullPointerException
	{
		if(orderID == 0) {
			logErr("Order-ID may not be null.");
			throw new NullPointerException("No Order-ID given.");
		}

		logInf("Getting Order with ID " + orderID + ".");

		Order order = DBService_Order.getOrderByID(connection, orderID);
		if(order != null)
			return order;

		logErr("Order with ID " + orderID + " doesn't exist in the database.");
		throw new NullPointerException("Order-ID " + orderID + " not found.");
	}

	@Override
	public float getOrderPrice(int orderID) throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting price of order form MySQL-Database.");

		return DBService_Order.getPrice(connection, orderID);
	}

	// Itemdeliveries
    @Override
    public ArrayList<Itemdelivery> getAllItemdeliveries() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Itemdeliveries from MySQL-Database.");

		return DBService_Itemdelivery.getAllItemdeliveries(connection);
    }

	@Override
	public Itemdelivery getItemdeliveryById(int itemdeliveryID) throws NullPointerException
	{
		if(itemdeliveryID == 0) {
			logErr("Itemdelivery-ID may not be null.");
			throw new NullPointerException("No Itemdelivery-ID given.");
		}
    
		logInf("Getting Itemdelivery with ID " + itemdeliveryID + ".");

		Itemdelivery itemdelivery = DBService_Itemdelivery.getItemdeliveryByID(connection, itemdeliveryID);
		if(itemdelivery != null)
			return itemdelivery;

		logErr("Itemdelivery with ID " + itemdeliveryID + " doesn't exist in the database.");
		throw new NullPointerException("Itemdelivery-ID " + itemdeliveryID + " not found.");

	}

    // OrderedItems
    @Override
    public ArrayList<OrderedItem> getAllOrderedItems() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting OrderedItems from MySQL-Database.");

        return DBService_OrderedItem.getAllOrderedItems(connection, false);
    }

	@Override
	public OrderedItem getOrderedItemById(int orderedItemID) throws NullPointerException
	{
		if(orderedItemID == 0) {
			logErr("OrderedItem-ID may not be null.");
			throw new NullPointerException("No OrderedItem-ID given.");
		}

		logInf("Getting OrderedItem with ID " + orderedItemID + ".");

		OrderedItem orderedItem = DBService_OrderedItem.getOrderedItemById(connection, orderedItemID);
		if(orderedItem != null)
			return orderedItem;

		logErr("OrderedItem with ID " + orderedItemID + " doesn't exist in the database.");
		throw new NullPointerException("OrderedItem-ID " + orderedItemID + " not found.");
	}

	public ArrayList<OrderedItem> getAllUnproducedOrderedItems()
	{
		checkConnection();

		logInf("Getting OrderedItems from MySQL-Database.");

		return DBService_OrderedItem.getAllOrderedItems(connection, true);
	}

	public ArrayList<OrderedItem> getAllUnproducedOrderedItemsByItemId(int itemID)
	{
		checkConnection();

		logInf("Getting unproduced OrderedItems from MySQL-Database with Item-ID " + itemID + ".");

		return DBService_OrderedItem.getAllUnproducedOrderedItemsByItemId(connection, itemID);
	}

	@Override
    public ArrayList<OrderedItem> getOrderedItemsByOrderId(int orderID) throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting OrderedItems with Order-ID " + orderID + " from MySQL-Database.");

        return DBService_OrderedItem.getOrderedItemsByOrderId(connection, orderID);
    }

	@Override
    public ArrayList<OrderedItem> getOrderedItemsByItemId(int itemID) throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting OrderedItems with Item-ID " + itemID + " from MySQL-Database.");

        return DBService_OrderedItem.getOrderedItemsByItemId(connection, itemID);
    }

    // Waiters
	@Override
	public ArrayList<Waiter> getAllWaiters() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting Waiters from MySQL-Database.");

		return DBService_Waiter.getAllWaiters(connection);
	}

	@Override
	public Waiter getWaiterByID(int waiterID)
	{
		checkConnection();

		//logInf("Getting Waiter with ID " + waiterID + ".");

		return DBService_Waiter.getWaiterByID(connection, waiterID);
	}

	// Logindata
	@Override
	public ArrayList<Logindata> getAllLogindata() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting Logindata from MySQL-Database.");

		return DBService_LoginData.getAllLogindata(connection);
	}
	//endregion

	//region Adding data to the database
  	//Adding data to the database
  	@Override
  	public void addItem(Item item) throws MySQLServerConnectionException, DataException
	{
        checkConnection();

        logInf("Adding Item to MySQL-Database.");

        if(item.getItemID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Item was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        //Vollständigkeit der Order ueberpruefen
        if(item.getQuantity() == 0) {
            logErr("When adding item the quantity may not be zero.");
            throw new DataException("Es wurde keine Anzahl gesetzt!");
        }
        isItemComplete(item);

        DBService_Item.addItem(connection, item);
    }

    @Override
    public void addTable(Table table) throws MySQLServerConnectionException, DataException
	  {
        checkConnection();

        logInf("Adding Table to MySQL-Database.");

        //this.tables = this.getAllTables();

        //Vollständigkeit des Tables überprüfen
        if(table.getTableID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Table was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isTableComplete(table);

        DBService_Table.addTable(connection, table);
    }

    @Override
    public int addOrder(Order order) throws MySQLServerConnectionException, DataException
	{
        checkConnection();

        logInf("Adding Order to MySQL-Database.");

        //Vollständigkeit der Order ueberpruefen
        if(order.getOrderID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Order was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isOrderComplete(order);

        return DBService_Order.addOrder(connection, order);
    }

    @Override
    public void addItemdelivery(Itemdelivery itemdelivery) throws MySQLServerConnectionException,
            DataException
	{
        checkConnection();

        logInf("Adding Itemdelivery to MySQL-Database.");

        //Vollständigkeit der Order ueberpruefen
        if(itemdelivery.getItemdeliveryID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Itemdelivery was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isItemdeliveryComplete(itemdelivery);

        DBService_Itemdelivery.addItemdelivery(connection, itemdelivery);
    }
	@Override
	public void addOrderedItem(OrderedItem orderedItem) throws MySQLServerConnectionException,
			DataException
	{
		checkConnection();

		logInf("Adding OrderedItem to MySQL-Database.");

		//Vollständigkeit der Order ueberpruefen
		if(orderedItem.getOrderedItemID() != 0) {
			logErr("ID may not be set by the user.");
			logErr("OrderedItem was not added to the Database!");
			throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
		}
		isOrderedItemComplete(orderedItem);

		DBService_OrderedItem.addOrderedItem(connection, orderedItem);
	}

	@Override
	public void addWaiter(Waiter waiter) throws MySQLServerConnectionException, DataException
	{
		checkConnection();

		logInf("Adding Waiter to MySQL-Database.");

		//Vollständigkeit des waiters überprüfen
		if(waiter.getWaiterID() != 0) {
			logErr("ID may not be set by the user.");
			logErr("Waiter was not added to the Database!");
			throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
		}

		isWaiterComplete(waiter);

		DBService_Waiter.addWaiter(connection, waiter);
	}

	@Override
	public void addLogindata(Logindata logindata) throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Adding Logindata to MySQL-Database.");

		// Existenz der waiterID überprüfen
		if(!existsWaiterWithID(logindata.getWaiterID())) {
			logErr("Waiter with ID " + logindata.getWaiterID() + " does not exist in the database! ");
			throw new DataException("Bedienung mit der ID " + logindata.getWaiterID() +
					" existiert nicht in der Datenbank!");
		}

		//Vollständigkeit der Logindata überprüfen
		isLogindataComplete(logindata);

		DBService_LoginData.addLogindata(connection, logindata);
	}
	//endregion

	//region Updating data in database
    @Override
    public void updateItem(int itemID, Item item) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        checkConnection();

        logInf("Updating Item with ID " + itemID + ".");

        if(itemID == 0) {
            logErr("Item-ID may not be null.");
            throw new NullPointerException("No Item-ID given.");
        }

        //Existenz eines Items mit der itemID überprüfen
        if(!existsItemWithID(itemID)) {
            logErr("Item with ID " + itemID + " does not exist in the database! ");
            throw new DataException("Artikel mit der ID " + itemID + " existiert nicht in der Datenbank!");
        }

        isItemComplete(item);

        DBService_Item.updateItem(connection, item, itemID);
    }

    @Override
    public void updateTable(int tableID, Table table) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        checkConnection();

        logInf("Updating Table with ID " + tableID + ".");

        if(tableID == 0) {
            logErr("Table-ID may not be null.");
            throw new NullPointerException("No Table-ID given.");
        }

        //Existenz eines Tables mit der tableID überprüfen
        if(!existsTableWithID(tableID)) {
            logErr("Table with ID " + tableID + " does not exist in the database! ");
            throw new DataException("Tisch mit der ID " + tableID + " existiert nicht in der Datenbank!");
        }

        isTableComplete(table);

        DBService_Table.updateTable(connection, table, tableID);
    }

    @Override
    public void updateOrder(int orderID, Order order) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        checkConnection();

        logInf("Updating Order with ID " + orderID + ".");

        if(orderID == 0) {
            logErr("Order-ID may not be null.");
            throw new NullPointerException("No Order-ID given.");
        }

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID)) {
            logErr("Order with ID " + orderID + " does not exist in the database! ");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank!");
        }

        isOrderComplete(order);

        //Kontrollieren ob ID existiert
        if(this.getOrderById(orderID) == null)
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht!");

       // Bei jedem Update die Differenz zur bisherigen Bestellung erkennen und in einem Kuechenbeleg ausdrucken
        // Das Ausdrucken passiert nun beim Hinzufügen von orderedItems in dem Rest-Controller.
		// Dabei wird eine Liste von orderedItems übertragen. Anschließend werden diese Übertragenen Items ausgedruckt.

		DBService_Order.updateOrder(connection, order, orderID);
    }

	@Override
	public void updateOrderedItem(int orderedItemID, OrderedItem orderedItem)
	{
		checkConnection();

		logInf("Updating OrderedItem with ID " + orderedItemID + ".");

		if(orderedItemID == 0) {
			logErr("OrderedItem-ID may not be null.");
			throw new NullPointerException("No OrderedItem-ID given.");
		}

		//Existenz OrderedItems mit der OrderedItemID überprüfen
		if(!existsOrderedItemWithID(orderedItemID)) {
			logErr("OrderedItem with ID " + orderedItemID + " does not exist in the database! ");
			throw new DataException("Bestellter Artikel mit der ID " + orderedItemID + " existiert nicht in der Datenbank!");
		}

		isOrderedItemComplete(orderedItem);

		//Kontrollieren ob ID existiert
		if(!existsOrderedItemWithID(orderedItemID))
			throw new DataException("Bestellter Artikel mit der ID " + orderedItemID + " existiert nicht!");

		DBService_OrderedItem.updateOrderedItem(connection, orderedItem, orderedItemID);
	}

	@Override
	public void updateWaiter(int waiterID, Waiter waiter)
	{
		checkConnection();

		logInf("Updating Waiter with ID " + waiterID + ".");

		if(waiterID == 0) {
			logErr("Waiter-ID may not be null.");
			throw new NullPointerException("No Waiter-ID given.");
		}

		// Existenz einer Bedienung mit der ID überprüfen
		if(!existsWaiterWithID(waiterID)) {
			logErr("Waiter with ID " + waiter + " does not exist in the database! ");
			throw new DataException("Bedienung mit der ID " + waiterID + " existiert nicht in der Datenbank!");
		}

		isWaiterComplete(waiter);

		DBService_Waiter.updateWaiter(connection, waiterID, waiter);
	}

	@Override
	public void updateLogindata(Logindata logindata)
	{
		checkConnection();

		logInf("Updating Logindata with Waiter-ID " + logindata.getWaiterID() + ".");

		if(logindata.getWaiterID() == 0) {
			logErr("Waiter-ID may not be null.");
			throw new NullPointerException("No Waiter-ID given.");
		}

		// Existenz einer Bedienung mit der ID überprüfen
		if(!existsLogindataWithWaiterID(logindata.getWaiterID())) {
			logErr("Logindata with waiter-ID " + logindata.getWaiterID() + " does not exist in the database! ");
			throw new DataException("Logindaten mit der Bedienungs-ID " + logindata.getWaiterID()
					+ " existiert nicht in der Datenbank!");
		}

		isLogindataComplete(logindata);

		DBService_LoginData.updateLogindata(connection, logindata);
	}
	//endregion

	//region Deleting data from database
    /*
      Beim Löschen eines Items oder Tables wird dieser nur als nicht verfügbar markiert, Waiter werden gekündigt,
      verbleiben aber in der Datenbank.
      Somit ist sichergestellt, dass für bisherige Orders alle Daten verfügbar bleiben.
     */
    @Override
    public void deleteItem(int itemID) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        logInf("Setting Item with ID " + itemID + " to unavailable.");

        Item item = this.getItemById(itemID);

        item.setAvailable(false);

        this.updateItem(itemID, item);
    }

    @Override
    public void deleteTable(int tableID) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        logInf("Setting Table with ID " + tableID + " to unavailable.");

        Table table = this.getTableById(tableID);

        table.setAvailable(false);

        this.updateTable(tableID, table);
    }

    @Override
    public void deleteOrder(int orderID) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        logInf("Deleting Order with ID " + orderID + ".");

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID)) {
            logErr("Order with ID " + orderID + " does not exist in the database! Nothing was deleted.");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank! " +
                    "Es konnte nichts gelöscht werden.");
        }

        DBService_Order.deleteOrder(connection, orderID);
    }

    @Override
    public void deleteItemdelivery(int itemdeliveryID) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        logInf("Deleting Itemdelivery with ID " + itemdeliveryID + ".");

        //Existenz einer Order mit der OrderID überprüfen
        if(!existsItemdeliveryWithID(itemdeliveryID)) {
            logErr("Itemdelivery with ID " + itemdeliveryID + " does not exist in the database!" +
					" Nothing was deleted.");
            throw new DataException("Wareneingang mit der ID " + itemdeliveryID + " existiert nicht in der Datenbank! " +
                    "Es konnte nichts gelöscht werden.");
        }

        DBService_Itemdelivery.deleteItemdelivery(connection, itemdeliveryID);
    }

	@Override
	public void deleteOrderedItem(int orderedItemID)
	{
		logInf("Deleting OrderedItem with ID " + orderedItemID + ".");

		//Existenz einer OrderedItem mit der orderedItemID überprüfen
		if(!existsOrderedItemWithID(orderedItemID)) {
			logErr("OrderedItem with ID " + orderedItemID + " does not exist in the database!" +
					" Nothing was deleted.");
			throw new DataException("Bestellter Artikel mit der ID " + orderedItemID +
					" existiert nicht in der Datenbank! Es konnte nichts gelöscht werden.");
		}

		DBService_OrderedItem.deleteOrderedItem(connection, orderedItemID);
	}

	public void deleteOrderedItemByOrderId(int orderID)
	{
		DBService_OrderedItem.deleteOrderedItemByOrderId(connection, orderID);
	}

	@Override
	public void deleteWaiter(int waiterID)
	{
		logInf("Unemploying Waiter with ID " + waiterID + ".");

		Waiter waiter = this.getWaiterByID(waiterID);

		waiter.setEmployed(false);

		this.updateWaiter(waiterID, waiter);
	}

	@Override
	public void deleteLogindata(int waiterID)
	{
		logInf("Deleting Logindata with waiter-ID " + waiterID + ".");

		//Existenz eines Logindata mit der waiterID überprüfen
		if(!existsLogindataWithWaiterID(waiterID)) {
			logErr("Logindata with waiter-ID " + waiterID + " does not exist in the database!" +
					" Nothing was deleted.");
			throw new DataException("Login-Daten mit der Bedienungs-ID " + waiterID +
					" existieren nicht in der Datenbank! Es konnte nichts gelöscht werden.");
		}

		DBService_LoginData.deleteLogindata(connection, waiterID);
	}
	//endregion

	//region Drucken
	@Override
    public void printOrderById(int orderID) throws NullPointerException, DataException,
            MySQLServerConnectionException
	{
        if(orderID == 0) {
            logErr("Order-ID is null.");
            throw new NullPointerException("No Order-ID given.");
        }

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID)) {
            logErr("Order with ID " + orderID + " does not exist in the database!");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank! Es kann nichts gedruckt werden.");
        }

        this.printReceipt(orderID);
    }

    // Funktion die selbe wie printOrderById(), bis auf Überprüfung
	// Ist die Methode überhaupt notwendig?
	@Override
	public void printReceipt(int orderID)
	{
		PrinterService printerService = new PrinterService();
		printerService.printReceipt(orderID);
	}

	@Override
	public void printOrder(int orderID, ArrayList<OrderedItem> orderedItems)
	{
		PrinterService printerService = new PrinterService();
		printerService.printOrder(orderID, orderedItems);
	}

	@Override
	public void printLogindata(String loginname, String password, Waiter waiter)
	{
		PrinterService printerService = new PrinterService();

		printerService.printLogindata(loginname, password, waiter);
	}

	@Override
	public void printDataConflict(ArrayList<OrderedItem> orderedItems)
	{
		PrinterService printerService = new PrinterService();

		printerService.printDataConflict(orderedItems);
	}
	//endregion

	/**
	 * Überprüft Logindaten.
	 * @param loginname Der übergebene Loginname.
	 * @param passwordHash Der übergebene Password-Hash.
	 * @return Ob der Login mit den Daten erfolgreich war.
	 * @throws NotAuthentificatedException Wenn der Login nicht erfolgreich war.
	 */
	public boolean authentificate(String loginname, String passwordHash)
			throws NotAuthentificatedException
	{
		return DBService_LoginData.authentificate(connection, loginname, passwordHash);
	}

	public int getWaiterIdByLoginData(String loginname, String passwordhash)
			throws NotAuthentificatedException
	{
		return DBService_LoginData.getWaiterIdByLogindata(connection, loginname, passwordhash);
	}

	/**
	 * FIXME: ACHTUNG: Diese Klasse wird im Programmablauf nicht verwendet und ist unsicher.
	 * 			Sie ist aber die Sicherheitstechnisch bessere als die verwendete. Allerdings tritt
	 * 			bei der Verwendung ein Problem auf, das nicht gelöst werden konnte:
	 * 			Bei der Übertragung des Passworts von der App an die Rest-API kommt es zu einem Fehler,
	 * 			weil nicht erlaubte Zeichen übertragen wurden. Daher wurde die Standard-Hash-Funktion der String-Klasse
	 * 			verwendet. Wenn möglich sollte das System aber auf diese sicherere Funktion ausgelegt werden.
	 *
	 * Verschlüsselt ein Passwort als 256bit Hash-Code. Im GUI wird das Password eingegegeben und mit dieser
	 * Funktion in einen Hash-Code gewandelt. Dieser wird in der Datenbank abgelegt.
	 * Diese Verschlüsselungs-Funktion muss mit der in der Android-App verwendeten übereinstimmen.
	 * @param password Das zu verschlüsselnde Passwort.
	 * @return Den Hash-Code als String
	 */
	public String generateHash(String password)
	{
		try
		{
			String encryptedString;

			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			encryptedString = new String(messageDigest.digest());

			return encryptedString;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//region Vollständigkeit der Daten von Objekten überprüfen
	/*
	 * Folgende Funktionen überprüfen einen Datensatz auf ihre vollständigkeit. Wenn ein Datensatz nicht vollständig
	 * ist, wird ein Fehlertext zusammengestellt, in dem alle fehlenden/fehlerhaften Daten vermerkt sind. Diese
	 * wird anschließend in einer DataException ausgegeben.
	 */
    private void isOrderComplete(Order order)
	{
        String missingAttributs = "";

        if(order.getTable() == 0) {
            missingAttributs += "Tisch ";
            logErr("Table-ID missing.");
        }

        if(!missingAttributs.isEmpty()) {
            logErr("Order was not added to the database!");
            throw new DataException("Die Bestellung ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }

        if(!existsTableWithID(order.getTable())) {
            logErr("The Table-ID does not exist in the database!");
            logErr("Order was not added to the database!");
            throw new DataException("Die angegebene Table-ID existiert nicht in der Datenbank!");
        }
    }

    private boolean isTableComplete(Table table)
	{
        String missingAttributs = "";

        if(table.getName().isEmpty()) {
            missingAttributs += " Name";
            logErr("Table missing.");
        }
        //Nur Verfügbarkeit von neuen Artikeln darf nicht auf false gesetzt werden
        if(table.isAvailable() == false && table.getTableID() == 0) {
            missingAttributs += "Available ";
            logErr("New Table cannot be set unavailable.");
        }
        if(!missingAttributs.isEmpty()) {
            logErr("Table was not added to the database!");
            throw new DataException("Der Tisch ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }
        return true;

    }

    private boolean isItemComplete(Item item)
	{
        String missingAttributs = "";

        if(item.getName().isEmpty() || item.getName() == null) {
            missingAttributs += "Name ";
            logErr("Name is missing.");
        }
        //Nur Verfügbarkeit von neuen Artikeln darf nicht auf false gesetzt werden
        if(item.isAvailable() == false && item.getItemID() == 0) {
            missingAttributs += "Available ";
            logErr("New Item cannot be set unavailable.");
        }
        if(!missingAttributs.isEmpty()) {
            logErr("Item was not added to the Database!");
            throw new DataException("Der Artikel ist unvollständig! Folgende Attribute fehlen: " + missingAttributs);
        }
        return true;

    }

    private boolean isItemdeliveryComplete(Itemdelivery itemdelivery)
	{
        String missingAttributs = "";

        if(itemdelivery.getItemID() == 0) {
            missingAttributs += " Artikel";
            logErr("Item-ID missing");
        }
        if(itemdelivery.getQuantity() == 0) {
            missingAttributs += " Anzahl";
            logErr("Quantity missing.");
        }

        if(!missingAttributs.isEmpty()) {
            logErr("Itemdelivery was not added to the database!");
            throw new DataException("Der Wareneingang ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }

        if(!existsItemWithID(itemdelivery.getItemID())) {
            logErr("The Item-IDs does not exist in the Database!");
            logErr("Itemdelivery was not added to the Database!");
            throw new DataException("Die angegebene Artikel-ID existieren nicht in der Datenbank.");
        }

        return true;

    }

    private boolean isOrderedItemComplete(OrderedItem orderedItem)
	{
		String missingAttributs = "";


		if(orderedItem.getOrderID() == 0) {
			missingAttributs += " OrderID";
			logErr("OrderID missing.");
		}
		if(orderedItem.getItemID() == 0) {
			missingAttributs += " ItemID";
			logErr("ItemID missing.");
		}

		if(!missingAttributs.isEmpty()) {
			logErr("OrderedItem was not added to the database!");
			throw new DataException("Der bestellte Artikel ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
		}

		return true;
	}

	private boolean isWaiterComplete(Waiter waiter)
	{
		String missingAttributs = "";

		if(waiter.getPrename().isEmpty()) {
			missingAttributs += " Vorname";
			logErr("Prename missing.");
		}
		if(waiter.getLastname().isEmpty()) {
			missingAttributs += " Nachname";
			logErr("Lastname missing.");
		}

		if(!missingAttributs.isEmpty()) {
			logErr("Waiter was not added to the database!");
			throw new DataException("Die Bedienung ist unvollständig! Die folgenden Parameter fehlen: "
					+ missingAttributs);
		}

		return true;
	}

	private boolean isLogindataComplete(Logindata logindata)
	{
		String missingAttributs = "";

		if(logindata.getWaiterID() == 0) {
			missingAttributs += " Bedienungs-ID";
			logErr("WaiterID missing.");
		}
		if(logindata.getLoginname().isEmpty()) {
			missingAttributs += " Login-Name";
			logErr("Loginname missing.");
		}
		if(logindata.getPasswordHash().isEmpty()) {
			missingAttributs += " Passwort";
			logErr("Password missing.");
		}

		if(!missingAttributs.isEmpty()) {
			logErr("Logindata was not added to the database!");
			throw new DataException("Die Login-Daten sind unvollständig! Die folgenden Parameter fehlen: "
					+ missingAttributs);
		}

		return true;
	}
	//endregion

	//region Existenz von Objekten mit ID überprüfen
    private boolean existsItemWithID(int itemID)
	{

        return DBService_Item.existsItemWithID(connection, itemID);
    }

    private boolean orderIsAvailable(int orderID)
	{

        return DBService_Order.existsOrderWithID(connection, orderID);
    }

    private boolean existsTableWithID(int tableID)
	{

        return DBService_Table.existsTableWithID(connection, tableID);
    }

    private boolean existsItemdeliveryWithID(int itemdeliveryID)
	{
        return DBService_Itemdelivery.existsItemdeliveryWithID(connection, itemdeliveryID);
    }

    public boolean existsOrderedItemWithID(int orderedItemID)
	{
		return DBService_OrderedItem.existsOrderedItemWithID(connection, orderedItemID);
	}

	private boolean existsWaiterWithID(int waiterID)
	{

		return DBService_Waiter.existsWaiterWithID(connection, waiterID);
	}

	public boolean existsLogindataWithWaiterID(int waiterID)
	{
		return DBService_LoginData.existsLogindataWithWaiterID(connection, waiterID);
	}

	public boolean existsLogindataWithLoginname(String loginname)
	{
		return DBService_LoginData.existsLogindataWithLoginname(connection, loginname);
	}
	//endregion

	static double round(double number)
	{

		return (double) Math.round(number * 100d) / 100d;
	}

	//Konverter

    /**
     * Konvertiert ein joda.time.DateTime in einen Timestamp, der in der SQL-Datenbank gespeicher weren kann
     * @param dateTime zu konvertierendes joda.time.DateTime
     * @return Timestamp
     */
    static Object convertJodaDateTimeToSqlTimestamp(DateTime dateTime)
	{
        // Convert sql-Timestamp to joda.DateTime
        return new Timestamp(dateTime.getMillis());
    }

    /**
     * Konvertiert einen Timestamp aus der SQL-Datenbank in eine joda.time.DateTime.
     * @param sqlTimestamp zu konvertierender Timestamp
     * @return jode.time.DateTime
     */
    static DateTime convertSqlTimestampToJodaDateTime(Timestamp sqlTimestamp)
	{
        //Convert joda.DateTime to sql-Timestamp
        return new DateTime(sqlTimestamp);
    }
}