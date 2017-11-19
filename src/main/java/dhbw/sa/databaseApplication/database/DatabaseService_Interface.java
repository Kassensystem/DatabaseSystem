package dhbw.sa.databaseApplication.database;

import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Itemdelivery;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;
import dhbw.sa.databaseApplication.exceptions.MySQLServerConnectionException;

import java.util.ArrayList;

/**
 * Der DatabaseService stellt die Schnittstelle zu einer MySQL-Datenbank dar.
 *
 *  @throws MySQLServerConnectionException, wenn zur Laufzeit ein Fehler
 *  bei der Verbindung mit der MySQL-Datenbank auftritt.  Die genaue Fehler-
 *  meldung ist der Message zu entnehmen.
 *  @throws dhbw.sa.databaseApplication.exceptions.DataException, wenn ein
 *  unvollstaendiger Datensatz (bis auf die ID fehlt ein weiteres Attribut)
 *  in die Datenbank gespeichert werden soll.
 *
 * @author Marvin Mai
 */

public interface DatabaseService_Interface {
    /**
     * Stellt eine Verbindung zur MySQL-Datenbank her.
     * @throws IllegalStateException wenn die Datenbank nicht erreichbar ist.
     */
    void connect();

    /**
     * Beendet eine bestehende Verbindung mit einem MySQL-Server.
     */
    void disconnect();

    //Getting Table-Data from the database
    /**
     * Fragt die Artikel der Datenbank ab.
     * @return Artikel der Datenbank.
     */
    ArrayList<Item> getAllItems();

    /**
     * Fragt die Tische der Datenbank ab.
     * @return Tische der Datenbank.
     */
    ArrayList<Table> getAllTables();

    /**
     * Fragt die Bestellungen der Datenbank ab.
     * @return Bestellungen der Datenbank.
     */
    ArrayList<Order> getAllOrders();

    /**
     * Fragt die Wareneingaenge der Datenbank ab.
     * @return Wareneingaenge der Datenbank.
     */
    ArrayList<Itemdelivery> getAllItemdeliveries();

    //Adding data to the database

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
    void addOrder(Order order);

    /**
     * Fuegt der Datenbank einen neuen Wareneingang hinzu.
     * @param itemdelivery neuer Wareneingang.
     */
    void addItemdelivery(Itemdelivery itemdelivery);

    //Updating data in database

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

    //Deleting data from the database

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

}
