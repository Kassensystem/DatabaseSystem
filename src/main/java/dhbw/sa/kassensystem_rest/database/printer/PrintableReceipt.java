package dhbw.sa.kassensystem_rest.database.printer;


import java.util.ArrayList;

/**
 * In einer PrintableReceipt werden die Daten einer Order in primitiven Datentypen gespeichert, um diese anschließend
 * mit dem PrinterService ausdrucken zu können.
 *
 * @author Marvin Mai
 */
class PrintableReceipt
{
	private final String date;
	private final String tableName;
	private final ArrayList<PrintableOrderedItem> orderedItems;
	private final double price;

	public PrintableReceipt(String date, String tableName, ArrayList<PrintableOrderedItem> orderedItems, double price)
	{
		this.date = date;
		this.tableName = tableName;
		this.orderedItems = orderedItems;
		this.price = price;
	}

	public ArrayList<PrintableOrderedItem> getPrintableOrderedItems() {
        return orderedItems;
    }

    public String getTableName() {
        return tableName;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
