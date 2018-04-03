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
			e.printStackTrace();
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
	 * Fragt alle verfügbaren Artikel der Datenbank ab.
	 * @return Eine Liste mit allen verfügbaren Artikeln.
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
	 * Liefert alle als verfügbar markierten Tische der Datenbank.
	 * @return Eine Liste aller verfügbaren Tische
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
	 * Berechnet für eine gegebene Order-ID den Preis. Dafür wird für jeden bestellten Artikel ({@link OrderedItem})
	 * der Preis des zugehörigen Artikels ({@link Item}) aufsummiert.
	 * @param orderID ID der Bestellung, deren Preis ermittelt werden soll.
	 * @return Preis des Artikels.
	 */
	float getOrderPrice(int orderID);

	// Itemdeliveries
    /**
     * Fragt die Wareneingaenge der Datenbank ab.
     * @return Wareneingaenge der Datenbank.
     */
    ArrayList<Itemdelivery> getAllItemdeliveries();

	/**
	 * Ermittelt anhand einer gegebenen Itemdelivery-ID den zugehörigen Wareneingang ({@link Itemdelivery}).
	 * @param itemdeliveryID ID des zu ermittelnden Wareneingangs.
	 * @return Wareneingang mit der ID itemdeliveryID.
	 */
	Itemdelivery getItemdeliveryById(int itemdeliveryID);

	// OrderedItems
    /**
     * Ermittelt alle bestellten Artikel ({@link OrderedItem}) der Datenbank.
     * @return Alle bestellten Artikel aus der Datenbank.
     */
    ArrayList<OrderedItem> getAllOrderedItems();

	/**
	 * Bestellten Artikel anhand einer ID ermitteln.
	 * @param orderedItemID ID des bestellten Artikels.
	 * @return Bestellten Artikel {@link OrderedItem} aus der Datenbank.
	 */
	OrderedItem getOrderedItemById(int orderedItemID);

	/**
	 * Ermittelt alle bestellten Artikel, die zu einer Bestellung gehören.
	 * @param orderID ID der Bestellung.
	 * @return Eine Liste mit den bestellten Artikeln der Bestellung.
	 */
	ArrayList<OrderedItem> getOrderedItemsByOrderId(int orderID);

	/**
	 * Ermittelt alle bestellten Artikel, die zu einem vorgegebenen Artikel gehören.
	 * @param itemID Die ID des Artikels.
	 * @return Eine Liste aller bestellten Artikel, die den Artikel mit der itemID beinhalten.
	 */
	ArrayList<OrderedItem> getOrderedItemsByItemId(int itemID);

	// Waiters

	/**
	 * Ermittelt alle Bedienungen, die sich in der MySQl-Datenbank befinden.
	 * @return Eine Liste mit allen Bedienungen.
	 */
	ArrayList<Waiter> getAllWaiters();

	/**
	 * Ermittelt eine Bedienung, die zu einer vorgegebenen ID gehört.
	 * @param waiterID ID der zu ermittelnden Bedienung.
	 * @return Die Bedienung mit der waiterID.
	 */
	Waiter getWaiterByID(int waiterID);

	// Logindata
	/**
	 * Liefert alle Login-Daten, die sich in der Datenbank befinden.
	 * @return Eine Liste mit allen Login-Daten.
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
	 * Fügt der MySQL-Datenbank eine neue Bedienung hinzu.
	 * @param waiter Die hinzuzufügende Bedienung.
	 */
	void addWaiter(Waiter waiter);

	/**
	 * Fügt der Datenbank einen neuen Login-Daten-Eintrag hinzu.
	 * @param logindata Der hinzuzufügende Login-Daten-Satz
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
	 * Aktualisiert eine Bedienung in der Datenbank mit den Daten eines neuen Bedienungs-Datensatzes.
	 * @param waiterID ID der zu aktualisierenden Bedienung.
	 * @param waiter Die neuen Daten, mit denen die Daten mit der waiterID aktualisiert werden sollen.
	 */
	void updateWaiter(int waiterID, Waiter waiter);

	/**
	 * Aktualisiert einen Login-Daten-Satz in der Datenbank mit den Daten eines neuen Login-Daten-Satzes.
	 * @param logindata Die neuen Login-Daten, mit denen die alten Daten ersetzt werden sollen.
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
	 * Setzt den Eintrag der Bedienung auf unemployed, also employed = false.
	 * @param waiterID Die ID der zu kündigenden Bedienung.
	 */
	void deleteWaiter(int waiterID);

	/**
	 * Löscht einen Login-Daten-Satz aus der Datenbank.
	 * @param waiterID Die ID der Bedienung, dessen Login-Daten-Satz aus der Datenbank gelöscht werden soll.
	 */
	void deleteLogindata(int waiterID);
	//endregion

	//region Drucken einer Order
    /**
     * Ausdrucken einer Bestellung in Abhängigkeit von einer ID.
	 * Hierbei handelt es sich um einen Kundenbeleg.
     * @param orderID ID der auszudruckenden Order.
     */
    void printOrderById(int orderID);

	/**
	 * Ausdrucken eines Kundenbeleges in Abhängigkeit von einer ID.
	 * @param orderID ID der auszudruckenden Order.
	 */
	void printReceipt(int orderID);

	/**
	 * Druckt eine Order für die Küche aus mit den neu hinzugefügten orderedItems.
	 * @param orderID ID der auszudruckenden Order.
	 * @param orderedItems Die neu hinzugefügten Artikel, die in der Küche zubereitet werden sollen.
	 */
	void printOrder(int orderID, ArrayList<OrderedItem> orderedItems);

	/**
	 * Druckt einen Login-Daten-Satz aus.
	 * @param loginname Der Login-Name.
	 * @param password Das Passwort in Klartext!
	 * @param waiter Die zum Login-Daten-Satz gehörende Bedienung.
	 */
	void printLogindata(String loginname, String password, Waiter waiter);

	/**
	 * Druckt einen gelösten Datenkonflikt aus. Ein Datenkonflikt tritt im GUI auf, wenn der Warenbestand eines
	 * Artikels negativ wird. Dann erscheint ein Dialog, in dem der Anwender den Datenkonflikt lösen muss. Wenn dies
	 * abgeschlossen wurde, wird ein Ausdruck getätigt, auf dem die zu informierenden Tische mit den betroffenen
	 * und nicht mehr verfügbaren Artikeln vermerkt sind. So können alle Kunden informiert werden.
	 * @param orderedItems Alle vom Datenkonflikt betroffenen bestellten Artikel.
	 */
	void printDataConflict(ArrayList<OrderedItem> orderedItems);
	//endregion


}
