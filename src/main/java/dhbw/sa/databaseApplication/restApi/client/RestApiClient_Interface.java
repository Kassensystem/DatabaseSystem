package dhbw.sa.databaseApplication.restApi.client;

import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;

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

    void updateOrder(int orderID, Order order) throws Exception;

}
