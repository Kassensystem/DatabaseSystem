package dhbw.sa.databaseApplication.database.entity;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test der Klasse {@link Order}
 *
 * @author Marvin Mai
 */
class OrderTest {

    Order testOrder = new Order(1, "2;3;", 3, 1.2, DateTime.now(), false);

    Item testItem1 = new Item(1, "Name1", 1.2, true);
    Item testItem2 = new Item(2, "Name1", 1.2, true);
    Item testItem3 = new Item(3, "Name1", 1.2, true);
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
    @Test
    void getItems() {
        assertEquals(getExpectedOrderItems(), testOrder.getItems(getTestItems()));
    }

    String expectedItemIDs1 = "1;2;3;";
    @Test
    void joinIDsIntoString() {
        assertEquals(expectedItemIDs1, testOrder.joinIDsIntoString(getTestItems()));
    }

    String testItemIDString1 = "1;2;3;4;5;";
    String testItemIDString2 = "1;2;3;4;5";
    ArrayList<Integer> getExpectedIDs() {
        ArrayList<Integer> expectedIDs = new ArrayList<>();
        for(int i = 1; i < 6; i++) {
            expectedIDs.add(i);
        }
        return expectedIDs;
    }
    @Test
    void splitItemIDString() {
        assertEquals(getExpectedIDs(), Order.splitItemIDString(testItemIDString1));
        assertEquals(getExpectedIDs(), Order.splitItemIDString(testItemIDString2));
    }

    ArrayList<Integer> getTestIDs() {
        ArrayList<Integer> expectedIDs = new ArrayList<>();
        for(int i = 1; i < 6; i++) {
            expectedIDs.add(i);
        }
        return expectedIDs;
    }
    String expectedItemIDs2 = "1;2;3;4;5;";
    @Test
    void joinIntIDsIntoString() {
        assertEquals(expectedItemIDs2, Order.joinIntIDsIntoString(getTestIDs()));
    }
}