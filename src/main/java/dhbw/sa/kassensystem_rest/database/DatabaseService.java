package dhbw.sa.kassensystem_rest.database;

import dhbw.sa.kassensystem_rest.database.entity.*;
import dhbw.sa.kassensystem_rest.exceptions.DataException;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import dhbw.sa.kassensystem_rest.database.printer.PrinterService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

/**
 * {@inheritDoc}
 *
 * Implementierung des DatabaseService_Interfaces
 *
 * @author Marvin Mai
 */

@Service
public class DatabaseService implements DatabaseService_Interface{
    /*
     *  items Alle in der Datenbank befindlichen Items werden hier gespeichert.
     *  tables Alle in der Datenbank befindlichen Tables werden hier gespeichert.
     *  orders Alle in der Datenbank befindlichen Orders werden hier gespeichert.
     *  itemdeliveries Alle in der Datenbank befindlichen itemdeliveries werden hier gespeichert.
     *  connection Eine Instanz einer Verbindung zur Datenbank.
     *  dbp Beinhaltet eine Beschreibung der Daten die zur Verbindung zur Datenbank noetig sind.
     */

    private final DatabaseProperties dbp = new DatabaseProperties();

    private Connection connection = null;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<OrderedItem> orderedItems = new ArrayList<>();
    private ArrayList<Table> tables = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Itemdelivery> itemdeliveries = new ArrayList<>();
    private ArrayList<Item> itemsWithoutQuantity = new ArrayList<>();

    public DatabaseService() {
        this.connect();
    }

