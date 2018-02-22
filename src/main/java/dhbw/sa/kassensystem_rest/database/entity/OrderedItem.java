package dhbw.sa.kassensystem_rest.database.entity;

public class OrderedItem {
    private int orderedItemID;
    private int orderID;
    private int itemID;
    private boolean itemPaid;

    public OrderedItem(int orderedItemID, int orderID, int itemID, boolean itemPaid) {
        this.orderedItemID = orderedItemID;
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemPaid = itemPaid;
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

    public void itemIsPaid() {
        this.itemPaid = true;
    }

}
