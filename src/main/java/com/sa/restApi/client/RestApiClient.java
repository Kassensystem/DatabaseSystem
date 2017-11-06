package com.sa.restApi.client;

import com.sa.database.Item;
import com.sa.database.Order;
import com.sa.database.Table;
import com.sa.exceptions.ControllerConnectionException;
import com.sa.exceptions.MySQLException;
import com.sa.exceptions.NoContentException;
import com.sa.restApi.RestApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.*;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

/**
 * ------------Erläuterung der Exceptions------------
 * Bei ResourceAccessException: Der Controller ist nicht erreichbar
 * --> HttpServerErrorException: Der DatabaseService ist nicht erreichbar.
 */

public class RestApiClient {

    public static final String REST_SERVICE_URL = RestApiProperties.getRestServiceUrl();

    /******************TEST*******************/
    public static String test()
            throws  ControllerConnectionException,
                    MySQLException,
                    NoContentException {
        /**
         * TODO Lösung für Exceptions-Empfang finden
         */

        System.out.println("Testing connection to RestApiController...");

        RestTemplate restTemplate = new RestTemplate();
        String s = null;
        ResponseEntity<String> responseEntity;
        //s = restTemplate.getForObject(REST_SERVICE_URL + "/test", String.class);
        try {
            responseEntity = restTemplate.exchange
                    (REST_SERVICE_URL + "/test", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
            return responseEntity.getBody();

        } catch (HttpServerErrorException e) {
            //e.printStackTrace();
            /**
             * Controller ist erreichbar aber meldet
             * eine Exception. Fehler-Code muss extrahiert werden.
             */
            System.out.println("------\nController meldet Fehler\n------");
            switch(e.getResponseBodyAsString()) {
                case "SERVICE_UNAVAILABLE":
                    throw new MySQLException();
                case "NO_CONTENT":
                    throw new NoContentException();
            }
        } catch (ResourceAccessException e) {
            /**
             * Rest-Api-Controller ist nicht erreichbar.
             */
            throw new ControllerConnectionException();
        }
        return null;
    }


    /*******************GET*******************/

    public static ArrayList<Item> getAllItems() throws Exception {
        System.out.println("Getting all items...");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ArrayList<Item>> responseEntity =
                restTemplate.exchange
                        (REST_SERVICE_URL + "/items", HttpMethod.GET,
                                null, new ParameterizedTypeReference<ArrayList<Item>>() {});
        ArrayList<Item> items = responseEntity.getBody();

        return items;
    }

    public static ArrayList<Table> getAllTables() throws Exception {
        System.out.println("Getting all tables...");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ArrayList<Table>> responseEntity =
                restTemplate.exchange
                        (REST_SERVICE_URL + "/tables", HttpMethod.GET,
                                null, new ParameterizedTypeReference<ArrayList<Table>>() {});
        ArrayList<Table> tables = responseEntity.getBody();

        return tables;
    }

    public static ArrayList<Order> getAllOrders() throws Exception {
        // TODO Ergänzen einer Abfrage an den Controller zum Erhalten aller Orders

        return null;
    }

    /******************POST*******************/

    public static void createOrder(Order order) throws Exception {
        System.out.println("Creating order...");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = restTemplate.postForLocation(REST_SERVICE_URL + "/order/", order, Order.class);
    }

    public static void updateOrder(Order order) throws Exception {
        // TODO
    }

}
