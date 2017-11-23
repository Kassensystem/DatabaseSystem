package dhbw.sa.kassensystem_rest.restApi.client;

import dhbw.sa.kassensystem_database.database.entity.Item;
import dhbw.sa.kassensystem_database.database.entity.Order;
import dhbw.sa.kassensystem_database.database.entity.Table;

import java.util.ArrayList;

/**
 * Schnittstelle der Database-Application nach außen. Die Android-Application verwendet diese, um Daten der Datenbank
 * anzufordern oder zu manipulieren. So können neue Bestellungen erstellt und upgedatet werden; Artikel, Bestellungen
 * und Tisch können angefordert werden.
 *
 * @author Marvin Mai
 */
interface RestApiClient_Interface {

    /*GET*/

    /**
     * Fordert alle Artikel der Datenbank vom RestApiController an.
     * @return Liste mit allen Artikeln {@link Item} der Datenbank.
     * @throws Exception
     */
    ArrayList<Item> getAllItems() throws Exception;

    /**
     * Fordert alle Tische der Datenbank vom RestApiController an.
     * @return Liste mit allen Tischen {@link Table} der Datenbank.
     * @throws Exception
     */
    ArrayList<Table> getAllTables() throws Exception;

    /**
     * Fordert alle Bestellungen der Datenbank vom RestApiController an.
     * @return Liste mit allen Bestellungen {@link Order} der Datenbank.
     * @throws Exception
     */
    ArrayList<Order> getAllOrders() throws Exception;

    /*POST*/

    /**
     * Überträgt eine neue Bestellung an den RestApiController im Pfad .../order,
     * wo diese in die Datenbank gespeichert wird.
     * @param order Zu übertragende Order.
     * @throws Exception Fehlermeldung mit Grund als Text.
     */
    void createOrder(Order order) throws Exception;

    /**
     * Überträgt eine bereits existierende Bestellung, die bearbeitet wurde, an den RestApiController im Pfad .../order,
     * wo diese in der Datenbank aktualisiert wird.
     * @param orderID ID der Bestellung, die aktualisiert werden soll.
     * @param order Bestellung, die an Stelle der Bestellung mit der orderID gespeichert werden soll.
     * @throws Exception Fehlermeldung mit Grund als Text.
     */
    void updateOrder(int orderID, Order order) throws Exception;

}
