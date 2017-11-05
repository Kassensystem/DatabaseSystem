package com.sa.restApi.client;

import com.sa.database.Item;
import com.sa.database.Order;
import com.sa.database.Table;
import com.sa.restApi.RestApiProperties;
import org.joda.time.DateTime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;

public class RestApiClient {

    public static final String REST_SERVICE_URI = RestApiProperties.getRestServiceUri();

    /******************TEST*******************/
    public static void test() {
        System.out.println("Testing connection to RestApiController...");

        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.getForObject(REST_SERVICE_URI + "/test", String.class);
        System.out.println(s);
    }

    /*******************GET*******************/

    public static ArrayList<Item> getAllItems() throws Exception {
        System.out.println("Getting all items...");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ArrayList<Item>> responseEntity =
                restTemplate.exchange
                        (REST_SERVICE_URI + "/items", HttpMethod.GET,
                                null, new ParameterizedTypeReference<ArrayList<Item>>() {});

        ArrayList<Item> items = new ArrayList<>();
        if(responseEntity.getStatusCode() == HttpStatus.OK)
            items = responseEntity.getBody();
        else if(responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            throw new Exception("Fehlertext");
        }

        return items;
    }

    public static ArrayList<Table> getAllTables() throws Exception {
        System.out.println("Getting all tables...");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ArrayList<Table>> responseEntity =
                restTemplate.exchange
                        (REST_SERVICE_URI + "/tables", HttpMethod.GET,
                                null, new ParameterizedTypeReference<ArrayList<Table>>() {});
        ArrayList<Table> tables = responseEntity.getBody();

        return tables;
    }

    /******************POST*******************/

    public static void createOrder(Order order) throws Exception {
        System.out.println("Creating order...");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/order/", order, Order.class);
    }


    public static void main(String args[]) {
        test();
        try {
            for(Item i: getAllItems()) {
                System.out.println(i.getItemID() + " \t  " + i.getName() + " \t " + i.getRetailprice() + " \t " + i.getQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        try {
            for(Table t: getAllTables()) {
                System.out.println(t.getTableID() + " \t " + t.getName());
            }
            System.out.println();

            Order newOrder = new Order(getAllItems(), getAllTables().get(2), 7.89);
            createOrder(newOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
