
import dhbw.sa.kassensystem_rest.database.entity.Item;
import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.database.entity.Table;
import dhbw.sa.kassensystem_rest.restApi.client.RestApiClient;
import org.joda.time.DateTime;

import java.util.ArrayList;

@Deprecated
public class RestApiClient_Test {
    private static RestApiClient restApiClient = new RestApiClient();

    public static void main(String[] args) {

        getAllOrders();

    }

    static ArrayList<Order> getAllOrders() {
        System.out.println("---------Get all orders---------");
        try {
            ArrayList<Order> allOrders = restApiClient.getAllOrders();
            for (Order o: allOrders) {
                System.out.println(o.getOrderID() + "\t" + o.getTable() + "\t" + o.getPrice());
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
        try {
            restApiClient.createOrder(testOrder());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    static void updateOrder(int orderID, Order testOrder) {
        // TODO
        try {
            restApiClient.updateOrder(orderID, testOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Order testOrder() {
        //Items zusammenstellen
        ArrayList<Item> orderItems = new ArrayList<>();
        double price = 0;
        try {
            for(Item i: restApiClient.getAllItems()) {
                int id = i.getItemID();
                if(id == 4 || id ==5) {
                    orderItems.add(i);
                    price += i.getRetailprice();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Table festlegen
        Table table = new Table();
        try {
            for(Table t: restApiClient.getAllTables()) {
                if(t.getTableID() == 5)
                    table = t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Order(table.getTableID(), price, DateTime.now(), true);
    }
}
