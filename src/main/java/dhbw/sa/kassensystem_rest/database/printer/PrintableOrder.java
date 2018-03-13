package dhbw.sa.kassensystem_rest.database.printer;

import java.util.ArrayList;

public class PrintableOrder
{
	private String date;
	private String tableName;
	private ArrayList<PrintableOrderedItem> orderedItems;

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
