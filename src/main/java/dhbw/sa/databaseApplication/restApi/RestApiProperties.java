package dhbw.sa.databaseApplication.restApi;

public class RestApiProperties {
    private static String REST_SERVICE_URL = "http://localhost:8080/api";

    public static String getRestServiceUrl() {
        return REST_SERVICE_URL;
    }
}
