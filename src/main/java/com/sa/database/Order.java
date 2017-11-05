package com.sa.database;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Order {
    private int orderID;
    private ArrayList<Item> items;
    private Table table;
    private double price;
    private org.joda.time.DateTime date;

    public Order() {
        //default constructor
    }

    Order(int orderID, ArrayList<Item> items, Table table, double price, DateTime date) {
        this.orderID = orderID;
        this.items = items;
        this.table = table;
        this.price = price;
        this.date = date;
    }
    public Order(ArrayList<Item> items, Table table, double price, DateTime date) {
        this.items = items;
        this.table = table;
        this.price = price;
        this.date = date;
    }
    public Order(ArrayList<Item> items, Table table, double price) {
        this.items = items;
        this.table = table;
        this.price = price;
    }

    public int getOrderID() {
        return this.orderID;
    }
    public ArrayList<Item> getItems() {
        return this.items;
    }
    public Table getTable() {
        return this.table;
    }
    public double getPrice() {
        return this.price;
    }
    public DateTime getDate() {
        return this.date;
    }

    public void setDate(DateTime dateTime) {
        this.date = dateTime;
    }
}
