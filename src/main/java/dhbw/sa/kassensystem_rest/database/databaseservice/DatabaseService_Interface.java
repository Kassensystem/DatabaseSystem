package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.*;
import dhbw.sa.kassensystem_rest.exceptions.DataException;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logErr;
import static dhbw.sa.kassensystem_rest.database.databaseservice.Log.logInf;

/**
 * Der DatabaseService stellt die Schnittstelle zu einer MySQL-Datenbank dar.
 *
 *  @throws MySQLServerConnectionException, wenn zur Laufzeit ein Fehler
 *  bei der Verbindung mit der MySQL-Datenbank auftritt.  Die genaue Fehler-
 *  meldung ist der Message zu entnehmen.
 *  @throws DataException, wenn ein
 *  unvollstaendiger Datensatz (bis auf die ID fehlt ein weiteres Attribut)
 *  in die Datenbank gespeichert werden soll.
 *
 * @author Marvin Mai
 */

public interface DatabaseService_Interface
{
	/*
	TODO Java-Doc-Kommentare der neuen Methoden ausschreiben.
	 */

    /**
     * Stellt eine Verbindung zur MySQL-Datenbank her.
     * @throws IllegalStateException wenn die Datenbank nicht erreichbar ist.
     */
    static Connection connect() {
		logInf("Connecting database...");

		Connection connection;

		try{
			connection = DriverManager.getConnection(DatabaseProperties.getUrl(), DatabaseProperties.getUsername(),
					DatabaseProperties.getPassword());
			logInf("Database connected!");
		}catch(SQLException e) {
			logErr("Verbindung zur Datenbank fehlgeschlagen!");
			throw new MySQLServerConnectionException();
		}
		return connection;
	}

    /**
     * Beendet eine bestehende Verbindung mit einem MySQL-Server.
     */
    void disconnect();

	//region Getting Table-Data from the database
	// Items
    /**
     * Fragt die Artikel der Datenbank ab.
     * @return Artikel der Datenbank.
     */
    ArrayList<Item> getAllItems();

	/**
	 * Liefert ein {@link Item} in Abhängigkeit von einer ID.
	 * @param itemID ID des Artikels.
	 * @return Item mit angegebener ID.
	 */
	Item getItemById(int itemID);

	/**
	 * TODO
	 * @return
	 */
	ArrayList<Item> getAllAvailableItems();

	// Tables
    /**
     * Fragt die Tische der Datenbank ab.
     * @return Tische der Datenbank.
     */
    ArrayList<Table> getAllTables();

	/**
	 * Liefert eine {@link Table} in Abhängigkeit von einer ID.
	 * @param tableID ID des Tisches.
	 * @return Table mit angegebener ID.
	 */
	Table getTableById(int tableID);

	/**
	 * TODO
	 * @return
	 */
	ArrayList<Table> getAllAvailableTables();

	// Orders
    /**
     * Fragt die Bestellungen der Datenbank ab.
     * @return Bestellungen der Datenbank.
     */
    ArrayList<Order> getAllOrders();

	/**
	 * Liefert eine Bestellung in Abhängigkeit von einer ID.
	 * @param orderID ID der Bestellung.
	 * @return Order mit angegebener ID.
	 */
	Order getOrderById(int orderID);

	/**
	 * TODO
	 * @param orderID
	 * @return
	 */
	float getOrderPrice(int orderID);

	// Itemdeliveries
    /**
     * Fragt die Wareneingaenge der Datenbank ab.
     * @return Wareneingaenge der Datenbank.
     */
    ArrayList<Itemdelivery> getAllItemdeliveries();

	/**
	 * TODO
	 * @param itemdeliveryID
	 * @return
	 */
	Itemdelivery getItemdeliveryById(int itemdeliveryID);

	// OrderedItems
    /**
     * TODO
     * @return
     */
    ArrayList<OrderedItem> getAllOrderedItems();

	/**
	 * TODO
	 * @param orderedItemID
	 * @return
	 */
	OrderedItem getOrderedItemById(int orderedItemID);

	/**
	 * TODO
	 * @param orderID
	 * @return
	 */
	ArrayList<OrderedItem> getOrderedItemsByOrderId(int orderID);

	/**
	 * TODO
	 * @param itemID
	 * @return
	 */
	ArrayList<OrderedItem> getOrderedItemsByItemId(int itemID);

	// Waiters

	/**
	 * TODO
	 * @return
	 */
	ArrayList<Waiter> getAllWaiters();

