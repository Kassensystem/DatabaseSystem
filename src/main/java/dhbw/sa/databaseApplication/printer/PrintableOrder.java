package dhbw.sa.databaseApplication.printer;

import dhbw.sa.databaseApplication.database.entity.Item;

import java.util.ArrayList;

class PrintableOrder {
    private int orderID;
    private ArrayList<Item> items;
    private String tableName;
    private double price;
    private String date;

    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
