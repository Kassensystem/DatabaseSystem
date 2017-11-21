package dhbw.sa.databaseApplication.restApi.client;

import dhbw.sa.databaseApplication.database.entity.Item;
import dhbw.sa.databaseApplication.database.entity.Order;
import dhbw.sa.databaseApplication.database.entity.Table;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 *
 * @author Marvin Mai
 */
public class RestApiClient implements RestApiClient_Interface {

    private String REST_SERVICE_URL;

    public RestApiClient() {
        this.REST_SERVICE_URL = "http://localhost:8080/api";
        System.out.println(
                "\n------------WARNUNG-----------\nDiesen Konstruktor nur verwenden, wenn der Client auf dem selben PC läuft wie der Controller! " +
                "\nAnsonsten Konstruktor mit Angabe der REST_SERVICE_URL verwenden!\n------------------------------\n"
        );
    }

    public RestApiClient(String REST_SERVICE_URL) {
        /*
          Die REST_SERVICE_URL muss vom Benutzer in den Einstellungen angegeben werden.
         */
        this.REST_SERVICE_URL = REST_SERVICE_URL;
    }

    /*GET*/

    public ArrayList<Item> getAllItems() throws Exception {
        System.out.println("Getting all items...");

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ArrayList<Item>> responseEntity =
                    restTemplate.exchange
                            (REST_SERVICE_URL + "/items", HttpMethod.GET,
                                    null, new ParameterizedTypeReference<ArrayList<Item>>() {});

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            String message = getMessage(e.getResponseBodyAsString());
            throw new Exception(message);
        }
    }

    public ArrayList<Table> getAllTables() throws Exception {
        System.out.println("Getting all tables...");

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ArrayList<Table>> responseEntity =
                    restTemplate.exchange
                            (REST_SERVICE_URL + "/tables", HttpMethod.GET,
                                    null, new ParameterizedTypeReference<ArrayList<Table>>() {});

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            String message = getMessage(e.getResponseBodyAsString());
            throw new Exception(message);
        }
    }

    public ArrayList<Order> getAllOrders() throws Exception {
        System.out.println("Getting all orders...");

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ArrayList<Order>> responseEntity =
                    restTemplate.exchange
                            (REST_SERVICE_URL + "/orders", HttpMethod.GET,
                                    null, new ParameterizedTypeReference<ArrayList<Order>>() {});

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            String message = getMessage(e.getResponseBodyAsString());
            throw new Exception(message);
        }
    }

    /*POST/PUT*/

    public void createOrder(Order order) throws Exception {
        System.out.println("Creating order...");

        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.postForLocation(REST_SERVICE_URL + "/order/", order, Order.class);
        } catch (HttpClientErrorException e) {
            String message = getMessage(e.getResponseBodyAsString());
            throw new Exception(message);
        }
    }

    public void updateOrder(int orderID, Order order) throws Exception {
        System.out.println("Updating order...");

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.put(REST_SERVICE_URL + "/order/" + orderID, order);
        } catch (HttpClientErrorException e) {
            String message = getMessage(e.getResponseBodyAsString());
            throw new Exception(message);
        }
    }

    /**
     * Extrahiert die Fehlermessage aus dem Body der JSON-Rückmeldung
     * @param body body der Rückmeldung des RestApiContorllers in JSON
     * @return extrahierte Fehlermessage des RestApiControllers
     */
    private static String getMessage(String body) {
        int lastindex = body.lastIndexOf("message");
        char [] charArray = body.toCharArray();
        int index = lastindex + 10;
        char newChar = charArray[index];
        StringBuilder message = new StringBuilder();
        while(!String.valueOf(newChar).equals("\"")) {
            message.append(newChar);
            newChar = charArray[++index];
        }
        return message.toString();
    }
}
