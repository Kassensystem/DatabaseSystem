package dhbw.sa.kassensystem_rest.database;

/**
 * Stellt Informationen über die Gastronomie bereit.
 * TODO Die Klasse soll mit einem Eintrag in der MySQL-Datenbank ersetzt werden.
 *
 * @author Marvin Mai
 */
public class Gastronomy {

    public static String getName() {
    	/*
      	 * TODO Ergänzen eines Datensatzes in der Datenbank, damit aus dem GUI die Gastro-Anschrift bearbeitet werden kann
     	 */
        return "Restaurante Gaumenfreude";
    }

    public static String getAdress() {
        return "Gourmetstraße 11\n49082 Leckerschmeckerhausen";
    }

    public static String getTelephonenumber() {
        return "+49 541 123 456";
    }
}
