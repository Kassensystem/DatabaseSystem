package dhbw.sa.kassensystem_rest.database.entity;

public class OrderedItem {
    private int orderedItemID;
    private int orderID;
    private int itemID;
    private boolean itemPaid;

    public int getOrderedItemID() {
        return orderedItemID;
    }

    public OrderedItem(int orderID, int itemID, boolean itemPaid) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemPaid = itemPaid;
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
