package dhbw.sa.kassensystem_rest.database.entity;

public class OrderedItem {
    private int orderedItemID;
    private int orderID;
    private int itemID;
    private boolean itemPaid;
    private boolean itemProduced;

	public OrderedItem(int orderedItemID, int orderID, int itemID, boolean itemPaid, boolean itemProduced) {
        this.orderedItemID = orderedItemID;
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemPaid = itemPaid;
        this.itemProduced = itemProduced;
    }

    public int getOrderedItemID() {
        return orderedItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getItemID() {
        return itemID;
    }

	public boolean isItemPaid() {
		return itemPaid;
	}

	public boolean isItemProduced()
	{
		return itemProduced;
	}

    public void itemIsPaid() {
        this.itemPaid = true;
    }

	public void setItemProduced(boolean itemProduced)
	{
		this.itemProduced = itemProduced;
	}

}
