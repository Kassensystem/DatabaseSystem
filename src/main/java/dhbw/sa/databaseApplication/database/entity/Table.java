package dhbw.sa.databaseApplication.database.entity;

import org.springframework.stereotype.Component;

@Component
public class Table {
    private int tableID;
    private String name;
    private boolean available;

    public Table() {
        //default constructor
    }

    public Table(int tableID, String name) {
        this.tableID = tableID;
        this.name = name;
        this.available = true;
    }
    public Table(int tableID, String name, boolean available) {
        this.tableID = tableID;
        this.name = name;
        this.available = available;
    }

    public Table(String name) {
        this.name = name;
    }

    public int getTableID() {
        return this.tableID;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
