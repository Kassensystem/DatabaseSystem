package dhbw.sa.kassensystem_rest.restApi.controller;

import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.Item;
import dhbw.sa.kassensystem_rest.database.entity.Order;
import dhbw.sa.kassensystem_rest.database.entity.OrderedItem;
import dhbw.sa.kassensystem_rest.database.entity.Table;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Der RestApiController stellt einen Server dar, über den Funktionen des DatabaseServices angesprochen werden können.
 * Diese sind über das Netzwerk verfügbar. Dabei müssen die entsprechenden Pfade beachtet werden. Der Root-Pfad ist
 * ".../api".
 *
 * @author Marvin Mai
 */
@RestController
@ComponentScan("dhbw.sa.kassensystem_database.database")
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    DatabaseService databaseService;

    /*GET*/

    /**
     * Durch das ansprechen des Pfades ".../api/items" können die Artikel der Datenbank abgefragt werden.
     * @return Liste aller Artikel der Datenbank.
     */
    @RequestMapping("/items")
    public ArrayList<Item> getAllItems() {
        return databaseService.getAllItems();
    }

    /**
     * Durch das ansprechen des Pfades ".../api/orders" können die Bestellungen der Datenbank abgefragt werden.
     * @return Liste aller Bestellungen der Datenbank.
     */
    @RequestMapping("/orders")
    public ArrayList<Order> getAllOrders() {
        return databaseService.getAllOrders();
    }

    /**
     * Durch das ansprechen des Pfades ".../api/tables" können die Tische der Datenbank abgefragt werden.
     * @return Liste aller Tische der Datenbank.
     */
    @RequestMapping("/tables")
    public ArrayList<Table> getAllTables() {
        return databaseService.getAllTables();
    }

    @RequestMapping("/orderedItems")
    public ArrayList<OrderedItem> getAllOrderedItems() {
        return databaseService.getAllOrderedItems();
    }

    @RequestMapping("/orderedItems/{orderID}")
    public ArrayList<OrderedItem> getOrderedItemsByOrderId(@PathVariable("orderID") int orderId) {
        return databaseService.getOrderedItemsByOrderId(orderId);
    }

    /*POST/PUT*/

    /**
     * Erstellt eine neue Bestellung in der MySQL-Datenbank.
     * @param order neu zu erstellende Bestellung.
     * @return ResponseEntity, das Erstellen entweder bestätigt oder eine Fehlermeldung liefert.
     */
    @RequestMapping(value = "/order/", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            order.setDate(DateTime.now());
            databaseService.addOrder(order);
            // TODO Method für das Ermitteln der letzten hinzugefügten OrderID
            int lastOrderID = 87;
            return new ResponseEntity(lastOrderID, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity<?> response = new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
            return response;
        }
    }

    /**
     * Updatet eine bereits existierende Bestellung in der Datenbank.
     * @param orderID Zu aktualisierende Bestellung.
     * @param order Bestellung, deren Daten anstelle der existierenden Bestellung gespeichert werden sollen.
     * @return ResponseEntity, das Updaten entweder bestätigt oder eine Fehlermeldung liefert.
     */
    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrder(@PathVariable("orderID") int orderID, @RequestBody Order order) {
        try {
            order.setDate(DateTime.now());
            databaseService.updateOrder(orderID, order);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }


    //Exception-Handling

    @ExceptionHandler(MySQLServerConnectionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String handleIndexNotFoundException(MySQLServerConnectionException e,
                                                    HttpServletRequest request, HttpServletResponse resp) {

        String response = e.getMessage();

        return response;
    }


}
