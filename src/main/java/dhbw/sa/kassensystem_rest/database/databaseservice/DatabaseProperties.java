package dhbw.sa.kassensystem_rest.database.databaseservice;

/**
 * Definition von Eigenschaften, die benötigt werden, um auf die MySQL-Datenbank zuzugreifen.
 *
 * @author Marvin Mai
 */
class DatabaseProperties
{
	// Die folgenden Attribute müssen beim Einrichten der Datenbank verwendet werden:
    private static final String url = "jdbc:mysql://localhost:3306/?verifyServerCertificate=false&useSSL=true";
    private static final String database = "Kassensystem";
    private static final String username = "DatabaseService";
    private static final String password = "password";

    DatabaseProperties() {}

    public static String getUrl() {
        return url;
    }

    public static String getDatabase() { return database; }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
