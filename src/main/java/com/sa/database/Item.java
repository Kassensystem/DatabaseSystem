package com.sa.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Item {
    private int itemID;
    private String name;
    private double retailprice;
    private int quantity;

    public Item() {
        //default constructor
    }
    Item(int itemID, String name, double retailprice, int quantity){
        this.itemID = itemID;
        this.name = name;
        this.retailprice = retailprice;
        this.quantity = quantity;
    }
    public Item(String name, double retailprice, int quantity){
        this.name = name;
        this.retailprice = retailprice;
        this.quantity = quantity;
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
}
