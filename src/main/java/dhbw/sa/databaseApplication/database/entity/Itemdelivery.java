package dhbw.sa.databaseApplication.database.entity;

public class Itemdelivery {
    private int itemdeliveryID;
    private int itemID;
    private int quantity;

    public Itemdelivery(int itemdeliveryID, int itemID, int quantity) {
        this.itemdeliveryID = itemdeliveryID;
        this.itemID = itemID;
        this.quantity = quantity;
    }
    public Itemdelivery(int itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public int getItemdeliveryID() {
        return itemdeliveryID;
    }

    public void setItemdeliveryID(int itemdeliveryID) {
        this.itemdeliveryID = itemdeliveryID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
