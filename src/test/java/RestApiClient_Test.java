import dhbw.sa.database.Item;
import dhbw.sa.database.Order;
import dhbw.sa.database.Table;
import dhbw.sa.exceptions.ControllerConnectionException;
import dhbw.sa.exceptions.MySQLException;
import dhbw.sa.exceptions.NoContentException;
import dhbw.sa.restApi.client.RestApiClient;
import org.joda.time.DateTime;

import java.util.ArrayList;


public class RestApiClient_Test {
    private static RestApiClient restApiClient = new RestApiClient();

    public static void main(String[] args) {

        //updateOrder(8, testOrder());
        createOrder();

    }

    static void test() {
        try {
            System.out.println(RestApiClient.test());
        } catch (ControllerConnectionException e) {
            e.printStackTrace();
        } catch (MySQLException e) {
            e.printStackTrace();
        } catch (NoContentException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Order> getAllOrders() {
        System.out.println("---------Get all orders---------");
        try {
            ArrayList<Order> allOrders = restApiClient.getAllOrders();
            for (Order o: allOrders) {
                System.out.println(o.getOrderID() + "\t" + o.getItems() + "\t" + o.getTable() + "\t" + o.getPrice());
            }
            return allOrders;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static ArrayList<Item> getAllItems() {
        System.out.println("---------Get all items---------");
        try {
            ArrayList<Item> allItems = restApiClient.getAllItems();
            for(Item i: allItems) {
                System.out.println(i.getItemID() + " \t  " + i.getName() + " \t " + i.getRetailprice() + " \t " + i.getQuantity());
            }
            return allItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        return null;
    }

    static ArrayList<Table> getAllTables() {
        System.out.println("---------Get all tables---------");
        try {
            ArrayList<Table> allTables = restApiClient.getAllTables();
            for(Table t: allTables) {
                System.out.println(t.getTableID() + " \t " + t.getName());
            }
            System.out.println();
            return allTables;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void createOrder() {
        restApiClient.createOrder(testOrder());
    }

    static void updateOrder(int orderID, Order testOrder) {
        restApiClient.updateOrder(orderID, testOrder);
    }

    private static Order testOrder() {
        //Items zusammenstellen
        ArrayList<Item> orderItems = new ArrayList<>();
        double price = 0;
        for(Item i: restApiClient.getAllItems()) {
            int id = i.getItemID();
            if(id == 4 || id ==5) {
                orderItems.add(i);
                price += i.getRetailprice();
            }
        }
        String itemIDs = Order.joinIDsIntoString(orderItems);
        //Table festlegen
        Table table = new Table();
        for(Table t: restApiClient.getAllTables()) {
            if(t.getTableID() == 5)
                table = t;
        }

        return new Order(itemIDs, table.getTableID(), price, DateTime.now(), false);
    }
}
