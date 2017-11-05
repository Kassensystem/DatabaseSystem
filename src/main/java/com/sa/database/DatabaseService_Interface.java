package com.sa.database;

import java.util.ArrayList;

public interface DatabaseService_Interface {

    void connect();
    void disconnect();

    //region Getting Table-Data from the database
    ArrayList<Item> getAllItems();
    ArrayList<Table> getAllTables();
    ArrayList<Order> getAllOrders();
    //endregion

    //region Adding data to the database
    void addItem(Item item);
    void addTable(Table table);
    void addOrder(Order order);
    //endregion

    //region Updating data in database
    void updateItem(int itemID, Item item);
    void updateTable(int tableID, Table table);
    void updateOrder(int orderID, Order order);
    //endregion

    //region Deleting data from the database

    // TODO Klären: Welche Daten drüfen gelöscht werden, ohne dass in bisherigen Orders Daten fehlen?
    // TODO Mögliche Lösung: Items und Tables als gelöscht markieren, Daten sind weiterhin zugänglich,
    // TODO aber können durch eine vorherige Abfrage der Lösch-Markierung als gelöscht erkannt werden.

    //endregion

}
