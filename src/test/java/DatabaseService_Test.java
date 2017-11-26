
import dhbw.sa.kassensystem_rest.database.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.Item;
import dhbw.sa.kassensystem_rest.database.entity.Itemdelivery;
import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.database.entity.Table;
import org.joda.time.DateTime;

import java.util.ArrayList;


/** Testfälle für die Klasse DatabaseService
 *  Test der Get-Methoden: Es werden alle in der Datenbank befindlichen Daten der Tabelle ausgegeben
 *  Test der Add-Methoden: Mit generischen dummyObjekten wird das Befüllen der Datenbank getestet. Anschließendes manuelles Löschen notwendig.
 *  Test der Update-Methoden:
 */


public class DatabaseService_Test {
    private static DatabaseService dbs = new DatabaseService();
    private static int dummyCounter = 1;
    private static int updateNumber = 999;

    public static void main(String[] args) {



        /*
        for(int i = 0; i < 20; i++){
            dbs.addOrder(dummyOrder());
        }*/

        //Item newItem = new Item("Apfel", 0.75, 55, true);
        //dbs.addItem(newItem);

        //Itemdelivery itemdelivery = new Itemdelivery(5, 20);
        //dbs.addItemdelivery(itemdelivery);
        //System.out.println(dbs.getItemQuantity(5));

        /*
        //Add-Test
        System.out.println("---------------Adding Dummy Entries---------------");
        addOrder(dummyOrder());
        addItem(dummyItem());
        addTable(dummyTable());
        //Get-Test
        getItems();
        getOrders();
        getTables();


        //Update-Test
        System.out.println("Adding Update-Dummy-Order...");
        addOrder(dummyUpdateOrder());
        System.out.println("---------------Checking Update-Dummy-Order------------------------");
        getOrders();
        System.out.println("Manipulating Update-Dummy-Order...");
        //Order mit TableID 999 (updateNumber) soll aktualisiert werden
        ArrayList<Order> allOrders = dbs.getAllOrders();
        //Ermitteln der Order mit TableID 99
        Order updateOrder = new Order();
        for(Order o: allOrders) {
            if(o.getTable() == updateNumber)
                updateOrder = o;
        }
        updateOrder.setPrice(30001);
        System.out.println("Updating Update-Dummy-Order...");
        updateOrder(updateOrder);
        System.out.println("---------------Checking manipulated Update-Dummy-Order------------------------");
        getOrders();
        */

        getItemdeliveryById(0);

    }

    //region Test der Get-Methoden
    private static ArrayList<Order> getOrders() {
        System.out.println("----------------All-Orders-Test----------------");

        ArrayList<Order> allOrders = dbs.getAllOrders();

        for(Order o: allOrders) {
            logOrder(o);
        }
        return allOrders;
    }
    private static ArrayList<Item> getItems() {
        System.out.println("-----------------All-Items-Test-----------------");

        ArrayList<Item> allItems = dbs.getAllItems();

        for(Item i: allItems) {
            logItem(i);
        }
        return allItems;
    }
    private static ArrayList<Table> getTables() {
        System.out.println("-------------All-Tables-Test---------------");

        ArrayList<Table> allTables = dbs.getAllTables();

        for(Table t: allTables) {
            logTable(t);
        }
        return allTables;
    }
    private static ArrayList<Itemdelivery> getItemdeliveries() {
        System.out.println("-------------All-Itemdeliveries-Test---------------");

        ArrayList<Itemdelivery> allItemdeliveries = dbs.getAllItemdeliveries();

        for(Itemdelivery i: allItemdeliveries) {
            logItemdelivery(i);
        }
        return allItemdeliveries;
    }

    private static Order getOrderById(int orderID) {
        System.out.println("-----------------Order-By-ID-Test-----------------");

        Order o = dbs.getOrderById(orderID);

        logOrder(o);

        return o;
    }
    private static Item getItemById(int itemID) {
        System.out.println("-----------------Item-By-ID-Test-----------------");

        Item i = dbs.getItemById(itemID);

        logItem(i);

        return i;
    }
    private static Table getTableById(int tableID) {
        System.out.println("-----------------Table-By-ID-Test-----------------");

        Table t = dbs.getTableById(tableID);

        logTable(t);

        return t;
    }
    private static Itemdelivery getItemdeliveryById(int itemdeliveryID) {
        System.out.println("-----------------Itemdelivery-By-ID-Test-----------------");

        Itemdelivery i = dbs.getItemdeliveryById(itemdeliveryID);

        logItemdelivery(i);

        return i;
    }
    //endregion

    //region Test der Add-Methoden
    private static void addOrder(Order order) {
        System.out.println("-----------------Add-Order-Test-----------------");

        logOrder(order);

        dbs.addOrder(order);
    }
    private static void addItem(Item item) {
        System.out.println("-----------------Add-Item-Test-----------------");

        logItem(item);

        dbs.addItem(item);
    }
    private static void addTable(Table table) {
        System.out.println("-----------------Add-Table-Test-----------------");

        logTable(table);

        dbs.addTable(table);
    }
    private static void addItemdelivery(Itemdelivery i) {
        System.out.println("-----------------Add-Itemdelivery-Test-----------------");

        logItemdelivery(i);

        dbs.addItemdelivery(i);
    }
    //endregion

    //region Test der Update-Methoden
    private static void updateOrder(Order updateOrder) {
        dbs.updateOrder(updateOrder.getOrderID(), updateOrder);
    }
    private static void updateItem() {

    }
    private static void updateTable() {

    }
    //endregion

    /****Logging****/
    private static void logOrder(Order o) {
        System.out.println(o.getOrderID() + "\t" + o.getItems()
                + "\t" + o.getPrice() + "\t" + o.getDate().toString("dd.MM.yyyy kk:mm:ss")
                + "\t" + o.getTable() + "\t" + o.isPaid());
    }
    private static void logItem(Item i) {
        System.out.println(i.getItemID() + "\t" + i.getName() + "\t"
                + i.getRetailprice() + "\t" + i.getQuantity() + "\t"
                + i.isAvailable());
    }
    private static void logTable(Table t) {
        System.out.println(t.getTableID() + "\t" + t.getName() + "\t" + t.isAvailable());
    }
    private static void logItemdelivery(Itemdelivery i) {
        System.out.println(i.getItemdeliveryID() + "\t" + i.getItemID() + "\t" + i.getQuantity());
    }

    /****Dummys****/
    private static Order dummyOrder() {
        int i = dummyCounter++;
        return new Order(i+";"+i+1+";", 3+i, i*1.5, DateTime.now(), false);
    }
    private static Order dummyUpdateOrder() {
        int i = dummyCounter++;
        return new Order(i+";"+i+1+";", updateNumber, i*1.5, DateTime.now(), false);
    }
    private static Item dummyItem() {
        int i = dummyCounter++;
        return new Item("TestItem", i*1.5, i*3, true);
    }
    private static Table dummyTable() {
        return new Table("TestTable", true);
    }
}
