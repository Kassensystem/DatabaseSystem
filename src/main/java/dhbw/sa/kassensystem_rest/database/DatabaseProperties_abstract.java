package dhbw.sa.kassensystem_rest.database;

/**
 * Klasse für Einstellungen, die benötigt werden, um sich mit der MySQL-Datenbank zu verbinden.
 * Der username und password muss in der Datenbank mit entsprechenden Zugriffsrechten hinterlegt
 * sein.
 *
 * @author Marvin Mai
 */
abstract class DatabaseProperties_abstract {
    private String url = "jdbc:mysql://localhost:3306/?verifyServerCertificate=false&useSSL=true";
    private String database = "Kassensystem";
    private static String username;
    private static String password;

    DatabaseProperties_abstract() {
        DatabaseProperties_abstract.username = "DatabaseService";
        DatabaseProperties_abstract.password = "password";
    }

    public String getUrl() {
        return this.url;
    }

    public String getDatabase() { return this.database; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
