package com.sa.restApi.controller;

import com.sa.database.DatabaseService;
import com.sa.database.Item;
import com.sa.database.Order;
import com.sa.database.Table;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    DatabaseService databaseService;
    private HttpServletResponse response;

    /********************TESTS********************/

    @RequestMapping("/test")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity("testest", HttpStatus.OK);
    }

    /*********************GET**********************/

    @RequestMapping("/items")
    public @ResponseBody ArrayList<Item> getAllItems() {
        return databaseService.getAllItems();
    }

    @RequestMapping("/orders")
    public ArrayList<Order> getAllOrder() {
        return databaseService.getAllOrders();
    }

    @RequestMapping("/tables")
    public ArrayList<Table> getAllTables() {
        return databaseService.getAllTables();
    }

    @RequestMapping(value = "/order/", method = RequestMethod.POST)
    public void createOrder(@RequestBody Order order) {
        order.setDate(DateTime.now());
        databaseService.addOrder(order);
    }

}
