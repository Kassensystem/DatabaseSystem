package dhbw.sa.restApi.client;

import dhbw.sa.database.Item;
import dhbw.sa.database.Order;
import dhbw.sa.database.Table;

import java.util.ArrayList;

/**
 * Exception wird durch custom Exceptions ersetzt.
 * Fehlertext wird von dieser Exception mitgeliefert.
 */

public interface RestApiClient_Interface {

    /*******************GET*******************/

    ArrayList<Item> getAllItems() throws Exception;

    ArrayList<Table> getAllTables() throws Exception;

    ArrayList<Order> getAllOrders() throws Exception;

    /******************POST*******************/

    void createOrder(Order order) throws Exception;

    void updateOrder(Order order) throws Exception;

}
