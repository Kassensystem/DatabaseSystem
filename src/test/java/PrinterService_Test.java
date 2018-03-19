

import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.OrderedItem;
import dhbw.sa.kassensystem_rest.database.printer.PrinterService;

import java.util.ArrayList;

public class PrinterService_Test {
    private static DatabaseService dbs = new DatabaseService();

    public static void main(String [] args) {

        //PrinterTest
        PrinterService printerService = new PrinterService();
        ArrayList<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(new OrderedItem(38, 3, ""));
		orderedItems.add(new OrderedItem(38, 4, ""));
		orderedItems.add(new OrderedItem(38, 5, ""));
        printerService.printOrder(38, orderedItems);
    }
}
