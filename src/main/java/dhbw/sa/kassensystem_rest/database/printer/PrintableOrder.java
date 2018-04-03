package dhbw.sa.kassensystem_rest.database.printer;

import java.util.ArrayList;

class PrintableOrder
{
	private final String date;
	private final String tableName;
	private final ArrayList<PrintableOrderedItem> orderedItems;

	PrintableOrder(String date, String tableName, ArrayList<PrintableOrderedItem> orderedItems)
	{
		this.date = date;
		this.tableName = tableName;
		this.orderedItems = orderedItems;
	}

	public String getDate()
	{
		return date;
	}

	public String getTableName()
	{
		return tableName;
	}

	public ArrayList<PrintableOrderedItem> getOrderedItems()
	{
		return orderedItems;
	}
}
