package dhbw.sa.kassensystem_rest.database;

/**
 * {@inheritDoc}
 *
 * Implementierung des DatabaseProperties_Interfaces
 *
 * @author Marvin Mai
 */
class DatabaseProperties
{
    private String url = "jdbc:mysql://localhost:3306/?verifyServerCertificate=false&useSSL=true";
    private String database = "Kassensystem";
    private static String username;
    private static String password;

    DatabaseProperties() {
        this.username = "DatabaseService";
        this.password = "password";
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
