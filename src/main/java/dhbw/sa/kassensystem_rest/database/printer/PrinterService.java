package dhbw.sa.kassensystem_rest.database.printer;


import dhbw.sa.kassensystem_rest.database.Gastronomy;
import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.*;
import org.joda.time.DateTime;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Service zum Ausdrucken einer Bestellung, die in einer PrintableReceipt gespeichert wurde.
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

    /** @param printerName Name des Druckers, wie er im Betriebssystem angezeigt wird.*/
    private final String printerName = "EPSON TM-T88V Receipt";
    private DatabaseService databaseService = new DatabaseService();

    // Interface zum Ausdrucken einer Order oder Receipt
    /**
     * Druckt über einen formatierten Text die Bestellung für die Küche aus.
	 * @param orderID ID der zu druckenden Bestellung.
	 * @param orderedItems Neu bestellte Artikel.
	 */
    public void printOrder(int orderID, ArrayList<OrderedItem> orderedItems)
	{
        PrintableOrder printableOrder = getPrintableOrder(orderID, orderedItems);

        String formattedOrderText = getFormattedOrder(printableOrder);

        printString(formattedOrderText);

        databaseService.disconnect();
    }

	/**
	 * Druckt einen Kundenbeleg aus.
	 * @param orderID Die ID der zu druckenden Bestellung.
	 */
    public void printReceipt(int orderID)
	{
		PrintableReceipt printableReceipt = getPrintableReceipt(orderID);

		String formattedReceiptText = getFormattedReceipt(printableReceipt);

		printString(formattedReceiptText);

		databaseService.disconnect();
	}

	public void printLogindata(String loginname, String password, Waiter waiter)
	{
		String formattedLogindata = getFormattedLogindata(loginname, password, waiter);

		printString(formattedLogindata);
	}

	public void printDataConflict(ArrayList<OrderedItem> orderedItems)
	{
		String formattedText = getFormattedDataConflicts(getPrintableDataConflicts(orderedItems));

		printString(formattedText);
	}

	// Funktionen zum Ermitteln aller zum Drucken benötigten Daten
	/**
	 * Sammelt Daten für eine {@Link printableOrder}, um einen Küchenbeleg ausdrucken zu können.
	 * @param orderID Die ID der Order, für die neue bestellte Artikel zur Zubereitung in der Küche
	 *                ausgedruckt werden sollen.
	 * @param orderedItems Die neue hinzugefügten bestellten Artikel.
	 * @return Eine {@Link PrintableOrder}, die alle auszudruckenden Informationen enthält.
	 */
	private PrintableOrder getPrintableOrder(int orderID, ArrayList<OrderedItem> orderedItems)
	{
		Order order = databaseService.getOrderById(orderID);

		// Date
		String dateString = order.getDate().toString("dd.MM.yyyy kk:mm:ss");
		// Table-Name
		String tableName = databaseService.getTableById(order.getTable()).getName();
		// Printable Order
		ArrayList<PrintableOrderedItem> printableOrderedItems = new ArrayList<>();
		for(OrderedItem o: orderedItems)
		{
			// Alle noch nicht in der DB existierenden OrderedItems den auszudruckenden OrderedItems hinzufügen.
			// Nur diese sollen an die Küche ausgedruckt werden.
			if (!databaseService.existsOrderedItemWithID(o.getOrderedItemID()))
			{
				String name = databaseService.getItemById(o.getItemID()).getName();
				printableOrderedItems.add(new PrintableOrderedItem(name, o.getComment()));
			}
		}

		return new PrintableOrder(dateString, tableName, printableOrderedItems);
	}

	/**
	 * Sammelt Daten für eine {@link PrintableReceipt}, um einen Kundenbeleg ausdrucken zu können.
	 * @param orderID Die ID der zu druckenden Bestellung.
	 * @return Eine {@link PrintableReceipt}, die alle auszudruckenden Informationen enthält.
	 */
	private PrintableReceipt getPrintableReceipt(int orderID)
	{
		Order order = databaseService.getOrderById(orderID);

		// Date
		String dateString = order.getDate().toString("dd.MM.yyyy kk:mm:ss");
		// Table-Name
		String tableName = databaseService.getTableById(order.getTable()).getName();
		// PrintableOrderedItems
		ArrayList<PrintableOrderedItem> printableOrderedItems = new ArrayList<>();
		for(OrderedItem o: databaseService.getOrderedItemsByOrderId(order.getOrderID()))
		{
			Item item = databaseService.getItemById(o.getItemID());
			String name = item.getName();
			double price = item.getRetailprice();
			String comment = o.getComment();
			printableOrderedItems.add(new PrintableOrderedItem(name, price, comment));
		}
		// Preis
		double price = databaseService.getOrderPrice(order.getOrderID());

		return new PrintableReceipt(dateString, tableName, printableOrderedItems, price);
	}

	private ArrayList<PrintableDataConflict> getPrintableDataConflicts(ArrayList<OrderedItem> orderedItems)
	{
		ArrayList<PrintableDataConflict> printableDataConflicts = new ArrayList<>();
		String tableName;
		String itemName;
		for(OrderedItem o: orderedItems)
		{
			tableName = databaseService.getTableById(databaseService.getOrderById(o.getOrderID()).getTable()).getName();
			itemName = databaseService.getItemById(o.getItemID()).getName();
			printableDataConflicts.add(new PrintableDataConflict(tableName, itemName));
		}

		return printableDataConflicts;
	}

	// Formatierung der zu Druckenden Daten
    /**
     * Formatiert eine {@link PrintableReceipt} in einen Text, der entsprechend des Layouts des Belegs formattiert wird.
     * @param printableReceipt Daten der Order in ausdruckbarem Format.
     * @return Die Order formatiert in einen Text, der als Beleg ausgedruckt werden kann.
     */
    private String getFormattedReceipt(PrintableReceipt printableReceipt)
	{
        StringBuilder formattedReceiptText = new StringBuilder("");

		formattedReceiptText.append("----------Kundenbeleg-------------\n\n");

        formattedReceiptText.append(Gastronomy.getName() + "\n"
                        + Gastronomy.getAdress() + "\n"
                        + Gastronomy.getTelephonenumber() + "\n"
                        + "\n"
                        + "Ihre Bestellung:\n");

        DecimalFormat df = new DecimalFormat("#0.00");
        for(PrintableOrderedItem o: printableReceipt.getPrintableOrderedItems())
        {
            formattedReceiptText
                    .append(o.getName())
                    .append("\t\t")
                    .append(df.format(o.getPrice()))
                    .append(" EUR\n");

			if(o.getComment() != null)
				formattedReceiptText.append("\t" + o.getComment() + "\n");
        }

        double mwst = Math.round(printableReceipt.getPrice()*0.199 * 100d) / 100d;

        formattedReceiptText
                .append("________________________\n")
				.append("Summe\t\t").append(df.format(printableReceipt.getPrice())).append(" EUR\n")
                .append("inkl. MWST 19%\t").append(df.format(mwst)).append(" EUR\n")
				.append("\n")
				.append("Sie saßen an Tisch ").append(printableReceipt.getTableName()).append(".\n")
				.append("Vielen Dank für Ihren Besuch!\n")
                .append(printableReceipt.getDate()).append("\n\n");

        return formattedReceiptText.toString();
    }

	/**
	 * Formatiert eine {@Link PrintableOrder} in einen formatierten Text, der anschließend ausgedruckt werden kann.
	 * @param printableOrder PrintableOrder, die die zu formatierenden Daten enthält.
	 * @return Einen formatierten Text, der ausgedruckt werden kann.
	 */
	private String getFormattedOrder(PrintableOrder printableOrder)
	{
		StringBuilder formattedOrderText = new StringBuilder("");

		formattedOrderText.append("\t\tKÜCHE\n\n");

		for(PrintableOrderedItem p: printableOrder.getOrderedItems())
		{
			formattedOrderText
					.append(p.getName()).append("\n");
			if(p.getComment() != null)
					formattedOrderText.append("\t" + p.getComment() + "\n");
		}

		formattedOrderText
				.append("\n")
				.append("Tisch " + printableOrder.getTableName() + "\n")
				.append(printableOrder.getDate())
				.append("\n\n");

		return formattedOrderText.toString();
	}

	private String getFormattedLogindata(String loginname, String password, Waiter waiter)
	{
		StringBuilder txt = new StringBuilder("");

		txt.append("Login-Daten\n" + DateTime.now().toString("dd.MM.yyyy kk:mm:ss") + "\n")
			.append("Benutzername:\t" + loginname + "\n")
			.append("Passwort:\t" + password + "\n");

		return txt.toString();
	}

	private String getFormattedDataConflicts(ArrayList<PrintableDataConflict> printableDataConflicts)
	{
		StringBuilder txt = new StringBuilder("");

		txt.append("Die folgenden Tische informieren:\n");

		for(PrintableDataConflict p: printableDataConflicts)
		{
			txt.append(p.getTableName() + "\t" + p.getItemName() + "\n");
		}

		return txt.toString();
	}

    // Druckerfunktionen
	/**
	 * Druckt über einen printJob einen String, in dem die Bestellung formatiert wurde.
	 * @param text formatierter Text der Bestellung.
	 */
	private void printString(String text)
	{
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

    private PrintService findPrintService(String printerName, PrintService[] services)
	{
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }

        return null;
    }

}
