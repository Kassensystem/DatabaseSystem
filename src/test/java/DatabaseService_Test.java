
import dhbw.sa.kassensystem_database.database.DatabaseService;
import dhbw.sa.kassensystem_database.database.entity.Item;
import dhbw.sa.kassensystem_database.database.entity.Order;
import dhbw.sa.kassensystem_database.database.entity.Table;
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

        for(int i = 0; i < 20; i++){
            dbs.addOrder(dummyOrder());
        }
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
    }

    //region Test der Get-Methoden
    private static ArrayList<Order> getOrders() {
        ArrayList<Order> allOrders = dbs.getAllOrders();

        System.out.println("----------------All-Orders-Test----------------");
        for(Order o: allOrders) {
            System.out.println(o.getOrderID() + "\t" + o.getItems()
                    + "\t" + o.getPrice() + "\t" + o.getDate()
                    + "\t" + o.getTable() + "\t" + o.isPaid());
        }
        return allOrders;
    }
    private static ArrayList<Item> getItems() {
        ArrayList<Item> allItems = dbs.getAllItems();

        System.out.println("-----------------All-Items-Test-----------------");
        for(Item i: allItems) {
            System.out.println(i.getItemID() + "\t" + i.getName() + "\t"
                            + i.getRetailprice() + "\t" + i.getQuantity() + "\t"
                            + i.isAvailable());
        }
        return allItems;
    }
    private static ArrayList<Table> getTables() {
        ArrayList<Table> allTables = dbs.getAllTables();

        System.out.println("-------------All-Tables-Test---------------");
        for(Table t: allTables) {
            System.out.println(t.getTableID() + "\t" + t.getName());
        }
        return allTables;
    }
    //endregion

    //region Test der Add-Methoden
    private static void addOrder(Order order) {
        dbs.addOrder(order);
    }
    private static void addItem(Item item) {
        dbs.addItem(item);
    }
    private static void addTable(Table table) {
        dbs.addTable(table);
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
