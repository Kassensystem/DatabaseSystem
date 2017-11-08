import dhbw.sa.database.DatabaseService;
import dhbw.sa.printer.PrinterService;

public class PrinterService_Test {
    private static DatabaseService dbs = new DatabaseService();

    public static void main(String [] args) {

        //PrinterTest
        PrinterService printerService = new PrinterService();
        //printerService.printOrder(dbs.getOrderById(4), dbs.getAllItems(), dbs.getAllTables());
        printerService.printOrder(dbs.getOrderById(1), dbs.getAllItems(), dbs.getAllTables());
        //printerService.printOrder(dbs.getOrderById(3), dbs.getAllItems(), dbs.getAllTables());
    }
}
