package dhbw.sa.databaseApplication.database;

import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Itemdelivery;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;
import dhbw.sa.databaseApplication.printer.PrinterService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class DatabaseService implements DatabaseService_Interface{
    private DatabaseProperties dbp = new DatabaseProperties();
    private Connection connection = null;

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Table> tables = new ArrayList<>();
    ArrayList<Order> orders = new ArrayList<>();
    ArrayList<Itemdelivery> itemdeliveries = new ArrayList<>();

    public DatabaseService() {
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
            String query = "SELECT itemID, name, retailprice, available " +
                    "FROM " + dbp.getDatabase() + ".items";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each Item from DB
                int itemID = rs.getInt("itemID");
                String name = rs.getString("name");
                double retailprice = rs.getFloat("retailprice");
                retailprice = round(retailprice);

                int quantity = getItemQuantity(itemID);

                boolean available = rs.getBoolean("available");
                items.add(new Item(itemID, name, retailprice, quantity, available));
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
            String query = "SELECT tableID, name, available " +
                    "FROM " + dbp.getDatabase() + ".tables";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each table from DB
                int tableID = rs.getInt("tableID");
                String name = rs.getString("name");
                boolean available = rs.getBoolean("available");
                tables.add(new Table(tableID, name, available));
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
            String query = "SELECT orderID, itemIDs, price, date, tableID, paid " +
                    "FROM " + dbp.getDatabase() + ".orders";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                int orderID = rs.getInt("orderID");
                String itemIdString = rs.getString("itemIDs");
                int tableID = rs.getInt("tableID");
                double price = rs.getFloat("price");
                price = round(price);
                DateTime dateTime = convertSqlTimestampToJodaDateTime(rs.getTimestamp("date"));
                boolean paid = rs.getBoolean("paid");

                this.orders.add(new Order(orderID, itemIdString, tableID, price, dateTime, paid));
            }
            return this.orders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<Itemdelivery> getAllItemdeliveries() {
        this.itemdeliveries.clear();

        try {
            String query = "SELECT itemdeliveryID, itemID, quantity " +
                    "FROM " + dbp.getDatabase() + ".itemdeliveries";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each Itemdelivery from DB
                int itemdeliveryID = rs.getInt("itemdeliveryID");
                int itemID = rs.getInt("itemID");
                int quantity = rs.getInt("quantity");
                itemdeliveries.add(new Itemdelivery(itemdeliveryID, itemID, quantity));
            }
            return this.itemdeliveries;
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
            String query =  "INSERT INTO " + dbp.getDatabase() + ".items(itemID, name, retailprice, available) " +
                            "VALUES(DEFAULT, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setBoolean(3, item.isAvailable());
            pst.executeUpdate();

            //ID des neu erzeugten Items ermitteln, um anschließend hierfür einen neuen Wareneingang anzulegen
            query = "SELECT * FROM " + dbp.getDatabase() + ".items ORDER BY itemID DESC LIMIT 1";
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int itemID = 0;
            while(rs.next()) {
                itemID = rs.getInt("itemID");
            }
            Itemdelivery itemdelivery = new Itemdelivery(itemID, item.getQuantity());
            addItemdelivery(itemdelivery);
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void addTable(Table table) {

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".tables(tableID, name, available) " +
                            "VALUES(DEFAULT, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.setBoolean(2, table.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void addOrder(Order order) {

        if(order.isPaid())
            printOrder(order);

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".orders(orderID, itemIDs, price, date, tableID, paid) " +
                            "VALUES(DEFAULT, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, order.getItems());
            pst.setDouble(2, order.getPrice());
            pst.setObject(3, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(4, order.getTable());
            pst.setBoolean(5, order.isPaid());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void addItemdelivery(Itemdelivery itemdelivery) {
        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".itemdeliveries(itemdeliveryID, itemID, quantity) " +
                    "VALUES(DEFAULT, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setInt(1, itemdelivery.getItemID());
            pst.setInt(2, itemdelivery.getQuantity());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    //endregion

    //region Updating data in database
    @Override
    public void updateItem(int itemID, Item item) {

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".items " +
                            "SET name = ?, retailprice = ?, available = ? " +
                            "WHERE itemID = " + itemID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setBoolean(3, item.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void updateTable(int tableID, Table table) {

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".tables " +
                            "SET name = ?, available = ? " +
                            "WHERE tableID = " + tableID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.setBoolean(2, table.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    @Override
    public void updateOrder(int orderID, Order order) {

        if(order.isPaid())
            printOrder(order);

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".orders " +
                            "SET itemIDs = ?, price = ?, date = ?, tableID = ?, paid = ? " +
                            "WHERE orderID = " + orderID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, order.getItems());
            pst.setDouble(2, order.getPrice());
            pst.setObject(3, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(4, order.getTable());
            pst.setBoolean(5, order.isPaid());
            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    //endregion

    //region Deleting data from database
    /**
     * Beim Löschen eines Items oder Tables wird dieser nur als nicht verfügbar markiert, verbleiben aber in der Datenbank.
     * Somit ist sichergestellt, dass für bisherige Orders alle Daten verfügbar bleiben.
     */
    @Override
    public void deleteItem(int itemID) {
        Item item = this.getItemById(itemID);

        item.setAvailable(false);

        this.updateItem(itemID, item);
    }
    @Override
    public void deleteTable(int tableID) {
        Table table = this.getTableById(tableID);

        table.setAvailable(false);

        this.updateTable(tableID, table);
    }
    @Override
    public void deleteOrder(int orderID) {
        /**
         * Löschen einer Order löscht diese unwiederruflich aus der Datenbank.
         */
        try {
            String query =  "DELETE FROM " + dbp.getDatabase() + ".orders " +
                    "WHERE orderID = " + orderID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.executeUpdate();
        } catch(SQLException e) { e.printStackTrace(); }
    }
    //endregion

    public int getItemQuantity(int itemID) {
        //Ermitteln der Wareneingänge
        int itemdeliveries = 0;
        for(Itemdelivery i: this.getAllItemdeliveries()) {
            if(i.getItemID() == itemID)
                itemdeliveries += i.getQuantity();
        }
        //Ermitteln der Warenausgänge
        int itemorders = 0;
        ArrayList<Item> allItems = this.getAllItemsEmpty();
        for(Order o: this.getAllOrders()) {
            for(Item i: o.getItems(allItems)) {
                if(i.getItemID() == itemID)
                    itemorders++;
            }
        }
        return itemdeliveries - itemorders;
    }
    public ArrayList<Item> getAllItemsEmpty()  {
        ArrayList<Item> emptyItems = new ArrayList<>();
        try {
            String query = "SELECT itemID, name, retailprice, available " +
                    "FROM " + dbp.getDatabase() + ".items";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                //get each Item from DB without quantity
                int itemID = rs.getInt("itemID");
                String name = rs.getString("name");
                double retailprice = rs.getFloat("retailprice");
                retailprice = round(retailprice);
                boolean available = rs.getBoolean("available");
                emptyItems.add(new Item(itemID, name, retailprice, available));
            }
            return emptyItems;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    //Drucken einer Order
    public void printOrder(int orderID) {
        printOrder(getOrderById(orderID));
    }
    public void printOrder(Order order) {
        PrinterService printerService = new PrinterService();
        printerService.printOrder(order,  this.getAllItems(), this.getAllTables());
    }

    public Order getOrderById(int orderID) {
        for(Order o: this.getAllOrders()) {
            if(o.getOrderID() == orderID)
                return o;
        }
        return null;
    }
    public Item getItemById(int itemID) {
        for(Item i: this.getAllItems()) {
            if(i.getItemID() == itemID)
                return i;
        }
        return null;
    }
    public Table getTableById(int tableID) {
        for(Table t: this.getAllTables()) {
            if(t.getTableID() == tableID)
                return t;
        }
        return null;
    }
    public Itemdelivery getItemdeliveryById(int itemdeliveryID) {
        for(Itemdelivery i: this.getAllItemdeliveries()) {
            if(i.getItemdeliveryID() == itemdeliveryID)
                return i;
        }
        return null;
    }

}
