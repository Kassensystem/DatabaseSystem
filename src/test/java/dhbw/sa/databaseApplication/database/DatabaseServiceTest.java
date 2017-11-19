package dhbw.sa.databaseApplication.database;

import dhbw.sa.databaseApplication.database.entity.Order;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseServiceTest {

    Order oldOrder = new Order(1, "1;2;3;4;", 5, 5.55, DateTime.now(), true);
    Order newOrder = new Order(1, "2;3;3;4;4;5;6;7;8;9;15;", 5, 5.55, DateTime.now(), true);
    String expectedNewItemIDs = "3;4;5;6;7;8;9;15;";
    @Test
    void getDiffOrder() {
        //Order diffOrder = DatabaseService.getDiffOrder(oldOrder, newOrder);
        //assertEquals(expectedNewItemIDs, diffOrder.getItems());
    }

}