package dhbw.sa.databaseApplication.database.entity;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    Item testItem1 = new Item(1, "Name1", 1.2, true);
    Item testItem2 = new Item(2, "Name1", 1.2, true);
    Item testItem3 = new Item(3, "Name1", 1.2, true);

    Order testOrder = new Order(1, "2;3;", 3, 1.2, DateTime.now(), false);

    ArrayList<Item> getTestItems() {
        ArrayList<Item> testItems = new ArrayList<>();
        testItems.add(testItem1);
        testItems.add(testItem2);
        testItems.add(testItem3);
        return testItems;
    }
    ArrayList<Item> getExpectedOrderItems() {
        ArrayList<Item> testItems = new ArrayList<>();
        testItems.add(testItem2);
        testItems.add(testItem3);
        return testItems;
    }
    String expectedItemIDs = "1;2;3;";

    @Test
    void getItems() {
        assertEquals(getExpectedOrderItems(), testOrder.getItems(getTestItems()));
    }

    @Test
    void joinIDsIntoString() {
        assertEquals(expectedItemIDs, testOrder.joinIDsIntoString(getTestItems()));
    }

}