package dhbw.sa.databaseApplication.printer;

import dhbw.sa.databaseApplication.database.Gastronomy;
import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Service zum Ausdrucken einer Bestellung, die in einer PrintableOrder gespeichert wurde.
 *
 * Die folgenden Hardwarekomponenten sind für diesen Service erforderlich:
 * Modell des Druckers:
 * Epson TM-T88V MODEL M244A
 * Treiber muss im OS installiert sein
 * Download des Treibers:
 * https://download.epson-biz.com/modules/pos/index.php?page=single_soft&cid=5131&pcat=3&scat=31
 *
 * @author Marvin Mai
 */

public class PrinterService {

    //TODO Ergänzen eines Datensatzes in der Datenbank, um Druckernamen einstellbar zu machen
    /** @param printerName Name des Druckers, wie er im Betriebssystem angezeigt wird.*/
    private final String printerName = "EPSON TM-T88V Receipt";

    /**
     * Druckt über einen formatierten Text die Bestellung aus.
     * @param order die zu druckende Bestellung.
     * @param allItems alle Items aus der Datenbank, aus denen die erforderlichen Informationen erhalten weren.
     * @param allTables alle Tische aus der Datenbank, aus denen die erforderlichen Informationen erhalten weren.
     * @param kitchenReceipt Indikator, ob es sich um einen Küchen- oder Kundenbeleg handelt.
     */
    public void printOrder(Order order, ArrayList<Item> allItems, ArrayList<Table> allTables, boolean kitchenReceipt) {
        PrintableOrder printableOrder = getPrintableOrder(order, allItems, allTables);

        String formattedOrderText = getFormattedOrder(printableOrder, kitchenReceipt);

        printString(formattedOrderText);
    }

    /**
     * Druckt über einen printJob einen String, in dem die Bestellung formatiert wurde.
     * @param text formatierter Text der Bestellung.
     */
    private void printString(String text) {
        // find the printService of name printerName
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
        PrintService service = findPrintService(printerName, printService);

        assert service != null;
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
            e.printStackTrace();
        }
    }

    /**
     * Formatiert eine Bestellung in eine {@link PrintableOrder}
     * @param order zu druckende Bestellung.
     * @param allItems alle Items der Datenbank.
     * @param allTables alle Tische der Datenbank.
     * @return eine {@link PrintableOrder} mit den Daten der Bestellung.
     */
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

    /**
     * Formatiert eine {@link PrintableOrder} in einen Text, der entsprechend dem Layout des Belegs formattiert wurde.
     * @param printableOrder Daten der Order in ausdruckbarem Format.
     * @param kitchenReceipt ändert den Beleg zu einem Küchen- oder Kundenbeleg
     * @return die Order formatiert in einen Text, der als Beleg ausgedruckt werden kann.
     */
    private String getFormattedOrder(PrintableOrder printableOrder, boolean kitchenReceipt) {
        StringBuilder formattedOrderText = new StringBuilder("");

        if(kitchenReceipt)
            formattedOrderText.append("----------Küchenbeleg-------------\n\n");
        else
            formattedOrderText.append("----------Kundenbeleg-------------\n\n");

        formattedOrderText.append(Gastronomy.getName() + "\n"
                        + Gastronomy.getAdress() + "\n"
                        + Gastronomy.getTelephonenumber() + "\n"
                        + "\n"
                        + "Ihre Bestellung:\n");

        DecimalFormat df = new DecimalFormat("#0.00");
        for(Item i: printableOrder.getItems()) {
            double price = i.getRetailprice();

            formattedOrderText
                    .append(i.getName())
                    .append("\t\t")
                    .append(df.format(price))
                    .append(" EUR\n");
        }

        double mwst = Math.round(printableOrder.getPrice()*0.199 * 100d) / 100d;

        formattedOrderText
                .append("________________________\n" + "Summe\t\t")
                .append(df.format(printableOrder.getPrice())).append(" EUR\n")
                .append("inkl. MWST 19%\t").append(df.format(mwst)).append(" EUR\n").append("\n").append("Sie saßen an Tisch ")
                .append(printableOrder.getTableName()).append(".\n").append("Vielen Dank für Ihren Besuch!\n")
                .append(printableOrder.getDate()).append("\n\n");

        return formattedOrderText.toString();
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
