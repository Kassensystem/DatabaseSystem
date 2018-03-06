package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.*;
import dhbw.sa.kassensystem_rest.exceptions.DataException;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import dhbw.sa.kassensystem_rest.database.printer.PrinterService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logErr;
import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logInf;

/**
 * {@inheritDoc}
 *
 * Implementierung des DatabaseService_Interfaces
 *
 * @author Marvin Mai
 */

@Service
public class DatabaseService implements DatabaseService_Interface
{
    /*
     *  items Alle in der Datenbank befindlichen Items werden hier gespeichert.
     *  tables Alle in der Datenbank befindlichen Tables werden hier gespeichert.
     *  orders Alle in der Datenbank befindlichen Orders werden hier gespeichert.
     *  itemdeliveries Alle in der Datenbank befindlichen itemdeliveries werden hier gespeichert.
     *  connection Eine Instanz einer Verbindung zur Datenbank.
     *  DatabaseProperties Beinhaltet eine Beschreibung der Daten die zur Verbindung zur Datenbank noetig sind.
     */

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

    //Getting Table-Data from the database
    @Override
    public ArrayList<Item> getAllItems() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Items from MySQL-Database.");

        return DBService_Item.getAllItems(connection);
    }
    @Override
    public ArrayList<Table> getAllTables() throws MySQLServerConnectionException
	{
		checkConnection();

		logInf("Getting Tables from MySQL-Database.");

		return DBService_Table.getAllTables(connection);
    }
    @Override
    public ArrayList<Order> getAllOrders() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Orders from MySQL-Database.");

        return DBService_Order.getAllOrders(connection);
    }
    @Override
    public ArrayList<Itemdelivery> getAllItemdeliveries() throws MySQLServerConnectionException
	{
        checkConnection();

        logInf("Getting Itemdeliveries from MySQL-Database.");

		return DBService_Itemdelivery.getAllItemdeliveries(connection);
    }
    @Override
    public ArrayList<OrderedItem> getAllOrderedItems()
	{
        checkConnection();

        logInf("Getting OrderedItems from MySQL-Database.");

        return DBService_OrderedItem.getAllOrderedItems(connection);
    }

    public ArrayList<OrderedItem> getOrderedItemsByOrderId(int orderID)
	{
        checkConnection();

        logInf("Getting OrderedItems from MySQL-Database.");

        return DBService_OrderedItem.getOrderedItemsByOrderId(connection, orderID);
    }

    public ArrayList<OrderedItem> getOrderedItemsByItemId(int itemID)
	{
        checkConnection();

        logInf("Getting OrderedItems from MySQL-Database.");

        return DBService_OrderedItem.getOrderedItemsByItemId(connection, itemID);
    }

    //Datenbankinhalte mit Angabe der ID erhalten
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

    public Item getItemById(int itemID) throws NullPointerException
	{
        if(itemID == 0) {
            logErr("Item-ID may not be null.");
            throw new NullPointerException("No Item-ID given.");
        }

        logInf("Getting Item with ID " + itemID + ".");

		Item item = DBService_Item.getItemByID(connection, itemID);
		if(item != null)
			return item;

        logErr("Item with ID " + itemID + " doesn't exist in the database.");
        throw new NullPointerException("Item-ID " + itemID + " not found.");
    }

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
    public void addOrder(Order order) throws MySQLServerConnectionException, DataException
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

        // TODO Ausdrucklogik überarbeiten:
        if(DBService_Order.isOrderPaid(connection, order.getOrderID())) {
            printOrder(order, true);
            printOrder(order, false);
        }
        else
            printOrder(order, true);

        DBService_Order.addOrder(connection, order);
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

	//Updating data in database
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

        /*if(order.isPaid())
            //Kundenbeleg ausdrucken, wenn bezahlt wird
            printOrder(order, false);*/

        //Bei jedem Update die Differenz zur bisherigen Bestellung erkennen und in einem Kuechenbeleg ausdrucken
        // TODO Ausdrucken der DifferenzOrder auf neues System anpassen
        // Das Ausdrucken passiert nun beim Hinzufügen von orderedItems. Dabei wird eine Liste von orderedItems
        // übertragen. Anschließend werden diese Übertragenen Items ausgedruckt.

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

	//Deleting data from database
    /*
      Beim Löschen eines Items oder Tables wird dieser nur als nicht verfügbar markiert,
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
			throw new DataException("Bestellter Artikel mit der ID " + orderedItemID + " existiert nicht in der Datenbank! " +
					"Es konnte nichts gelöscht werden.");
		}

		DBService_OrderedItem.deleteOrderedItem(connection, orderedItemID);
	}

	//Drucken einer Order

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

        this.printOrder(getOrderById(orderID), false);
    }

    // Vollständigkeit der Daten von Objekten überprüfen
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
    private boolean existsOrderedItemWithID(int orderedItemID)
	{
		return DBService_OrderedItem.existsOrderedItemWithID(connection, orderedItemID);
	}

    /**
     * Druckt eine Order aus.
     * @param order die auszudruckende Order.
     * @param kitchenReceipt sagt dem PrinterService, ob es sich um einen Kuechenbeleg oder Kundenbeleg handelt. Layout
     *                       des ausgedruckten Belegs wird dementsprechend geaendert.
     */
    private void printOrder(Order order, boolean kitchenReceipt)
	{
        PrinterService printerService = new PrinterService();
        printerService.printOrder(order,  this.getAllItems(),
				DBService_OrderedItem.getAllOrderedItems(connection), this.getAllTables(), kitchenReceipt);
    }

    /**
     * Ermittelt die Haeufigkeit einer ID in einer Liste von IDs.
     * @param id ID desjenigen Items, dessen Haeufigkeit in den itemIDs ermittelt werden soll.
     * @param itemIDs ArrayList von Integers mit itemIDs.
     * @return wie oft die id in den itemIDs vorkommt.
     */
    private static int getItemQuantity(int id, ArrayList<Integer> itemIDs)
	{
        if(!itemIDs.contains(id))
            return 0;
        int quantity = 0;
        for(Integer i: itemIDs) {
            if(i.intValue() == id)
                quantity++;
        }
        return quantity;
    }

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
     * Konvertiert einen Timestamp aus der SQL-Datenbank in eine joda.time.DateTime
     * @param sqlTimestamp zu konvertierender Timestamp
     * @return jode.time.DateTime
     */
    static DateTime convertSqlTimestampToJodaDateTime(Timestamp sqlTimestamp)
	{
        //Convert joda.DateTime to sql-Timestamp
        return new DateTime(sqlTimestamp);
    }
}