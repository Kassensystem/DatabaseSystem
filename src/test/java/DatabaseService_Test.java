
import dhbw.sa.kassensystem_rest.database.databaseservice.DBService_OrderedItem;
import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.*;
import org.joda.time.DateTime;

import java.util.ArrayList;


/** Testfälle für die Klasse DatabaseService
 *  Test der Get-Methoden: Es werden alle in der Datenbank befindlichen Daten der Tabelle ausgegeben
 *  Test der Add-Methoden: Mit generischen dummyObjekten wird das Befüllen der Datenbank getestet. Anschließendes manuelles Löschen notwendig.
 *  Test der Update-Methoden:
 */


public class DatabaseService_Test {
    private static DatabaseService dbs = new DatabaseService();
    private static int dummyCounter = 3;
    private static int updateNumber = 999;

    public static void main(String[] args) {
//		getOrders();
//		getItems();
//		getTables();
//		getItemdeliveries();
//		getOrderedItems();
//		getWaiters();
//		getLogindata();
//
//		getOrderedItemByOrderId(6);
//		getOrdereditemById(2);
//		addOrderedItem(new OrderedItem(10, 16));
//		updateOrderedItem(new OrderedItem(5, 10, 16, true, true));
//		deleteOrderedItem(8);
//		addWaiter(dummyWaiter());
//		addLogindata(dummyLogindata());

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
    private static ArrayList<OrderedItem> getOrderedItems() {
		System.out.println("-------------All-OrderedItems-Test---------------");

		ArrayList<OrderedItem> allOrderedItems = dbs.getAllOrderedItems();

		for(OrderedItem o: allOrderedItems) {
			logOrderedItem(o);
		}
		return allOrderedItems;
	}
	private static ArrayList<OrderedItem> getOrderedItemByOrderId(int orderID) {
		System.out.println("-------------OrderedItem-By-OrderID-Test---------------");

		ArrayList<OrderedItem> orderedItems = dbs.getOrderedItemsByOrderId(orderID);

		for(OrderedItem o: orderedItems) {
			logOrderedItem(o);
		}
		return orderedItems;
	}
	private static ArrayList<Waiter> getWaiters() {
		System.out.println("----------------All-Waiters-Test----------------");

		ArrayList<Waiter> allWaiters = dbs.getAllWaiters();

		for(Waiter w: allWaiters) {
			logWaiter(w);
		}
		return allWaiters;
	}
	private static ArrayList<Logindata> getLogindata() {
		System.out.println("----------------All-Logindata-Test----------------");

		ArrayList<Logindata> allLogindata = dbs.getAllLogindata();

		for(Logindata l: allLogindata) {
			logLogindata(l);
		}
		return allLogindata;
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
 	private static OrderedItem getOrdereditemById(int orderedItemID) {
		System.out.println("-----------------OrderedItem-By-ID-Test-----------------");

		OrderedItem o = dbs.getOrderedItemById(orderedItemID);

		logOrderedItem(o);

		return o;
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
    private static void addOrderedItem(OrderedItem o) {
		System.out.println("-----------------Add-OrderedItem-Test-----------------");

		logOrderedItem(o);

		dbs.addOrderedItem(o);
	}
	private static void addWaiter(Waiter w) {
		System.out.println("-----------Add-Waiter-Test---------------");

		logWaiter(w);

		dbs.addWaiter(w);
	}
	private static void addLogindata(Logindata l) {
		System.out.println("-----------Add-Login-Data-Test----------------");

		logLogindata(l);

		dbs.addLogindata(l);
	}
    //endregion

    //region Test der Update-Methoden
    private static void updateOrder(Order updateOrder) {
        System.out.println("-----------------Update-Order-Test-----------------");

        logOrder(updateOrder);

        dbs.updateOrder(updateOrder.getOrderID(), updateOrder);
    }
    private static void updateItem(Item updateItem) {
        System.out.println("-----------------Update-Item-Test-----------------");

        logItem(updateItem);

        dbs.updateItem(updateItem.getItemID(), updateItem);
    }
    private static void updateTable(Table updateTable) {
        System.out.println("-----------------Update-Table-Test-----------------");

        logTable(updateTable);

        dbs.updateTable(updateTable.getTableID(), updateTable);
    }
    private static void updateOrderedItem(OrderedItem updateOrderedItem) {
		System.out.println("-----------------Update-OrderedItem-Test-----------------");

		logOrderedItem(updateOrderedItem);

		dbs.updateOrderedItem(updateOrderedItem.getOrderedItemID(), updateOrderedItem);
	}
    //endregion

    //region Test der Delete-Methoden
    private static void deleteOrder(int orderID) {
        System.out.println("-----------------Delete-Order-Test-----------------");

        dbs.deleteOrder(orderID);
    }
    private static void deleteItemdelivery(int itemdeliveryID) {
        System.out.println("-----------------Delete-Itemdelivery-Test-----------------");

        dbs.deleteItemdelivery(itemdeliveryID);
    }
    private static void deleteOrderedItem(int orderedItemID) {
		System.out.println("-----------------Delete-OrderedItem-Test-----------------");

		dbs.deleteOrderedItem(orderedItemID);
	}
    //endregion

    /****Logging****/
    private static void logOrder(Order o) {
        System.out.println(o.getOrderID() + "\t"
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
    private static void logOrderedItem(OrderedItem o) {
		System.out.println(o.getOrderedItemID() + "\t" + o.getOrderID() + "\t" + o.getItemID() + "\t" + o.isItemPaid() +
		"\t" + o.isItemProduced());
	}
	private static void logWaiter(Waiter w) {
		System.out.println(w.getWaiterID() + "\t" + w.getPrename() + "\t" + w.getLastname());
	}
	private static void logLogindata(Logindata l) {
		System.out.println(l.getWaiterID() + "\t" + l.getLoginname() + "\t" + l.getPasswordHash());
	}

    /****Dummys****/
    private static Order dummyOrder() {
        int i = dummyCounter++;
        return new Order( 3+i, i*1.5, DateTime.now(), false, 0);
    }
    private static Order dummyUpdateOrder() {
        int i = dummyCounter++;
        return new Order(updateNumber, i*1.5, DateTime.now(), false, 0);
    }
    private static Item dummyItem() {
        int i = dummyCounter++;
        return new Item("TestItem", i*1.5, i*3, true);
    }
    private static Table dummyTable() {
        return new Table("TestTable", 9, true);
    }
    private static Waiter dummyWaiter() {
    	int i = dummyCounter++;
    	return new Waiter("name" + i, "prename" + i);
	}
	private static Logindata dummyLogindata() {
    	int i = dummyCounter++;
    	return new Logindata(i, "loginname" + i, Logindata.encryptPassword("passwort" + i));
	}
}
