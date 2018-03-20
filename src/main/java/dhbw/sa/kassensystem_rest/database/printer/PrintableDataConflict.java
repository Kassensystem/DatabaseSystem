package dhbw.sa.kassensystem_rest.database.printer;

public class PrintableDataConflict
{
	private String tableName;
	private String itemName;

	public PrintableDataConflict(String tableName, String itemName)
	{
		this.tableName = tableName;
		this.itemName = itemName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}
}
