package dhbw.sa.kassensystem_rest.restApi.controller;

import dhbw.sa.kassensystem_rest.database.databaseservice.DBService_LoginData;
import dhbw.sa.kassensystem_rest.database.databaseservice.DatabaseService;
import dhbw.sa.kassensystem_rest.database.entity.*;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;
import dhbw.sa.kassensystem_rest.exceptions.NotAuthentificatedException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
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
    public ArrayList<Item> getAllItems
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        return databaseService.getAllAvailableItems();
    }

    /**
     * Durch das ansprechen des Pfades ".../api/orders" können die Bestellungen der Datenbank abgefragt werden.
     * @return Liste aller Bestellungen der Datenbank.
     */
    @RequestMapping("/orders")
    public ArrayList<Order> getAllOrders
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        return databaseService.getAllOrders();
    }

    /**
     * Durch das ansprechen des Pfades ".../api/tables" können die Tische der Datenbank abgefragt werden.
     * @return Liste aller Tische der Datenbank.
     */
    @RequestMapping("/tables")
    public ArrayList<Table> getAllTables
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        return databaseService.getAllAvailableTables();
    }


    @RequestMapping("/orderedItems")
    public ArrayList<OrderedItem> getAllOrderedItems
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        return databaseService.getAllOrderedItems();
    }

    @RequestMapping("/unproducedOrderedItems")
	public ArrayList<OrderedItem> getAllUnproducedOrderedItems
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
		return databaseService.getAllUnproducedOrderedItems();
	}

    @RequestMapping("/orderedItems/{orderID}")
    public ArrayList<OrderedItem> getOrderedItemsByOrderId
			(@PathVariable("orderID") int orderId,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        return databaseService.getOrderedItemsByOrderId(orderId);
    }
  
    /*POST/PUT*/

    /**
     * Erstellt eine neue Bestellung in der MySQL-Datenbank.
     * @param order neu zu erstellende Bestellung.
     * @return ResponseEntity, das Erstellen entweder bestätigt oder eine Fehlermeldung liefert.
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<Integer> createOrder
			(@RequestBody Order order,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        try {
            order.setDate(DateTime.now());
            // Anhand der Logindaten die waiterID des zugehörigen waiters ermitteln
			int waiterID = databaseService.getWaiterIdByLoginData(loginname, passwordhash);
            order.setWaiterID(waiterID);
            Integer orderID = databaseService.addOrder(order);

            return new ResponseEntity(orderID, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity<Integer> response = new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
            return response;
        }
    }

    @RequestMapping(value = "/orderedItem", method = RequestMethod.POST)
    public ResponseEntity<?> createOrderedItems
			(@RequestBody ArrayList<OrderedItem> orderedItems,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
		try {
			ArrayList<OrderedItem> newOrderedItems = new ArrayList<>();

			for(OrderedItem o: orderedItems) {
				// alle noch nicht existierenden OrderedItems der DB hinzufügen
				if (!databaseService.existsOrderedItemWithID(o.getOrderedItemID()))
				{
					databaseService.addOrderedItem(o);
					newOrderedItems.add(o);
				}
			}

			// Ausdrucken der hinzugefügten OrderedItems
			if (!newOrderedItems.isEmpty())
				databaseService.printOrder(newOrderedItems.get(0).getOrderID(), newOrderedItems);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/printOrder/{orderID}", method = RequestMethod.POST)
	public ResponseEntity<?> printReceipe
			(@PathVariable("orderID") int orderID,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
		databaseService.printReceipt(orderID);
		return new ResponseEntity(HttpStatus.OK);
	}

    /**
     * Updatet eine bereits existierende Bestellung in der Datenbank.
     * @param orderID Zu aktualisierende Bestellung.
     * @param order Bestellung, deren Daten anstelle der existierenden Bestellung gespeichert werden sollen.
     * @return ResponseEntity, das Updaten entweder bestätigt oder eine Fehlermeldung liefert.
     */
    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrder
			(@PathVariable("orderID") int orderID, @RequestBody Order order,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
        try {
            order.setDate(DateTime.now());
            databaseService.updateOrder(orderID, order);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/orderedItem", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOrderedItems
			(@RequestBody ArrayList<OrderedItem> orderedItems,
			 @RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
		try {
			for (OrderedItem o: orderedItems)
			{
				databaseService.updateOrderedItem(o.getOrderedItemID(), o);
			}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/changeLoginPassword", method = RequestMethod.PUT)
	public boolean updateLogindata(@RequestBody String newPassword,
			@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		authentificate(loginname, passwordhash);
		int waiterID = databaseService.getWaiterIdByLoginData(loginname, passwordhash);
		databaseService.updateLogindata(new Logindata(waiterID, loginname, newPassword));

		return true;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public boolean login
			(@RequestHeader("loginname") String loginname, @RequestHeader("passwordhash") String passwordhash)
	{
		return authentificate(loginname, passwordhash);
	}

	private boolean authentificate(String loginname, String passwordHash)
			throws NotAuthentificatedException
	{
		if (databaseService.authentificate(loginname, passwordHash))
			return true;
		return false;
	}

    //Exception-Handling

    @ExceptionHandler(MySQLServerConnectionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String handleIndexNotFoundException(MySQLServerConnectionException e,
                                                    HttpServletRequest request, HttpServletResponse resp)
	{

        String response = e.getMessage();

        return response;
    }

}
