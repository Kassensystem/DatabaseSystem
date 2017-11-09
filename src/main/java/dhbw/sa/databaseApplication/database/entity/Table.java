package dhbw.sa.databaseApplication.database.entity;

import org.springframework.stereotype.Component;

@Component
public class Table {
    private int tableID;
    private String name;

    public Table() {
        //default constructor
    }

    public Table(int tableID, String name) {
        this.tableID = tableID;
        this.name = name;
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
}
