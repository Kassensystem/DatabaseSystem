package dhbw.sa.kassensystem_rest.database.entity;

import org.springframework.stereotype.Component;

/**
 * Model für einen Datensatz der Datenbanktabelle tables.
 *
 * @author Marvin Mai
 */
@Component
public class Table {
    private int tableID;
    private String name;
    private int seats;
    private boolean available;

    /**
     * Default Constructor
     */
    public Table() {
    }

    /**
     * Konstruktor für einen vollständige Tisch, der aus der MySQL-Datenbank gelesen wurde.
     * @param tableID ID des Tisches aus der Datenbank.
     * @param name Name des Tisches aus der Datenbank.
	 * @param seats Anzahl der Sitze des Tisches
     * @param available Verfügbarkeit des Tisches aus der Datenbank.
     */
    public Table(int tableID, String name, int seats, boolean available) {
        this.tableID = tableID;
        this.name = name;
        this.seats = seats;
        this.available = available;
    }

    /**
     * Konstruktor für das Erstellen eines neuen Tisches, der anschließend an die MySQL-Datenbank
     * übertragen weden soll.
     * @param name Name des neuen Tisches.
	 * @param seats Anzahl der Sitze des Tisches
     * @param available Verfügbarkeit des neuen Tisches
     */
    public Table(String name, int seats, boolean available) {
        this.name = name;
        this.seats = seats;
        this.available = available;
    }

    public int getTableID() {
        return this.tableID;
    }

    public String getName() {
        return this.name;
    }

	public int getSeats()
	{
		return seats;
	}

	public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
