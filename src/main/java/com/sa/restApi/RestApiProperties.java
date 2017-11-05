package com.sa.restApi;

public class RestApiProperties {
    private static String REST_SERVICE_URI = "http://localhost:8080/api";

    public static String getRestServiceUri() {
        return REST_SERVICE_URI;
    }
}
