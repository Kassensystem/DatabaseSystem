package com.sa.database;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Order {
    private int orderID;
    private String itemIDs;
    private int tableID;
    private double price;
    private org.joda.time.DateTime date;
    private boolean paid;

    public Order() {
        //default constructor
    }

    Order(int orderID, String itemIDs, int table, double price, DateTime date, boolean paid) {
        this.orderID = orderID;
        this.itemIDs = itemIDs;
        this.tableID = table;
        this.price = price;
        this.date = date;
        this.paid = paid;
    }

    public Order(String itemIDs, int tableID, double price, DateTime date, boolean paid) {
        this.itemIDs = itemIDs;
        this.tableID = tableID;
        this.price = price;
        this.date = date;
        this.paid = paid;
    }

    public Order(String itemIDs, int tableID, double price, boolean paid) {
        this.itemIDs = itemIDs;
        this.tableID = tableID;
        this.price = price;
        this.paid = paid;
    }

    public int getOrderID() {
        return this.orderID;
    }
    public String getItems() {
        return this.itemIDs;
    }
    public int getTable() {
        return this.tableID;
    }
    public double getPrice() {
        return this.price;
    }
    public DateTime getDate() {
        return this.date;
    }
    public boolean isPaid() {
        return paid;
    }

    public void setItemIDs(String itemIDs) {
        this.itemIDs = itemIDs;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setDate(DateTime dateTime) {
        this.date = dateTime;
    }
    public void setPaid() {
        this.paid = true;
    }

    /** Funktionen zum Erhalten der Items, die zu dieser Bestellung gehören.
     *  Zur Verwendung alle Items übergeben, auch nicht verfügbare.
     */
    public ArrayList<Item> getItems(ArrayList<Item> allItems) {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Integer> itemIDs = splitItemIDString(this.itemIDs);
        for(Integer itemID: itemIDs) {
            Item item = getItemByID(itemID, allItems);
            items.add(item);
        }
        return items;
    }
    //region Hilfsmethoden zur Ermittlung der Items
    private ArrayList<Integer> splitItemIDString(String itemIDString) {
        //Ermitteln der einzelnen IDs aus String
        ArrayList<Integer> itemIDList = new ArrayList<>();
        for(String itemID: itemIDString.split(";")) {
            itemIDList.add(Integer.parseInt(itemID));
        }
        return itemIDList;
    }
    private Item getItemByID(int itemID, ArrayList<Item> allItems) {
        for(Item i: allItems) {
            if(i.getItemID() == itemID)
                return i;
        }
        return null;
    }
    //endregion
    public String joinIDsIntoString(ArrayList<Item> items) {
        String IDString = "";
        for(Item i: items) {
            IDString += i.getItemID() + ";";
        }
        return IDString;
    }
}
