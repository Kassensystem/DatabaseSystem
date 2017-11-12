package dhbw.sa.databaseApplication.database;

import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Itemdelivery;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;

import java.util.ArrayList;

public interface DatabaseService_Interface {

    void connect();
    void disconnect();

    //region Getting Table-Data from the database
    ArrayList<Item> getAllItems();
    ArrayList<Table> getAllTables();
    ArrayList<Order> getAllOrders();
    ArrayList<Itemdelivery> getAllItemdeliveries();
    //endregion

    //region Adding data to the database
    void addItem(Item item);
    void addTable(Table table);
    void addOrder(Order order);
    void addItemdelivery(Itemdelivery itemdelivery);
    //endregion

    //region Updating data in database
    void updateItem(int itemID, Item item);
    void updateTable(int tableID, Table table);
    void updateOrder(int orderID, Order order);
    //endregion

    //region Deleting data from the database
    void deleteItem(int itemID);
    void deleteTable(int tableID);
    void deleteOrder(int orderID);
    //endregion

}
