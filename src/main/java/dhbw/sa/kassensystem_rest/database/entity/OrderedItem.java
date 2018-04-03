package dhbw.sa.kassensystem_rest.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.stereotype.Component;

/**
 * Ein OrderedItem ist ein bestellter Artikel der zu einer Bestellung gehört.
 * Für jeden Artikel, der zu einer Bestellung gehört, wird ein OrderedItem erstellt
 * und in der Datenbank abgelegt.
 *
 * @author Marvin Mai
 */
@Component
public class OrderedItem {
	@JsonProperty private int orderedItemID;
	@JsonProperty private int orderID;
	@JsonProperty private int itemID;
	@JsonProperty private boolean itemPaid;
	@JsonProperty private boolean itemProduced;
	@JsonProperty private String comment;

	public OrderedItem() {}

	/**
	 * Konstruktor zum Abrufen eines vollständigen OrderedItems aus der Datenbank.
	 * @param orderedItemID Die von der Datenbank zugewiesene ID der OrderedItems.
	 * @param orderID Die ID der order, zu der das OrderedItem gehört.
	 * @param itemID Der Artikel, der mit diesem OrderedItem bestellt wurde.
	 * @param itemPaid Ob das OrderedItem bereits bezahlt wurde.
	 * @param itemProduced Ob das OrderedItem bereits produziert wurde.
	 */
	public OrderedItem(@JsonProperty("orderedItemID")int orderedItemID,
					   @JsonProperty("orderID") int orderID,
					   @JsonProperty("itemID") int itemID,
					   @JsonProperty("itemPaid") boolean itemPaid,
					   @JsonProperty("itemProduced") boolean itemProduced,
					   @JsonProperty("comment") String comment) {
        this.orderedItemID = orderedItemID;
        this.orderID = orderID;
        this.itemID = itemID;
        this.itemPaid = itemPaid;
        this.itemProduced = itemProduced;
        this.comment = comment;
    }

	/**
	 * Konstruktor zum Hinzufügen eines neuen OrderedItems in die Datenbank.
	 * @param orderID Die ID der order, zu der das OrderedItem gehört.
	 * @param itemID Der Artikel, der mit diesem OrderedItem bestellt wurde.
	 */
	public OrderedItem(int orderID, int itemID, String comment) {
		this.orderID = orderID;
		this.itemID = itemID;
		this.itemPaid = false;
		this.itemProduced = false;
		this.comment = comment;
	}

    public int getOrderedItemID() {
        return orderedItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getItemID() {
        return itemID;
    }

	public boolean isItemPaid() {
		return itemPaid;
	}

	public boolean isItemProduced()
	{
		return itemProduced;
	}

    public void itemIsPaid() {
        this.itemPaid = true;
    }

	public void itemIsProduced()
	{
		this.itemProduced = true;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
}
