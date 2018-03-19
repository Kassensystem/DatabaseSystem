package dhbw.sa.kassensystem_rest.database.printer;

public class PrintableOrderedItem
{
	private String name;
	private double price;
	private String comment;

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
