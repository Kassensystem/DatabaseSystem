package dhbw.sa.printer;

import dhbw.sa.database.Gastronomy;
import dhbw.sa.database.Item;
import dhbw.sa.database.Order;
import dhbw.sa.database.Table;
import org.joda.time.DateTime;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**Service zum Ausdrucken einer Bestellung
 * Modell des Druckers:
 * Epson TM-T88V MODEL M244A
 * Treiber muss im OS installiert sein
 * Download des Treibers: https://download.epson-biz.com/modules/pos/index.php?page=single_soft&cid=5131&pcat=3&scat=31
 */

public class PrinterService {

    private final String printerName = "EPSON TM-T88V Receipt";

    public void printOrder(Order order, ArrayList<Item> allItems, ArrayList<Table> allTables) {
        PrintableOrder printableOrder = getPrintableOrder(order, allItems, allTables);

        String formattedOrderText = getFormattedOrder(printableOrder);

        printString(formattedOrderText);
    }

    private void printString(String text) {
        // find the printService of name printerName
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        DocPrintJob job = service.createPrintJob();

        try {

            // important for umlaut chars
            byte[] textBytes = text.getBytes("CP437");
            byte[] commandBytes = {29, 86, 65, 0, 0};

            byte[] bytes = new byte[textBytes.length + commandBytes.length];

            System.arraycopy(textBytes, 0, bytes, 0, textBytes.length);
            System.arraycopy(commandBytes, 0, bytes, textBytes.length, commandBytes.length);

            Doc doc = new SimpleDoc(bytes, flavor, null);


            job.print(doc, null);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private PrintableOrder getPrintableOrder(Order order, ArrayList<Item> allItems, ArrayList<Table> allTables) {
        PrintableOrder printableOrder = new PrintableOrder();

        //Items
        ArrayList<Item> orderItems = order.getItems(allItems);
        //Table-Name
        String tableName = order.getTable(allTables).getName();
        //Date
        String dateString = order.getDate().toString("dd.MM.yyyy kk:mm:ss");

        //printableOrder zusammenstellen
        printableOrder.setOrderID(order.getOrderID());
        printableOrder.setItems(orderItems);
        printableOrder.setTableName(tableName);
        printableOrder.setPrice(order.getPrice());
        printableOrder.setDate(dateString);

        return printableOrder;
    }

    private String getFormattedOrder(PrintableOrder printableOrder) {
        String formattedOrderText =
                Gastronomy.getName()  + "\n"
                        + Gastronomy.getAdress() + "\n"
                        + Gastronomy.getTelephonenumber() + "\n"
                        + "\n"
                        + "Ihre Bestellung mit der ID " + printableOrder.getOrderID() + ":\n";

        DecimalFormat df = new DecimalFormat("#0.00");
        for(Item i: printableOrder.getItems()) {
            double price = i.getRetailprice();

            formattedOrderText += i.getName() + "\t\t" + df.format(price) + " EUR\n";
        }

        double mwst = Math.round(printableOrder.getPrice()*0.199 * 100d) / 100d;

        formattedOrderText +=
                "________________________\n"
                + "Summe\t\t" + df.format(printableOrder.getPrice()) + " EUR\n"
                + "MWST 19%\t" + df.format(mwst) + " EUR\n"
                + "\n"
                + "Sie saßen an Tisch " + printableOrder.getTableName() + ".\n"
                + "Vielen Dank für Ihren Besuch!\n" + printableOrder.getDate() + "\n\n";

        return formattedOrderText;
    }

    private PrintService findPrintService(String printerName, PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }

}
