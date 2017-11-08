package dhbw.sa.database;

public class Gastronomy {
    private static String name = "Test Gastronomie";
    private static String adress = "Test Straße\n12345 Testhausen";
    private static String telephonenumber = "+49 123 456789";

    public static String getName() {
        return name;
    }

    public static String getAdress() {
        return adress;
    }

    public static String getTelephonenumber() {
        return telephonenumber;
    }
}
