package dhbw.sa.database;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Order {
    @JsonProperty private int orderID;
    @JsonProperty private String itemIDs;
    @JsonProperty private int tableID;
    @JsonProperty private double price;
    @JsonProperty private org.joda.time.DateTime date;
    @JsonProperty private boolean paid;

    /**********Constructors***********/

    @JsonCreator
    public Order(@JsonProperty("orderID") int orderID,
          @JsonProperty("itemIDs") String itemIDs,
          @JsonProperty("tableID") int tableID,
          @JsonProperty("price") double price,
          @JsonProperty("date") DateTime date,
          @JsonProperty("paid") boolean paid) {
        this.orderID = orderID;
        this.itemIDs = itemIDs;
        this.tableID = tableID;
        this.price = price;
        this.date = date;
        this.paid = paid;
    }

    public Order() {
        //default constructor
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

    /***************GETTER*****************/

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

    /*************SETTER******************/

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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
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

    public static String joinIDsIntoString(ArrayList<Item> items) {
        String IDString = "";
        for(Item i: items) {
            IDString += i.getItemID() + ";";
        }
        return IDString;
    }

    public Table getTable(ArrayList<Table> allTables) {
        for(Table t: allTables) {
            if(t.getTableID() == this.tableID)
                return t;
        }
        return null;
    }

}
