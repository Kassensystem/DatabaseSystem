package dhbw.sa.restApi.controller;

import dhbw.sa.database.DatabaseService;
import dhbw.sa.database.Item;
import dhbw.sa.database.Order;
import dhbw.sa.database.Table;
import dhbw.sa.restApi.errorHandling.ApiError;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    DatabaseService databaseService;

    /********************TESTS********************/

    @RequestMapping("/test")
    public ResponseEntity<Object> getTest() {
        /**
         * TODO Lösung für Exceptions-Response finden
         */
        String s = "unerreichbarTest";
        if(s == "erreichbarTest")
            return new ResponseEntity(s, HttpStatus.OK);
        else if(s == "unerreichbarTest") {
            /**
             *
             */
            ApiError apiError = new ApiError(
                    HttpStatus.SERVICE_UNAVAILABLE, "SQL nicht erreichbar");
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
            //throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE);
        }
        else if (s == "keinInhalt") {
            /**
             *
             */
            //throw new HttpServerErrorException(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity("--------\ndefault\n---------", HttpStatus.OK);
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

    /**
     * neue Methode zum Updaten einer Order ergänzen
     */
    @RequestMapping(value = "/order/{id}", method = RequestMethod.POST)
    public void updateOrder(@RequestBody Order order) {
        /**
         *  TODO Ergänzen einer Aktualisierung einer Order mit gegebener ID
         *  --> ID wird in Pfad mit angegeben
         *  POST oder PUT oder was zum Aktualisieren?
         */
        //databaseService.updateOrder(ID, order);
    }

}
