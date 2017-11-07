package com.sa.restApi.client;

import com.sa.database.Item;
import com.sa.database.Order;
import com.sa.database.Table;

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
