package dhbw.sa.kassensystem_rest.database.printer;

class PrintableOrderedItem
{
	private final String name;
	private final double price;
	private final String comment;

	PrintableOrderedItem(String name, double price, String comment)
	{
		this.name = name;
		this.price = price;
		this.comment = comment;
	}

	PrintableOrderedItem(String name, String comment)
	{
		this.name = name;
		this.comment = comment;
		this.price = 0;
	}

	public String getName()
	{
		return name;
	}

	public double getPrice()
	{
		return price;
	}

	public String getComment()
	{
		return comment;
	}
}
