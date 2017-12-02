package dhbw.sa.kassensystem_rest.database.printer;


import dhbw.sa.kassensystem_rest.database.entity.Item;

import java.util.ArrayList;

/**
 * In einer PrintableOrder werden die Daten einer Order in primitiven Datentypen gespeichert, um diese anschließend#
 * mit dem PrinterService ausdrucken zu können.s
 *
 * @author Marvin Mai
 */
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
