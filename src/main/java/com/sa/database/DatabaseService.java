package com.sa.database;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class DatabaseService implements DatabaseService_Interface{
    private DatabaseProperties dbp = new DatabaseProperties();
    private Connection connection = null;

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Table> tables = new ArrayList<>();
    ArrayList<Order> orders = new ArrayList<>();

    DatabaseService() {
        this.connect();
    }

    @Override
    public void connect() {
        System.out.println("Connecting database...");

        try{
            connection = DriverManager.getConnection(dbp.getUrl(), dbp.getUsername(), dbp.getPassword());
            System.out.println("\nDatabase connected!");
        }catch(SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    @Override
    public void disconnect() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("\nDatabase disconnected!");
            } catch(SQLException e)  { e.printStackTrace();}
        }
    }

    //region Getting Table-Data from the database
    @Override
    public ArrayList<Item> getAllItems()  {
        this.items.clear();

        try {
            String query = "SELECT itemID, name, retailprice, quantity " +
                    "FROM " + dbp.getDatabase() + ".items";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each Item from DB
                int itemID = rs.getInt("itemID");
                String name = rs.getString("name");
                double retailprice = rs.getFloat("retailprice");
                retailprice = round(retailprice);
                int quantity = rs.getInt("quantity");
                items.add(new Item(itemID, name, retailprice, quantity));
            }
            return this.items;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<Table> getAllTables() {
        this.tables.clear();

        try {
            String query = "SELECT tableID, name " +
                    "FROM " + dbp.getDatabase() + ".tables";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each table from DB
                int tableID = rs.getInt("tableID");
                String name = rs.getString("name");
                tables.add(new Table(tableID, name));
            }
            return this.tables;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<Order> getAllOrders() {
        this.orders.clear();

        try {
            String query = "SELECT orderID, itemIDs, price, date, tableID " +
                    "FROM " + dbp.getDatabase() + ".orders";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                int orderID = rs.getInt("orderID");
                String itemIdString = rs.getString("itemIDs");
                ArrayList<Item> items = getItemListByID(itemIdString);
                int tableID = rs.getInt("tableID");
                Table table = getTableByID(tableID);
                double price = rs.getFloat("price");
                price = round(price);
                DateTime dateTime = convertSqlTimestampToJodaDateTime(rs.getTimestamp("date"));

                this.orders.add(new Order(orderID, items, table, price, dateTime));
            }
            return this.orders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Adding data to the database
    @Override
    public void addItem(Item item) {

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".items(itemID, name, retailprice, quantity) " +
                            "VALUES(DEFAULT, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setInt(3, item.getQuantity());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void addTable(Table table) {

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".tables(tableID, name) " +
                            "VALUES(DEFAULT, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void addOrder(Order order) {

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".orders(orderID, itemIDs, price, date, tableID) " +
                            "VALUES(DEFAULT, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, joinIDsIntoString(order.getItems()));
            pst.setDouble(2, order.getPrice());
            pst.setObject(3, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(4, order.getTable().getTableID());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    //endregion

    //region Updating data in database
    @Override
    public void updateItem(int itemID, Item item) {

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".items " +
                            "SET name = ?, retailprice = ?, quantity = ? " +
                            "WHERE ID = " + itemID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setInt(3, item.getQuantity());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void updateTable(int tableID, Table table) {

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".tables " +
                            "SET name = ? " +
                            "WHERE ID = " + tableID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void updateOrder(int orderID, Order order) {
        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".orders " +
                            "SET itemIDs = ?, price = ?, date = ?, tableID = ? " +
                            "WHERE ID = " + orderID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, joinIDsIntoString(order.getItems()));
            pst.setDouble(2, order.getPrice());
            pst.setObject(3, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(4, order.getTable().getTableID());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    //endregion

    private String joinIDsIntoString(ArrayList<Item> items) {
        String IDString = "";
        for(Item i: items) {
            IDString += i.getItemID() + ";";
        }
        return IDString;
    }
    private double round(double number) {
        return (double) Math.round(number * 100d) / 100d;
    }
    private Object convertJodaDateTimeToSqlTimestamp(DateTime dateTime) {
        // Convert sql-Timestamp to joda.DateTime
        return new Timestamp(dateTime.getMillis());
    }
    private DateTime convertSqlTimestampToJodaDateTime(Timestamp sqlTimestamp) {
        //Convert joda.DateTime to sql-Timestamp
        return new DateTime(sqlTimestamp);
    }

    //region Getting items by a string with multiple IDs with the syntax: "10;11;12;13;14"
    private ArrayList<Item> getItemListByID(String itemIDString) {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Integer> itemIDs = splitItemIDString(itemIDString);
        for(Integer i: itemIDs) {
            Item item = getItemByID(i);
            items.add(item);
        }
        return items;
    }
    private ArrayList<Integer> splitItemIDString(String itemIDString) {
        ArrayList<Integer> itemIDList = new ArrayList<>();
        for(String itemID: itemIDString.split(";")) {
            itemIDList.add(Integer.parseInt(itemID));
        }
        return itemIDList;
    }
    private Item getItemByID(int itemID) {
        for(Item i: items) {
            if(i.getItemID() == itemID)
                return i;
        }
        return null;
    }
    private Table getTableByID(int tableID) {
        for(Table t: tables) {
            if(t.getTableID() == tableID)
                return t;
        }
        return null;
    }
    //endregion

    //Convert util- and sql-Date
    private Date localDateToSqlDate(LocalDate localDate) {
        //convert LocalDate to SQL-Date
        return Date.valueOf( localDate );
    }
    private LocalDate sqlDateToLocalDate(Date sqlDate) {
        //Convert sqlDate to LocalDate
        return sqlDate.toLocalDate();
    }

}
