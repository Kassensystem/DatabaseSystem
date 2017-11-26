package dhbw.sa.kassensystem_rest.database.entity;

/**
 * Model für einen Datensatz der Datenbanktabelle Itemdelivery.
 *
 * @author Marvin Mai
 */
public class Itemdelivery {
    private int itemdeliveryID;
    private int itemID;
    private int quantity;

    /**
     * Konstruktor für einen vollständigen Wareneingang, der aus der MySQL-Datenbank gelesen wurde.
     * @param itemdeliveryID ID des Wareneingangs aus der Datenbank.
     * @param itemID Artikel-ID des Wareneingangs aus der Datenbank.
     * @param quantity Anzahl des Wareneingangs aus der Datenbank.
     */
    public Itemdelivery(int itemdeliveryID, int itemID, int quantity) {
        this.itemdeliveryID = itemdeliveryID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    /**
     * Konstruktor zum Erstellen eines neuen Wareneingangs, der anschließend an die MySQL-Datenbank
     * übertragen werden soll.
     * @param itemID Artikel-ID des neuen Wareneingangs.
     * @param quantity Anzahl des neuen Wareneingangs.
     */
    public Itemdelivery(int itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }

    /*Getter*/

    public int getItemdeliveryID() {
        return itemdeliveryID;
    }

    public void setItemdeliveryID(int itemdeliveryID) {
        this.itemdeliveryID = itemdeliveryID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    /*Setter*/

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