    @Override
    public void connect() {
        logInf("Connecting database...");

        try{
            connection = DriverManager.getConnection(dbp.getUrl(), dbp.getUsername(), dbp.getPassword());
            logInf("Database connected!");
        }catch(SQLException e) {
            logErr("Verbindung zur Datenbank fehlgeschlagen!");
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void disconnect() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
                logInf("Database disconnected!");
            } catch(SQLException e)  { e.printStackTrace();}
        }
    }

    //Getting Table-Data from the database
    @Override
    public ArrayList<Item> getAllItems() throws MySQLServerConnectionException {
        this.items.clear();

        checkConnection();

        logInf("Getting Items from MySQL-Database.");

        this.itemdeliveries = this.getAllItemdeliveries();
        this.orders = this.getAllOrders();
        this.itemsWithoutQuantity = this.getAllItemsEmpty();

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
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public ArrayList<Table> getAllTables() throws MySQLServerConnectionException {
        this.tables.clear();

        checkConnection();

        logInf("Getting Tables from MySQL-Database.");

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
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public ArrayList<Order> getAllOrders() throws MySQLServerConnectionException  {
        this.orders.clear();

        checkConnection();

        logInf("Getting Orders from MySQL-Database.");

        try {
            String query = "SELECT orderID, price, date, tableID, paid " +
                    "FROM " + dbp.getDatabase() + ".orders";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                int orderID = rs.getInt("orderID");
                int tableID = rs.getInt("tableID");
                double price = rs.getFloat("price");
                price = round(price);
                DateTime dateTime = convertSqlTimestampToJodaDateTime(rs.getTimestamp("date"));
                boolean paid = rs.getBoolean("paid");

                this.orders.add(new Order(orderID, tableID, price, dateTime, paid));
            }
            return this.orders;
        } catch (SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public ArrayList<Itemdelivery> getAllItemdeliveries() throws MySQLServerConnectionException {
        this.itemdeliveries.clear();

        checkConnection();

        logInf("Getting Itemdeliveries from MySQL-Database.");

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
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public ArrayList<OrderedItem> getAllOrderedItems() {
        // TODO
        return null;
    }

    //Datenbankinhalte mit Angabe der ID erhalten
    public Order getOrderById(int orderID) throws NullPointerException {

        if(orderID == 0) {
            logErr("Order-ID may not be null.");
            throw new NullPointerException("No Order-ID given.");
        }

        logInf("Getting Order with ID " + orderID + ".");

        for(Order o: this.getAllOrders()) {
            if(o.getOrderID() == orderID)
                return o;
        }

        logErr("Order with ID " + orderID + " doesn't exist in the database.");
        throw new NullPointerException("Order-ID " + orderID + " not found.");
    }

    public Item getItemById(int itemID) throws NullPointerException {

        this.itemdeliveries = this.getAllItemdeliveries();
        this.orders = this.getAllOrders();
        this.items = this.getAllItems();
        this.tables = this.getAllTables();

        if(itemID == 0) {
            logErr("Item-ID may not be null.");
            throw new NullPointerException("No Item-ID given.");
        }

        logInf("Getting Item with ID " + itemID + ".");

        for(Item i: this.items) {
            if(i.getItemID() == itemID)
                return i;
        }

        logErr("Item with ID " + itemID + " doesn't exist in the database.");
        throw new NullPointerException("Item-ID " + itemID + " not found.");
    }

    public Table getTableById(int tableID) throws NullPointerException {

        if(tableID == 0) {
            logErr("Table-ID may not be null.");
            throw new NullPointerException("No Table-ID given.");
        }

        logInf("Getting Table with ID " + tableID + ".");

        this.tables = this.getAllTables();

        for(Table t: this.tables) {
            if(t.getTableID() == tableID)
                return t;
        }

        logErr("Table with ID " + tableID + " doesn't exist in the database.");
        throw new NullPointerException("Table-ID " + tableID + " not found.");
    }

    public Itemdelivery getItemdeliveryById(int itemdeliveryID) throws NullPointerException {

        if(itemdeliveryID == 0) {
            logErr("Itemdelivery-ID may not be null.");
            throw new NullPointerException("No Itemdelivery-ID given.");
        }

        logInf("Getting Itemdelivery with ID " + itemdeliveryID + ".");

        this.itemdeliveries = this.getAllItemdeliveries();

        for(Itemdelivery i: this.itemdeliveries) {
            if(i.getItemdeliveryID() == itemdeliveryID)
                return i;
        }

        logErr("Itemdelivery with ID " + itemdeliveryID + " doesn't exist in the database.");
        throw new NullPointerException("Itemdelivery-ID " + itemdeliveryID + " not found.");

    }

    //Adding data to the database
    @Override
    public void addItem(Item item) throws MySQLServerConnectionException, DataException {

        checkConnection();

        logInf("Adding Item to MySQL-Database.");

        this.items = this.getAllItems();
        this.orders = this.getAllOrders();
        this.itemdeliveries = this.getAllItemdeliveries();

        if(item.getItemID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Item was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        //Vollständigkeit der Order ueberpruefen
        if(item.getQuantity() == 0) {
            logErr("When adding item the quantity may not be zero.");
            throw new DataException("Es wurde keine Anzahl gesetzt!");
        }
        isItemComplete(item);

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".items(itemID, name, retailprice, available) " +
                            "VALUES(DEFAULT, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setBoolean(3, item.isAvailable());
            pst.executeUpdate();

            //ID des neu erzeugten Items ermitteln, um anschließend hierfuer einen neuen Wareneingang anzulegen
            query = "SELECT * FROM " + dbp.getDatabase() + ".items ORDER BY itemID DESC LIMIT 1";
            pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int itemID = 0;
            while(rs.next()) {
                itemID = rs.getInt("itemID");
            }
            Itemdelivery itemdelivery = new Itemdelivery(itemID, item.getQuantity());
            addItemdelivery(itemdelivery);
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void addTable(Table table) throws MySQLServerConnectionException, DataException {

        checkConnection();

        logInf("Adding Table to MySQL-Database.");

        //this.tables = this.getAllTables();

        //Vollständigkeit des Tables überprüfen
        if(table.getTableID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Table was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isTableComplete(table);

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".tables(tableID, name, available) " +
                            "VALUES(DEFAULT, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.setBoolean(2, table.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void addOrder(Order order) throws MySQLServerConnectionException, DataException {

        checkConnection();

        logInf("Adding Order to MySQL-Database.");

        this.orders = this.getAllOrders();
        this.items = this.getAllItems();
        this.tables = this.getAllTables();

        //Vollständigkeit der Order ueberpruefen
        if(order.getOrderID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Order was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isOrderComplete(order);

        if(order.isPaid()) {
            printOrder(order, true);
            printOrder(order, false);
        }
        else
            printOrder(order, true);

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".orders(orderID, price, date, tableID, paid)" +
                            "VALUES(DEFAULT, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setDouble(1, order.getPrice());
            pst.setObject(2, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(3, order.getTable());
            pst.setBoolean(4, order.isPaid());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void addItemdelivery(Itemdelivery itemdelivery) throws MySQLServerConnectionException,
            DataException {

        checkConnection();

        logInf("Adding Itemdelivery to MySQL-Database.");

        this.items = this.getAllItems();

        //Vollständigkeit der Order ueberpruefen
        if(itemdelivery.getItemdeliveryID() != 0) {
            logErr("ID may not be set by the user.");
            logErr("Itemdelivery was not added to the Database!");
            throw new DataException("Es darf keine ID übergeben werden. Die ID wird vom Datenbank-Server gewählt!");
        }
        isItemdeliveryComplete(itemdelivery);

        try {
            String query =  "INSERT INTO " + dbp.getDatabase() + ".itemdeliveries(itemdeliveryID, itemID, quantity) " +
                    "VALUES(DEFAULT, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setInt(1, itemdelivery.getItemID());
            pst.setInt(2, itemdelivery.getQuantity());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }

    //Updating data in database
    @Override
    public void updateItem(int itemID, Item item) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        checkConnection();

        logInf("Updating Item with ID " + itemID + ".");

        this.items = this.getAllItems();

        if(itemID == 0) {
            logErr("Item-ID may not be null.");
            throw new NullPointerException("No Item-ID given.");
        }

        //Existenz eines Items mit der itemID überprüfen
        if(!itemIsAvailable(itemID, this.items)) {
            logErr("Item with ID " + itemID + " does not exist in the database! ");
            throw new DataException("Artikel mit der ID " + itemID + " existiert nicht in der Datenbank!");
        }

        isItemComplete(item);

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".items " +
                            "SET name = ?, retailprice = ?, available = ? " +
                            "WHERE itemID = " + itemID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, item.getName());
            pst.setDouble(2, item.getRetailprice());
            pst.setBoolean(3, item.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void updateTable(int tableID, Table table) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        checkConnection();

        logInf("Updating Table with ID " + tableID + ".");

        this.tables = this.getAllTables();

        if(tableID == 0) {
            logErr("Table-ID may not be null.");
            throw new NullPointerException("No Table-ID given.");
        }

        //Existenz eines Tables mit der tableID überprüfen
        if(!tableIsAvailable(tableID, this.tables)) {
            logErr("Table with ID " + tableID + " does not exist in the database! ");
            throw new DataException("Tisch mit der ID " + tableID + " existiert nicht in der Datenbank!");
        }

        isTableComplete(table);

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".tables " +
                            "SET name = ?, available = ? " +
                            "WHERE tableID = " + tableID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1, table.getName());
            pst.setBoolean(2, table.isAvailable());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void updateOrder(int orderID, Order order) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        checkConnection();

        logInf("Updating Order with ID " + orderID + ".");

        this.orders = this.getAllOrders();
        this.items = this.getAllItems();
        this.tables = this.getAllTables();

        if(orderID == 0) {
            logErr("Order-ID may not be null.");
            throw new NullPointerException("No Order-ID given.");
        }

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID, this.orders)) {
            logErr("Order with ID " + orderID + " does not exist in the database! ");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank!");
        }

        isOrderComplete(order);

        //Kontrollieren ob ID existiert
        if(this.getOrderById(orderID) == null)
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht!");

        /*if(order.isPaid())
            //Kundenbeleg ausdrucken, wenn bezahlt wird
            printOrder(order, false);*/

        //Bei jedem Update die Differenz zur bisherigen Bestellung erkennen und in einem Kuechenbeleg ausdrucken
        // TODO Ausdrucken der DifferenzOrder auf neues System anpassen
        // Das Ausdrucken passiert nun beim Hinzufügen von orderedItems. Dabei wird eine Liste von orderedItems
        // übertragen. Anschließend werden diese Übertragenen Items ausgedruckt.

        try {
            String query =  "UPDATE " + dbp.getDatabase() + ".orders " +
                            "SET price = ?, date = ?, tableID = ?, paid = ? " +
                            "WHERE orderID = " + orderID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setDouble(1, order.getPrice());
            pst.setObject(2, convertJodaDateTimeToSqlTimestamp(order.getDate()) );
            pst.setInt(3, order.getTable());
            pst.setBoolean(4, order.isPaid());
            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    //Deleting data from database
    /*
      Beim Loeschen eines Items oder Tables wird dieser nur als nicht verfuegbar markiert,
      verbleiben aber in der Datenbank.
      Somit ist sichergestellt, dass fuer bisherige Orders alle Daten verfuegbar bleiben.
     */
    @Override
    public void deleteItem(int itemID) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        logInf("Setting Item with ID " + itemID + " to unavailable.");

        Item item = this.getItemById(itemID);

        item.setAvailable(false);

        this.updateItem(itemID, item);
    }
    @Override
    public void deleteTable(int tableID) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        logInf("Setting Table with ID " + tableID + " to unavailable.");

        Table table = this.getTableById(tableID);

        table.setAvailable(false);

        this.updateTable(tableID, table);
    }
    @Override
    public void deleteOrder(int orderID) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        logInf("Deleting Order with ID " + orderID + ".");

        this.orders = this.getAllOrders();

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID, this.orders)) {
            logErr("Order with ID " + orderID + " does not exist in the database! Nothing was deleted.");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank! " +
                    "Es konnte nichts gelöscht werden.");
        }

        /*
          Loeschen einer Order loescht diese unwiederruflich aus der Datenbank.
         */
        try {
            String query =  "DELETE FROM " + dbp.getDatabase() + ".orders " +
                    "WHERE orderID = " + orderID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }
    @Override
    public void deleteItemdelivery(int itemdeliveryID) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        logInf("Deleting Itemdelivery with ID " + itemdeliveryID + ".");

        this.itemdeliveries = this.getAllItemdeliveries();

        //Existenz einer Order mit der OrderID überprüfen
        if(!itemdeliveryIsAvailable(itemdeliveryID, this.itemdeliveries)) {
            logErr("Itemdelivery with ID " + itemdeliveryID + " does not exist in the database! Nothing was deleted.");
            throw new DataException("Wareneingang mit der ID " + itemdeliveryID + " existiert nicht in der Datenbank! " +
                    "Es konnte nichts gelöscht werden.");
        }

        try {
            String query =  "DELETE FROM " + dbp.getDatabase() + ".itemdeliveries " +
                    "WHERE itemdeliveryID = " + itemdeliveryID;
            PreparedStatement pst = connection.prepareStatement(query);

            pst.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            this.connect();
            throw new MySQLServerConnectionException();
        }
    }

    //Drucken einer Order

    public void printOrderById(int orderID) throws NullPointerException, DataException,
            MySQLServerConnectionException {

        this.orders = this.getAllOrders();
        this.items = this.getAllItems();
        this.tables = this.getAllTables();

        if(orderID == 0) {
            logErr("Order-ID is null.");
            throw new NullPointerException("No Order-ID given.");
        }

        //Existenz einer Order mit der OrderID überprüfen
        if(!orderIsAvailable(orderID, this.orders)) {
            logErr("Order with ID " + orderID + " does not exist in the database!");
            throw new DataException("Bestellung mit der ID " + orderID + " existiert nicht in der Datenbank! Es kann nichts gedruckt werden.");
        }

        this.printOrder(getOrderById(orderID), false);
    }

    // Vollständigkeit der Daten von Objekten überprüfen
    private boolean isOrderComplete(Order order) {

        String missingAttributs = "";

        if(order.getTable() == 0) {
            missingAttributs += "Tisch ";
            logErr("Table-ID missing.");
        }

        if(!missingAttributs.isEmpty()) {
            logErr("Order was not added to the database!");
            throw new DataException("Die Bestellung ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }

        if(!tableIsAvailable(order.getTable(), this.tables)) {
            logErr("The Table-ID does not exist in the database!");
            logErr("Order was not added to the database!");
            throw new DataException("Die angegebene Table-ID existiert nicht in der Datenbank!");
        }

        return true;

    }
    private boolean isTableComplete(Table table) {

        String missingAttributs = "";

        if(table.getName().isEmpty()) {
            missingAttributs += " Name";
            logErr("Table missing.");
        }
        //Nur Verfügbarkeit von neuen Artikeln darf nicht auf false gesetzt werden
        if(table.isAvailable() == false && table.getTableID() == 0) {
            missingAttributs += "Available ";
            logErr("New Table cannot be set unavailable.");
        }
        if(!missingAttributs.isEmpty()) {
            logErr("Table was not added to the database!");
            throw new DataException("Der Tisch ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }
        return true;

    }
    private boolean isItemComplete(Item item) {

        String missingAttributs = "";

        if(item.getName().isEmpty() || item.getName() == null) {
            missingAttributs += "Name ";
            logErr("Name is missing.");
        }
        //Nur Verfügbarkeit von neuen Artikeln darf nicht auf false gesetzt werden
        if(item.isAvailable() == false && item.getItemID() == 0) {
            missingAttributs += "Available ";
            logErr("New Item cannot be set unavailable.");
        }
        if(!missingAttributs.isEmpty()) {
            logErr("Item was not added to the Database!");
            throw new DataException("Der Artikel ist unvollständig! Folgende Attribute fehlen: " + missingAttributs);
        }
        return true;

    }
    private boolean isItemdeliveryComplete(Itemdelivery itemdelivery) {

        String missingAttributs = "";

        if(itemdelivery.getItemID() == 0) {
            missingAttributs += " Artikel";
            logErr("Item-ID missing");
        }
        if(itemdelivery.getQuantity() == 0) {
            missingAttributs += " Anzahl";
            logErr("Quantity missing.");
        }

        if(!missingAttributs.isEmpty()) {
            logErr("Itemdelivery was not added to the database!");
            throw new DataException("Der Wareneingang ist unvollständig! Die folgenden Parameter fehlen: " + missingAttributs);
        }

        if(!itemIsAvailable(itemdelivery.getItemID(), this.items)) {
            logErr("The Item-IDs does not exist in the Database!");
            logErr("Itemdelivery was not added to the Database!");
            throw new DataException("Die angegebene Artikel-ID existieren nicht in der Datenbank.");
        }

        return true;

    }

    private boolean itemIsAvailable(int itemID, ArrayList<Item> items) {
        for(Item i: items) {
            if(i.getItemID() == itemID)
                return true;
        }
        return false;
    }
    private boolean orderIsAvailable(int orderID, ArrayList<Order> orders) {
        for(Order o: orders) {
            if(orderID == o.getOrderID())
                return true;
        }
        return false;
    }
    private boolean tableIsAvailable(int tableID, ArrayList<Table> tables) {
        for(Table t: tables) {
            if(tableID == t.getTableID())
                return true;
        }
        return false;
    }
    private boolean itemdeliveryIsAvailable(int itemdeliveryID, ArrayList<Itemdelivery> itemdeliveries) {
        for(Itemdelivery i: itemdeliveries) {
            if(itemdeliveryID == i.getItemdeliveryID())
                return true;
        }
        return false;
    }

    /**
     * Druckt eine Order aus.
     * @param order die auszudruckende Order.
     * @param kitchenReceipt sagt dem PrinterService, ob es sich um einen Kuechenbeleg oder Kundenbeleg handelt. Layout
     *                       des ausgedruckten Belegs wird dementsprechend geaendert.
     */
    private void printOrder(Order order, boolean kitchenReceipt) {
        PrinterService printerService = new PrinterService();
        printerService.printOrder(order,  this.items, this.orderedItems, this.tables, kitchenReceipt);
    }

    /**
     * Ermittelt die aktuelle Verfuegbarkeit eines Items anhand der Wareneingaenge und Warenausgaenge.
     * @param itemID des items, dessen Haeufigkeit ermittelt werden soll.
     * @return aktuelle Verfuegbarkeit des items.
     */
    private int getItemQuantity(int itemID) {
        //Ermitteln der Wareneingaenge
        int itemdeliveries = 0;
        for(Itemdelivery i: this.itemdeliveries) {
            if(i.getItemID() == itemID)
                itemdeliveries += i.getQuantity();
        }
        //Ermitteln der Warenausgaenge
        int itemorders = 0;
        // TODO berechnung der Warenausgänge implementieren


        return itemdeliveries - itemorders;
    }

    /**
     * @return eine Liste von allen Items der Datenbank ohne die Anzahl zu bestimmen
     */
    private ArrayList<Item> getAllItemsEmpty()  {
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

    /**
     * Ermittelt die Haeufigkeit einer ID in einer Liste von IDs.
     * @param id ID desjenigen Items, dessen Haeufigkeit in den itemIDs ermittelt werden soll.
     * @param itemIDs ArrayList von Integers mit itemIDs.
     * @return wie oft die id in den itemIDs vorkommt.
     */
    private static int getItemQuantity(int id, ArrayList<Integer> itemIDs) {
        if(!itemIDs.contains(id))
            return 0;
        int quantity = 0;
        for(Integer i: itemIDs) {
            if(i.intValue() == id)
                quantity++;
        }
        return quantity;
    }

    private double round(double number) {
        return (double) Math.round(number * 100d) / 100d;
    }

    //Konverter

    /**
     * Konvertiert ein joda.time.DateTime in einen Timestamp, der in der SQL-Datenbank gespeicher weren kann
     * @param dateTime zu konvertierendes joda.time.DateTime
     * @return Timestamp
     */
    private Object convertJodaDateTimeToSqlTimestamp(DateTime dateTime) {
        // Convert sql-Timestamp to joda.DateTime
        return new Timestamp(dateTime.getMillis());
    }

    /**
     * Konvertiert einen Timestamp aus der SQL-Datenbank in eine joda.time.DateTime
     * @param sqlTimestamp zu konvertierender Timestamp
     * @return jode.time.DateTime
     */
    private DateTime convertSqlTimestampToJodaDateTime(Timestamp sqlTimestamp) {
        //Convert joda.DateTime to sql-Timestamp
        return new DateTime(sqlTimestamp);
    }

    private void logErr(String errorMessage) {
        log(errorMessage, "ERROR");
    }

    private void logInf(String message) {
        log(message, "INFO");
    }

    private void log(String message, String status) {
        String messageStatus = "";
        switch(status) {
            case "INFO": messageStatus = "MYSQL-Info";
            break;
            case "ERROR": messageStatus = "MYSQL-ERROR";
            break;
        }
        String logString = DateTime.now().toString("yyyy-MM-dd kk:mm:ss.SSS") + "  " + messageStatus + " " + message;
        System.out.println(logString);
    }

    private void checkConnection() {
        try {
            if(connection.isClosed())
                this.connect();
        } catch (SQLException e) {}
    }
}
