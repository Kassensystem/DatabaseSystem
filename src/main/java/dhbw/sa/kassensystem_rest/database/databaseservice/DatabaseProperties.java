package dhbw.sa.kassensystem_rest.database.databaseservice;

/**
 * {@inheritDoc}
 *
 * Implementierung des DatabaseProperties_Interfaces
 *
 * @author Marvin Mai
 */
class DatabaseProperties
{
    private static String url = "jdbc:mysql://localhost:3306/?verifyServerCertificate=false&useSSL=true";
    private static String database = "Kassensystem";
    private static String username = "DatabaseService";
    private static String password = "password";

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
