package dhbw.sa.databaseApplication.database;

public class Gastronomy {
    /**
     * TODO ergänzen eines Datensatzes in der Datenbank, damit aus dem GUI die Gastro-Anschrift bearbeitet werden kann
     */
    private static String name = "Restaurante Gaumenfreude";
    private static String adress = "Gourmetstraße 11\n12345 Leckerschmeckerhausen";
    private static String telephonenumber = "+49 541 466 655";

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
