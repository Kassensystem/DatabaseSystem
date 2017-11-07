package dhbw.sa.database;


public class Item {
    private int itemID;
    private String name;
    private double retailprice;
    private int quantity;
    private boolean available;

    public Item() {
        //default constructor
    }

    Item(int itemID, String name, double retailprice, int quantity, boolean available){
        this.itemID = itemID;
        this.name = name;
        this.retailprice = retailprice;
        this.quantity = quantity;
        this.available = available;
    }
    public Item(String name, double retailprice, int quantity, boolean available){
        this.name = name;
        this.retailprice = retailprice;
        this.quantity = quantity;
        this.available = available;
    }

    public int getItemID() {
        return this.itemID;
    }
    public String getName() {
        return this.name;
    }
    public double getRetailprice() {
        return this.retailprice;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
