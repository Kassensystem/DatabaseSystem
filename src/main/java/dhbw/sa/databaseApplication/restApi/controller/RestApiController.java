package dhbw.sa.databaseApplication.restApi.controller;

import dhbw.sa.databaseApplication.database.DatabaseService;
import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    DatabaseService databaseService;

    /*GET*/

    @RequestMapping("/items")
    public ArrayList<Item> getAllItems() {
        return databaseService.getAllItems();
    }

    @RequestMapping("/orders")
    public ArrayList<Order> getAllOrders() {
        return databaseService.getAllOrders();
    }

    @RequestMapping("/tables")
    public ArrayList<Table> getAllTables() {
        return databaseService.getAllTables();
    }

    /*POST/PUT*/

    @RequestMapping(value = "/order/", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            databaseService.addOrder(order);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrder(@PathVariable("orderID") int orderID, @RequestBody Order order) {
        try {
            databaseService.updateOrder(orderID, order);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }


}