	/**
	 * TODO
	 * @param waiterID
	 * @return
	 */
	Waiter getWaiterByID(int waiterID);

	// Logindata
	/**
	 * TODO
	 * @return
	 */
	ArrayList<Logindata> getAllLogindata();
	//endregion

	//region Adding data to the database
    /**
     * Fuegt der Datenbank einen neuen Artikel hinzu.
     * @param item neuer Artikel.
     */
    void addItem(Item item);

    /**
     * Fuegt der Datenbank einen neuen Tisch hinzu.
     * @param table neuer Tisch.
     */
    void addTable(Table table);

    /**
     * Fuegt der Datenbank eine neue Bestellung hinzu.
     * @param order neue Bestellung.
     */
    int addOrder(Order order);

    /**
     * Fügt der Datenbank einen neuen Wareneingang hinzu.
     * @param itemdelivery neuer Wareneingang.
     */
    void addItemdelivery(Itemdelivery itemdelivery);

	/**
	 * Fügt der Datenbank ein neues OrderedItem hinzu.
	 * @param orderedItem Das hinzuzufügende OrderedItem.
	 */
	void addOrderedItem(OrderedItem orderedItem);

	/**
	 * TODO
	 * @param waiter
	 */
	void addWaiter(Waiter waiter);

	/**
	 * TODO
	 * @param logindata
	 */
	void addLogindata(Logindata logindata);
	//endregion

	//region Updating data in database
    /**
     * Aktualisiert die Daten eines Artikels.
     * @param itemID ID des zu aktualisierenden Artikels.
     * @param item neue Artikeldaten.
     */
    void updateItem(int itemID, Item item);

    /**
     * Aktualisiert die Daten eines Tisches.
     * @param tableID ID des zu aktualisierenden Tisches.
     * @param table neue Tischdaten.
     */
    void updateTable(int tableID, Table table);

    /**
     * Aktualisiert die Daten einer Bestellung.
     * @param orderID ID der zu aktualisierenden Bestellung.
     * @param order neue Bestellungsdaten.
     */
    void updateOrder(int orderID, Order order);

	/**
	 * Aktualisiert die Daten eines OrderedItem,
	 * @param orderedItemID ID des zu aktualisierenden OrderdItems.
	 * @param orderedItem Neue OrderedItem Daten.
	 */
	void updateOrderedItem(int orderedItemID, OrderedItem orderedItem);

	/**
	 * TODO
	 * @param waiterID
	 * @param waiter
	 */
	void updateWaiter(int waiterID, Waiter waiter);

	/**
	 * TODO
	 * @param logindata
	 */
	void updateLogindata(Logindata logindata);
	//endregion

	//region Deleting data from the database
    /**
     * Markiert einen Artikel als nicht verfuegbar. Daten werden nicht geloescht.
     * @param itemID ID des als nicht verfuegbar zu markierenden Artikels,
     */
    void deleteItem(int itemID);

    /**
     * Markiert einen Tisch als nicht verfuegbar. Daten werden nicht geloescht.
     * @param tableID ID des als nicht verfuegbar zu markierenden Tisches,
     */
    void deleteTable(int tableID);

    /**
     * Loescht eine Bestellung aus der Datenbank.
     * @param orderID ID der zu loeschenden Bestellungen.
     */
    void deleteOrder(int orderID);

    /**
     * Löscht einen Wareneingang aus der Datenbank.
     * @param itemdeliveryID ID des zu loeschenden Wareneingangs.
     */
    void deleteItemdelivery(int itemdeliveryID);

	/**
	 * Löscht ein OrderedItem aus der Datenbank.
	 * @param orderedItemID Die ID des zu löschen OrderedItems.
	 */
	void deleteOrderedItem(int orderedItemID);

	/**
	 * TODO
	 * @param waiterID
	 */
	void deleteWaiter(int waiterID);

	/**
	 * TODO
	 * @param waiterID
	 */
	void deleteLogindata(int waiterID);
	//endregion

	//region Drucken einer Order
    /**
     * Ausdrucken einer Bestellung in Abhängigkeit von einer ID;
     * @param orderID ID der auszudruckenden Order.
     */
    public void printOrderById(int orderID);

	/**
	 * TODO
	 * @param orderID
	 */
	void printReceipt(int orderID);

	/**
	 * TODO
	 * @param orderID
	 * @param orderedItems
	 */
	void printOrder(int orderID, ArrayList<OrderedItem> orderedItems);
	//endregion


}
