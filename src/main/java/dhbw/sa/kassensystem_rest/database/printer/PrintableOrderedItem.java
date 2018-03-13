package dhbw.sa.kassensystem_rest.database.printer;

public class PrintableOrderedItem
{
	private String name;
	private double price;

	PrintableOrderedItem(String name, double price)
	{
		this.name = name;
		this.price = price;
	}

	PrintableOrderedItem(String name)
	{
		this.name = name;
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
}
